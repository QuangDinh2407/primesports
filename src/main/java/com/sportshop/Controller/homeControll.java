package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeControll {

    @GetMapping("/sign-in")
    public String render() {
        return "Auth/sign-in";
    }

    @GetMapping("/success")
    public String render1() {
        return "access-denied";
    }

}
