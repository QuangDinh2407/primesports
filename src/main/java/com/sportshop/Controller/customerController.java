package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class customerController {

    @RequestMapping("")
    public String render (){
        return "Customer/home";
    }
}
