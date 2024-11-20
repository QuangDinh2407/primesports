package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class customerController {

    @RequestMapping("")

    public String render() {
        return "Common/Customer_Shop/personal-info-begin";
    }

    @RequestMapping("edit-personal")
    public String editPersional() {
        return "Common/Customer_Shop/edit-personal-info";
    }

}