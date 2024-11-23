package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.AccountService;
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

    @Autowired
    AccountService accountService;

    @Autowired
    private HttpSession httpSession;

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

    @RequestMapping("")
    public String render() {
        return "/Customer/customer-info";
    }


    @PostMapping("/customer-info")
    public String updateInfo(UserDTO userDTO, Model model) {
        System.out.println(userDTO);
        Result rs = userService.updateInfoUser(userDTO);
        model.addAttribute("rs", rs);
        return "redirect:/customer";
    }

    @RequestMapping("/change-password-form")
    public String changePassword() {
        return "Customer/customer-change-password";
    }

    @RequestMapping("/order-history")
    public String orderHistory() {
        return "Customer/orders-history";
    }

    @PostMapping("/change-password-customer")
    public String changePassword(
            @ModelAttribute("oldPassword") String oldPassword,
            @ModelAttribute("newPassword") String newPassword,
            @ModelAttribute("confirmPassword") String confirmPassword,
            HttpSession session,
            Model model
    ) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            model.addAttribute("result", new Result(false, "Không tìm thấy email người dùng!"));
            return "Customer/customer-change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("result", new Result(false, "Mật khẩu mới và xác nhận mật khẩu không khớp!"));
            return "Customer/customer-change-password";
        }

        Result result = accountService.changePassword(email, oldPassword, newPassword);
        model.addAttribute("result", result);
        return "Customer/customer-change-password";
    }
}