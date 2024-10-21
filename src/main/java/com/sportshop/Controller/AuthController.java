package com.sportshop.Controller;

import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    AccountService accountService;

    @GetMapping("/sign-in")
    public String renderSignIn() {
        return "Auth/sign-in";
    }

    @GetMapping("/sign-up")
    public String renderSignUp() {
        return "Auth/sign-up";
    }

    @GetMapping("/success")
    public String render1() {
        return "Auth/access-denied";
    }

    @PostMapping("/sign-up")
    public String renderSignUp(@ModelAttribute AccountDTO accountDTO) {
        System.out.println(accountDTO.getUserName());
        System.out.println(accountDTO.getPassword());
        System.out.println(accountDTO.getRole());
        accountService.createAccount(accountDTO);
        return "welcome";
    }
}
