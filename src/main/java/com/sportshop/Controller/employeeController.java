package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class employeeController {

    @RequestMapping("")
    public String render (){
        return "Employee/home";
    }
}
