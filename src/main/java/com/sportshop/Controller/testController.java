package com.sportshop.Controller;

import com.sportshop.Entity.ProductTypeDetailEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Modal.Mail;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.ShopVoucherDTO;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Repository.ShopVoucherDetailRepository;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.MailService;
import com.sportshop.Service.ShopVoucherService;
import com.sportshop.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
//@Controller
public class testController {

    @Autowired
    UserService employeeService;

    @Autowired
    AccountService accountService;

    @Autowired
    ShopVoucherService shopVoucherService;

    @Autowired
    MailService mailService;

    @Autowired
    ShopVoucherDetailRepository shopVoucherDetailRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @GetMapping("/test")
    public String test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password123"));
        System.out.println(encoder.encode("password456"));
        System.out.println(encoder.encode("password789"));
        return "test";
    }

    @GetMapping("/")
    public String defaultpage() {

        return "homepage";
    }

    @GetMapping("/repo")
    @ResponseBody
    public List<UserDTO> getAll()
    {
        List <UserDTO> rs = employeeService.findAll();
        return rs;
    }

    @GetMapping("/account")
    @ResponseBody
    public AccountDTO getAccount(@RequestParam ("username") String username)
    {
        return accountService.findAccountByUserName(username);
    }

    @GetMapping("/dashboard")
    public String render1()
    {
        return "Admin/dashboard";
    }

    @Autowired
    ServletContext context;

    @GetMapping("/welcome")
    public String render2()
    {
        System.out.println(context.getRealPath("/static"));
        return "welcome";
    }

    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\Assets\\image\\Employee";

    @GetMapping("/upload")
    public String render3()
    {
        System.out.println(UPLOAD_DIRECTORY);
        return "homepage";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/upload";
        }

        try {
            Path path = Paths.get(UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename());
            System.out.println(path);
            file.transferTo(new File(String.valueOf(path)));

            Thread.sleep(8000);

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            redirectAttributes.addFlashAttribute("path", "/Assets/image/Employee/" + file.getOriginalFilename());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/upload";
    }

    @GetMapping("/type")
    List<ProductTypeEntity> hehe (){
        List<ProductTypeEntity> hehe = productTypeRepository.findAll();
        hehe.forEach(ProductType ->{
            List<ProductTypeDetailEntity> a = ProductType.getProductTypeDetailItems();
            a.forEach(product ->{
                System.out.println(product.getProduct().getName());
            });
        });
        return productTypeRepository.findAll();
    }

    @GetMapping("/pro")
    List<ProductTypeEntity> haha (@RequestParam List<String> names){
        List<ProductTypeEntity> a = productTypeRepository.findByNameIn(names);
        a.forEach(pro ->{
            pro.getProductTypeDetailItems().forEach(prod ->{
                System.out.println(prod.getProduct().getName());
            });
        });
        return a;
    }


}
