package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class customerController {

    @Autowired
    UserService userService;

    @ModelAttribute
    public void getUser(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        System.out.println(email);
        if (email != null) {
            UserDTO userDTO = userService.findbyEmail(email);
            model.addAttribute("userDTO", userDTO);
            System.out.println(userDTO);
        }
    }
//
//    @GetMapping("")
//    public String render (HttpSession session, Model model){
//        return "Common/Customer_Shop/customer-info";
//    }

    @RequestMapping("")
    public String render() {

        return "/Customer/customer-info";
    }

    @PostMapping("/customer-info")
    public String updateInfo (UserDTO userDTO, Model model){
        System.out.println(userDTO);
        Result rs = userService.updateInfoUser(userDTO);
        model.addAttribute("rs",rs);
        return "homepage";
    }

}