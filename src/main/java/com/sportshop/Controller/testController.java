package com.sportshop.Controller;

import com.sportshop.Modal.Mail;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.MailService;
import com.sportshop.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/*@RestController*/
@Controller
public class testController {

    @Autowired
    UserService employeeService;

    @Autowired
    AccountService accountService;

    @Autowired
    MailService mailService;

    @GetMapping("/test")
    public String test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password123"));
        System.out.println(encoder.encode("password456"));
        System.out.println(encoder.encode("password789"));
        return "test";
    }

    @GetMapping("/")
    public String defaultpage() {

        return "homepage";
    }

    @GetMapping("/repo")
    @ResponseBody
    public List<UserDTO> getAll()
    {
        List <UserDTO> rs = employeeService.findAll();
        return rs;
    }

    @GetMapping("/account")
    @ResponseBody
    public AccountDTO getAccount(@RequestParam ("username") String username)
    {
        return accountService.findAccountByUserName(username);
    }

    @GetMapping("/dashboard")
    public String render1()
    {
        return "Admin/dashboard";
    }

    @GetMapping("/welcome")
    public String render2()
    {
        return "welcome";
    }

}
