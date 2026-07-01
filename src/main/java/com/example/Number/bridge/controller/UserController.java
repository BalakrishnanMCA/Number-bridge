package com.example.Number.bridge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.Number.bridge.services.UserService;

import jakarta.servlet.http.HttpSession;

import com.example.Number.bridge.SMS.VerifyService;
import com.example.Number.bridge.entity.UserDb;

import java.util.logging.Logger;

@Controller
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    private final UserService userService;
    private final VerifyService verifyService;

    public UserController(UserService userService, VerifyService verifyService) {
        this.userService = userService;
        this.verifyService = verifyService;
    }

    @GetMapping("/Index")
    public String index() {
        return "index";
    }

    @PostMapping("/sentOTP")
    public String sendOtp(@RequestParam("phoneNumber") String phoneNumber, HttpSession session, Model model) {
        UserDb user = new UserDb();
        user.setPhoneNumber(phoneNumber);
        model.addAttribute("otp", userService.checkUser(user.getPhoneNumber(), session));
        return "login";
    }

    // @ResponseBody
    // @PostMapping("/register")
    // public UserDb register(@RequestBody UserDb data) {
    // return userService.registerUser(data);
    // }

    @PostMapping("/verifyOTP")
    public String verifyOtp(@RequestParam("otp") String otp, Model model, HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");

        if (verifyService.checkVerification(phoneNumber, otp)) {
            model.addAttribute("phoneNumber", phoneNumber);
            return "home";
        } else {
            model.addAttribute("otp", "Incorrect OTP");
            return "login";
        }

    }

    @PostMapping("/registerUser")
    public String registerUser(@RequestParam("otp") String otp, Model model, HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");

        if (verifyService.checkVerification(phoneNumber, otp)) {
            model = userService.registerUser(model, session);
            return "login";
        } else {
            model.addAttribute("ack", "Incorrect OTP");
            return "register";
        }

    }

    @PostMapping("/register")
    public String showRegisterPage(UserDb user, HttpSession session, Model model) {
        model.addAttribute("otp", userService.isAlreadyRegistered(user, session));
        return "register";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/chat")
    public String showChatPage(@RequestParam("phoneNumber") String phoneNumber, Model model, HttpSession session) {

        String sender = (String) session.getAttribute("phoneNumber");
        boolean isUserPresent = userService.findByPhoneNumber(phoneNumber).isPresent();
        LOGGER.info("Is user present: " + isUserPresent);

        if (!isUserPresent) {
            model.addAttribute("var1", "user not found, please register!");
            return "home";
        } else if (phoneNumber.equals(sender)) {
            model.addAttribute("var1", "You cannot chat with yourself");
            return "home";
        } else {
            return "chat";
        }

    }

    // @ResponseBody
    // @GetMapping("/search")
    // public Optional<UserDb> search(@RequestParam String phoneNumber) {
    // return userService.findByPhoneNumber(phoneNumber);
    // }
}
