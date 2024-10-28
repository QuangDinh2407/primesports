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
        return "redirect:/sign-in";
    }

}
