package com.sportshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RenderController {

    @GetMapping("/")
    public String defaultpage() {

        return "homepage";
    }

    @GetMapping("/header")
    public String headerRender() {

        return "templates/header";
    }

    @GetMapping("/footer")
    public String footerRender() {

        return "templates/footer";
    }
}
