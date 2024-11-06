package com.sportshop.Controller;

import com.sportshop.Contants.FormatDate;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @GetMapping("")
    public String render (HttpSession session, Model model){
        return "redirect:/employee/home";
    }

    @GetMapping("/employee-info")
    public String renderuserInfo (HttpSession session, Model model){
        return "Employee/employee-info";
    }

    @GetMapping("/home")
    public String renderHome (HttpSession session, Model model){
        return "Employee/home";
    }

    @GetMapping("/dashboard")
    public String renderdashboard (HttpSession session, Model model){
        return "Employee/dashboard";
    }

    @PostMapping("/employee-info")
    public String updateInfo (UserDTO userDTO,Model model){
        System.out.println(userDTO);
        Result rs = userService.updateInfoEmployee(userDTO);
        model.addAttribute("rs",rs);
        return "Employee/employee-info";
    }
}
