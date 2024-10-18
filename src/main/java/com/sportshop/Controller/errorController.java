package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class errorController {

    @GetMapping("/access-denied")
    public String render() {
        return "Auth/access-denied";
    }
}
