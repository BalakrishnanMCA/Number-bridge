package com.example.Number.bridge.services;

import com.example.Number.bridge.entity.UserDb;
import com.example.Number.bridge.SMS.VerifyService;
import com.example.Number.bridge.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final VerifyService verifyService;

    public UserService(UserRepository userRepository, VerifyService verifyService) {
        this.userRepository = userRepository;
        this.verifyService = verifyService;
    }

    public Model registerUser(Model model, HttpSession session) {

        UserDb data = new UserDb();
        data.setName(String.valueOf(session.getAttribute("name")));
        data.setPhoneNumber(String.valueOf(session.getAttribute("phoneNumber")));
        userRepository.save(data);
        model.addAttribute("ack", "User registered successfully");

        return model;
    }

    public Optional<UserDb> findByPhoneNumber(String phoneNumber) {
        Optional<UserDb> user = userRepository.findByPhoneNumber(phoneNumber);
        LOGGER.log(java.util.logging.Level.INFO, "Phone number: {0}", phoneNumber);
        return user;
    }

    public String checkUser(String phoneNumber, HttpSession session) {

        if (userRepository.findByPhoneNumber(phoneNumber).isEmpty()) {
            return "user not found, please register";
        } else {
            session.setAttribute("phoneNumber", phoneNumber);
            verifyService.sendVerification(phoneNumber);
            return phoneNumber;
        }
    }

    public String isAlreadyRegistered(UserDb user, HttpSession session) {
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isEmpty()) {
            session.setAttribute("phoneNumber", user.getPhoneNumber());
            session.setAttribute("name", user.getName());
            verifyService.sendVerification(user.getPhoneNumber());
            return "otp sent to " + user.getPhoneNumber();
        } else {
            return "user already registered, please login";
        }
    }
}
