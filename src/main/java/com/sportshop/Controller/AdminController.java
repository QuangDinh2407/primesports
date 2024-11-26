package com.sportshop.Controller;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import com.sportshop.Service.ProductService;
import com.sportshop.Service.ProductTypeService;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    private ProductServiceIml productServiceIml;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductTypeService productTypeService;

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
        Result rs = userService.updateInfoUser(userDTO);
        model.addAttribute("rs",rs);
        return "Admin/admin-info";
    }

    @GetMapping("/product")
    public String renderProductManage (HttpSession session, Model model){
        List<ProductDTO> products = productService.showProducts();
        for (ProductDTO productDTO : products) {
            System.out.println(productDTO.getImagePaths());
        }
        model.addAttribute("products",products);
        return "Admin/productManage";
    }

    @GetMapping("/product/add-product")
    public String addProduct (HttpSession session, Model model){
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);

        List<ProductTypeDTO> productTypes = productTypeService.showAllProductTypes();
        model.addAttribute("productTypes", productTypes);

        List<ProductTypeDTO> productTypesParent = productTypes.stream()
                .filter(type -> type.getParent_id() == null)
                .toList();

        model.addAttribute("productTypesParent", productTypesParent);

        return "Admin/addProduct";
    }

    @PostMapping("/product/add-product")
    public String saveProduct(@ModelAttribute ProductDTO productDTO,
                              RedirectAttributes redirectAttributes, @RequestParam("images") List <MultipartFile> files) {
        System.out.println("------------------- Vô đây rồi  nè  ---------------");
        System.out.println(productDTO);
        System.out.println("Received files: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File Name: " + file.getOriginalFilename() + ", Size: " + file.getSize());
        }
        // Gọi service để thêm sản phẩm
        Result rs = productService.addProduct(productDTO,files);
        // Thêm đối tượng Result vào redirectAttributes
        redirectAttributes.addFlashAttribute("rs", rs);
        // Chuyển hướng về trang GET "/admin/product/add-product"
        return "redirect:/admin/product/add-product";
    }


}
