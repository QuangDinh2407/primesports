package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AccountService accountService;

    @Autowired
    MailService mailService;


    @GetMapping("/sign-in")
    public String renderSignIn() {
        return "Auth/sign-in";
    }

    @GetMapping("/sign-up")
    public String renderSignUp(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "Auth/sign-up";
    }

    @GetMapping("/send-otp")
    public String renderResetPassword() {
        return "Auth/reset-password";
    }

    @GetMapping("/verify-otp")
    public String renderverifyotp(@RequestParam(value ="email" , required = false) String email,Model model) {
        model.addAttribute("email", email);
        System.out.println(email);
        return "Auth/verify-rs-pass";
    }

    @PostMapping("/sign-up")
    public String renderSignUp(@ModelAttribute AccountDTO accountDTO, Model model, HttpServletRequest request) {
        Result rs = accountService.createAccount(accountDTO,request);
        model.addAttribute("message", rs.getMessage());
        model.addAttribute("accountDTO", accountDTO);
        return rs.isSuccess() ? "welcome" : "Auth/sign-up";
    }

    @GetMapping ("/confirm-signup")
    public String renderConfirmSignUp(@RequestParam("email") String email) {
        accountService.confirmSignup(email);
        return "redirect:/auth/sign-in";
    }

    @PostMapping("/send-otp")
    public String rspass(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Result rs = accountService.sendOTPToEmail(email,request);
        model.addAttribute("message", rs.getMessage());
        redirectAttributes.addAttribute("email", email);
        return rs.isSuccess() ? "redirect:/auth/verify-otp" : "Auth/send-otp";
    }

    @PostMapping ("/verify-otp")
    public String verifyotp(@RequestParam("email") String email, @RequestParam ("otp") String otp, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String otpCode = otp.replaceAll(",","");
        Result rs = accountService.verifyOTPandSendPass(otpCode,email,request);
        redirectAttributes.addFlashAttribute("message", rs.getMessage());
        redirectAttributes.addAttribute("email", email);
        return rs.isSuccess() ?  "Auth/sign-in":"redirect:/auth/verify-otp";
    }
}
