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
@RequestMapping("/admin")
public class adminController {

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
        return "redirect:/admin/home";
    }

    @GetMapping("/admin-info")
    public String renderuserInfo (HttpSession session, Model model){
        return "Admin/admin-info";
    }

    @GetMapping("/home")
    public String renderHome (HttpSession session, Model model){

        return "Admin/home";
    }

    @GetMapping("/dashboard")
    public String renderdashboard (HttpSession session, Model model){
        return "Admin/dashboard";
    }

    @PostMapping("/admin-info")
    public String updateInfo (UserDTO userDTO,Model model){
        System.out.println(userDTO);
        Result rs = userService.updateInfoAdmin(userDTO);
        model.addAttribute("rs",rs);
        return "Admin/admin-info";
    }

    @PostMapping("/admin-voucher")
    public String adminVoucher (UserDTO userDTO,Model model){
        System.out.println(userDTO);
        Result rs = userService.updateInfoAdmin(userDTO);
        model.addAttribute("rs",rs);
        return "Admin/admin-voucher";
    }

}
