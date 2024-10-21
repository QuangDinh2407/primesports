package com.sportshop.Controller;

import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*@RestController*/
@Controller
public class testController {

    @Autowired
    UserService employeeService;

    @Autowired
    AccountService accountService;

    @GetMapping("/test")
    public String test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password123"));
        System.out.println(encoder.encode("password456"));
        System.out.println(encoder.encode("password789"));
        return "test";
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


}
