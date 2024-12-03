package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.ModalDTO.SizeDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.ProductService;
import com.sportshop.Service.ProductTypeService;
import com.sportshop.Service.SizeService;
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
    SizeService sizeService;
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
    public String renderProductManage(HttpSession session, Model model) {
        List<ProductDTO> products = productService.showProducts();
        List<ProductDTO> activeProducts = products.stream()
                .filter(product -> "ACTIVE".equals(product.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("products", activeProducts);


        List<ProductTypeDTO> productTypes = productTypeService.showAllProductTypes();
        model.addAttribute("productTypes", productTypes);

        List<ProductTypeDTO> productTypesParent = productTypes.stream()
                .filter(type -> type.getParent_id() == null)
                .toList();
        model.addAttribute("productTypesParent", productTypesParent);

        List<SizeDTO> sizes = sizeService.showSizesProduct();
        model.addAttribute("sizes", sizes);


        session.setAttribute("productTypes", productTypes);
        session.setAttribute("productTypesParent", productTypesParent);
        session.setAttribute("sizes", sizes);

        return "Admin/productManage";
    }

    @GetMapping("/product/view/{id}")
    public String viewProductDetails(@PathVariable("id") String productId, Model model) {
        System.out.println("------------------- Vô đây rồi  nè  ---------------");
        ProductDTO productDTO = productService.getProductById(productId);
        model.addAttribute("productDTO", productDTO);
        System.out.println(productDTO);
        List<ProductTypeDTO> productTypes = productTypeService.showAllProductTypes();
        model.addAttribute("productTypes", productTypes);

        List<ProductTypeDTO> productTypesParent = productTypes.stream()
                .filter(type -> type.getParent_id() == null)
                .toList();

        List<SizeDTO> sizes = sizeService.showSizesProduct();
        model.addAttribute("sizes", sizes);

        model.addAttribute("productTypesParent", productTypesParent);

        String title = "Chi tiết sản phẩm";
        model.addAttribute("title", title);
        model.addAttribute("formAction", "/admin/product/update-product");
        return "Admin/product";
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

        List<SizeDTO> sizes = sizeService.showSizesProduct();
        model.addAttribute("sizes", sizes);

        model.addAttribute("productTypesParent", productTypesParent);

        String title = "Thêm sản phẩm";
        model.addAttribute("title", title);

        model.addAttribute("formAction", "/admin/product/add-product");

        return "Admin/product";
    }

    @PostMapping("/product/add-product")
    public String saveProduct(@ModelAttribute ProductDTO productDTO, RedirectAttributes redirectAttributes,
                              @RequestParam("images") List <MultipartFile> files,
                              @RequestParam( required = false ) List <String> sizes,
                              @RequestParam( required = false ) List<String> quantities) {

        System.out.println("------------------- Vô đây rồi  nè  ---------------");
        System.out.println(productDTO);
        System.out.println(sizes);
        System.out.println(quantities);
        System.out.println("Received files: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File Name: " + file.getOriginalFilename() + ", Size: " + file.getSize());
        }
        // Gọi service để thêm sản phẩm
        Result rs = productService.addProduct(productDTO,files,sizes,quantities);
        // Thêm đối tượng Result vào redirectAttributes
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/admin/product/add-product";
    }

    @PostMapping("/product/update-product")
    public String updateProduct(@ModelAttribute ProductDTO productDTO, RedirectAttributes redirectAttributes,
                              @RequestParam("images") List <MultipartFile> files,
                              @RequestParam( required = false ) List <String> sizes,
                              @RequestParam( required = false ) List<String> quantities) {

        System.out.println(productDTO);
        System.out.println(sizes);
        System.out.println(quantities);
        System.out.println("Received files: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File Name: " + file.getOriginalFilename() + ", Size: " + file.getSize());
        }
        // Gọi service để thêm sản phẩm
        Result rs = productService.updateProduct(productDTO,files,sizes,quantities);
        // Thêm đối tượng Result vào redirectAttributes
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/admin/product/add-product";
    }

    @PostMapping("/product/delete")
    public String deleteProduct(@RequestParam("id") String productId,RedirectAttributes redirectAttributes) {
        Result rs = productService.deleteProduct(productId);
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/admin/product";
    }

    @PostMapping("/product/filter")
    public String filterProducts(@RequestParam(required = false) List<String> productTypes,
                                 @RequestParam(required = false) String beginPrice,
                                 @RequestParam(required = false) String endPrice,
                                 @RequestParam(required = false) String status,
                                 HttpSession session,  // Lấy session để truy xuất productTypes và productTypesParent
                                 Model model) {

        List<ProductTypeDTO> productTypesFromSession = (List<ProductTypeDTO>) session.getAttribute("productTypes");
        List<ProductTypeDTO> productTypesParentFromSession = (List<ProductTypeDTO>) session.getAttribute("productTypesParent");

        if (productTypesFromSession == null || productTypesParentFromSession == null) {
            productTypesFromSession = productTypeService.showAllProductTypes();
            productTypesParentFromSession = productTypesFromSession.stream()
                    .filter(type -> type.getParent_id() == null)
                    .toList();
        }

        model.addAttribute("productTypes", productTypesFromSession);
        model.addAttribute("productTypesParent", productTypesParentFromSession);

        List<ProductDTO> products = productService.filterProducts(productTypes, beginPrice, endPrice, status);
        model.addAttribute("products", products);

        return "Admin/productManage";
    }

}
