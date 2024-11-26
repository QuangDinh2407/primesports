package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.Iml.AccountServiceIml;
import com.sportshop.Service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ServletContext context;
    @Autowired
    private AccountServiceIml accountServiceIml;

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

    @GetMapping("/manage-customer")
    public String renderAccount(HttpSession session, Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                @RequestParam(value = "status", required = false, defaultValue = "all") String status) {

        Pageable pageable = page > 0 ? PageRequest.of(page-1, pageSize) : PageRequest.of(page, pageSize) ;
        Page<AccountDTO> accountPage = accountServiceIml.getAllCustomer(pageable, search, status);

        model.addAttribute("accountPage", accountPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        return "Admin/manage-customer";
    }

    @GetMapping("/manage-customer/edit-{email}")
    public String renderEditAccount(@PathVariable("email") String email, Model model) {
        if (!model.containsAttribute("accountDTO")) {
            AccountDTO accountDTO = accountServiceIml.findAccountByUserName(email);
            model.addAttribute("accountDTO", accountDTO);
        }
        return "Admin/account-edit";
    }

    @GetMapping("/manage-customer/delete-{email}")
    public String renderDeleteAccount(@PathVariable("email") String email, RedirectAttributes redirectAttribute, HttpServletRequest request) {
        Result rs = accountServiceIml.deleteByEmail(email);
        redirectAttribute.addFlashAttribute("rs", rs);
        System.out.println(rs.getMessage());
        return "redirect:/admin/manage-customer";
    }

    @GetMapping("/manage-customer/add")
    public String renderaddAccount (Model modal){
        if (!modal.containsAttribute("accountDTO")) {
            modal.addAttribute("accountDTO", new AccountDTO());
        }
        return "Admin/account-add";
    }


    @PostMapping("/admin-info")
    public String updateInfo (UserDTO userDTO,Model model, @RequestParam("avatar") MultipartFile file){
        Result rs = userService.updateInfoUser(userDTO,file);
        model.addAttribute("rs",rs);
        return "Admin/admin-info";
    }

    @PostMapping("/manage-customer/edit")
    public String renderEditAccount1(@Valid AccountDTO accountDTO , BindingResult bindingResult, @RequestParam("avatar") MultipartFile file,
                                    RedirectAttributes redirectAttribute ){

        System.out.println(accountDTO);
        if (bindingResult.hasErrors()) {
            redirectAttribute.addFlashAttribute("org.springframework.validation.BindingResult.accountDTO", bindingResult);
            redirectAttribute.addFlashAttribute("accountDTO", accountDTO);
            return "redirect:/admin/manage-customer/edit-" + accountDTO.getEmail();
        }
        Result rs = accountServiceIml.updateAccountCustomer(accountDTO,file);
        redirectAttribute.addFlashAttribute("rs",rs);
        return "redirect:/admin/manage-customer";
    }

    @PostMapping("/manage-customer/add")
    public String addAccount (@Valid AccountDTO accountDTO, BindingResult bindingResult, @RequestParam("avatar") MultipartFile file,
                              Model model, RedirectAttributes redirectAttribute){

        if(accountDTO.getPassword().isEmpty()){
            bindingResult.rejectValue("password", "accountDTO", "Vui lòng nhập mật khẩu!");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountDTO", accountDTO);
            return "Admin/account-add";
        }
        Result rs = accountServiceIml.addAccountCustomer(accountDTO,file);
        redirectAttribute.addFlashAttribute("rs", rs);
        return "redirect:/admin/manage-customer";
    }

}