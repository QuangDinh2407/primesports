package com.sportshop.Controller;

import com.sportshop.Converter.AccountConverter;
import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.RoleEntity;
import com.sportshop.Modal.Mail;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.RoleRepository;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.MailService;
import com.sportshop.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    RoleRepository roleRepository;

    @Autowired
    MailService mailService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountConverter accountConverter;

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


    @GetMapping("/findall")
    public Page<AccountDTO> getAllCustomer(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "status", required = false) String status) {
        Pageable pageable = PageRequest.of(page, size);
        return accountService.getAllCustomer(pageable, search, status);
    }


    @GetMapping("/hehe")
    public RoleEntity render11(AccountDTO accountDTO)
    {
        return roleRepository.findByName("CUSTOMER");
    }
}
