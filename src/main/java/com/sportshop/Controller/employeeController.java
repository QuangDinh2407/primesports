package com.sportshop.Controller;

import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class employeeController {

    @Autowired
    UserService userService;

    @ModelAttribute
    public void getUser(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            UserDTO userDTO = userService.findbyEmail(email);
            model.addAttribute("userDTO", userDTO);
        }
    }

    @RequestMapping("")
    public String render (HttpSession session, Model model){
        return "redirect:/employee/home";
    }

    @RequestMapping("/employee-info")
    public String renderuserInfo (HttpSession session, Model model){
        return "Employee/employee-info";
    }

    @RequestMapping("/home")
    public String renderHome (HttpSession session, Model model){
        return "Employee/home";
    }

    @RequestMapping("/dashboard")
    public String renderdashboard (HttpSession session, Model model){
        return "Employee/dashboard";
    }
}
