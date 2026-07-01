package com.example.Number.bridge.SMS;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class VerifyService {

    private static final Logger LOGGER = Logger.getLogger(VerifyService.class.getName());

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.verify-service-sid}")
    private String verifyServiceSid;

    public void init() {
        validateTwilioConfig();
        Twilio.init(accountSid, authToken);
    }

    private void validateTwilioConfig() {
        if (isBlank(accountSid) || isBlank(authToken) || isBlank(verifyServiceSid)) {
            throw new IllegalStateException(
                    "Twilio credentials are missing. Start the app with TWILIO_ACCOUNT_SID, "
                            + "TWILIO_AUTH_TOKEN, and TWILIO_VERIFY_SERVICE_SID environment variables.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public void sendVerification(String phoneNumber) {
        init();
        String phoneNum = "+91" + phoneNumber;
        Verification verification = Verification.creator(
                verifyServiceSid,
                phoneNum,
                "sms"
        ).create();
        LOGGER.info("Verification sent: " + verification.getStatus());
    }

    public boolean checkVerification(String phoneNumber, String code) {
        init();
        String phoneNum = "+91" + phoneNumber;

        VerificationCheck verificationCheck = VerificationCheck.creator(
                verifyServiceSid,
                code).setTo(phoneNum).create();
        return "approved".equals(verificationCheck.getStatus());
    }
}
