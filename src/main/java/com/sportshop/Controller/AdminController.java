package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.*;
import com.sportshop.Service.Iml.AccountServiceIml;
import com.sportshop.Service.Iml.ProductServiceIml;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    UserOrderRepository userOrderRepository;
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
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String renderDashboard(
            @RequestParam(name = "type", defaultValue = "none") String type,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "date_order", required = false) String dateOrder,
            HttpSession session,
            Model model)
    {
        List<Object[]> results = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        List<Object[]> orderCountsByStatus = new ArrayList<>();
        List<String> labels_sales = new ArrayList<>();
        List<Integer> values_sales = new ArrayList<>();

        List<Object[]> topSellingProduct = new ArrayList<>();
        List<String> labels_product = new ArrayList<>();
        List<Integer> values_product = new ArrayList<>();

        int year = 0;
        Double revenueToday = userOrderRepository.getTotalRevenueToday();
        Double revenueTotal = userOrderRepository.getTotalRevenueOfShop();
        Double totalImportPrice = productRepository.getTotalImportPrice();
        Double profit = revenueTotal - totalImportPrice;

        try {
            if ("day".equals(type) && date != null && date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] parts = date.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);
                if (day >= 1 && day <= 31 && month >= 1 && month <= 12) {
                    results = userOrderRepository.getTotalRevenueByDay(day, month, year);
                }
            } else if ("month".equals(type) && date != null && date.matches("\\d{2}/\\d{4}")) {
                String[] parts = date.split("/");
                int month = Integer.parseInt(parts[0]);
                year = Integer.parseInt(parts[1]);
                if (month >= 1 && month <= 12) {
                    results = userOrderRepository.getTotalRevenueByMonth(month, year);
                }
            } else if ("year".equals(type) && date != null && date.matches("\\d{4}")) {
                year = Integer.parseInt(date);
                if (year > 0) {
                    results = userOrderRepository.getTotalRevenueByYear(year);
                }
            } else {
                results = userOrderRepository.getTotalRevenueByMonth();
                year = ((Number) results.getFirst()[0]).intValue();

            }
        } catch (NumberFormatException e) {
            // Log lỗi nếu cần thiết
        }

        for (Object[] result : results) {
            String label = "";
            if ("day".equals(type)) {
                label = "Ngày " + result[0] + " Tháng " + result[1] + " Năm " + result[2];
            } else if ("month".equals(type)) {
                label = "Tháng " + result[0] + " Năm " + result[1];
            } else if ("year".equals(type)) {
                label = "Tháng " + result[1];
            } else {
                label = "Tháng " + result[1];
            }
            Double value = ((Number) result[result.length - 1]).doubleValue();
            labels.add(label);
            values.add(value);
        }

        year = ((Number) results.getFirst()[0]).intValue();
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("values", values);

        System.out.println(dateOrder);
        if (dateOrder != null && dateOrder.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] parts = dateOrder.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
            if (day >= 1 && day <= 31 && month >= 1 && month <= 12) {
                orderCountsByStatus = userOrderRepository.getOrderCountByDay(day, month, year);
            }

        } else {
            orderCountsByStatus = userOrderRepository.getOrderCountByStatus();
        }

        for (Object[] result : orderCountsByStatus) {
            String status = (String) result[0];
            Integer count = ((Number) result[1]).intValue();
            labels_sales.add(status);
            values_sales.add(count);
        }

        Map<String, Object> orderStatusCounts = new HashMap<>();
        orderStatusCounts.put("labels_sales", labels_sales);
        orderStatusCounts.put("values_sales", values_sales);


        topSellingProduct = userOrderRepository.findTopSellingProducts();
        for (Object[] result : topSellingProduct) {
            String status = (String) result[1];
            Integer count = ((Number) result[2]).intValue();
            labels_product.add(status);
            values_product.add(count);
        }

        Map<String, Object> topSellingProducts = new HashMap<>();
        topSellingProducts.put("labels_product", labels_product);
        topSellingProducts.put("values_product", values_product);




        model.addAttribute("chartData", chartData);
        model.addAttribute("orderStatusCounts", orderStatusCounts);
        model.addAttribute("topSellingProducts", topSellingProducts);
        model.addAttribute("year", year);
        model.addAttribute("revenueToday", revenueToday);
        model.addAttribute("revenueTotal", revenueTotal);
        model.addAttribute("totalImportPrice", totalImportPrice);
        model.addAttribute("profit", profit);

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
        System.out.println(userDTO);
        Result rs = userService.updateInfoAdmin(userDTO,file);
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
        model.addAttribute("buttonText", "Cập nhật");
        model.addAttribute("formAction", "/admin/product/update-product");
        return "Admin/product";
    }

    @GetMapping("/order/view/{id}")
    public String viewOrderDetails(@PathVariable("id") String orderId, Model model) {
        System.out.println("------------------- View order  ---------------");
        UserOrderDTO userOrderDTO = userOrderService.getUserOrderById(orderId);
        model.addAttribute("userOrderDTO", userOrderDTO);
        UserDTO userDTO = userService.findbyEmail(userOrderDTO.getEmail());
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("orderId", orderId);
        return "Admin/order";
    }

    @GetMapping("/product/add-product")
    public String addProduct (HttpSession session, Model model){


        if (!model.containsAttribute("productDTO")) {
            ProductDTO productDTO = new ProductDTO();
            model.addAttribute("productDTO", productDTO);
        }

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

        model.addAttribute("buttonText", "Thêm");

        model.addAttribute("formAction", "/admin/product/add-product");

        return "Admin/product";
    }

    @PostMapping("/product/add-product")
    public String saveProduct(@ModelAttribute @Valid ProductDTO productDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                              @RequestParam("images") List <MultipartFile> files,
                              @RequestParam( required = false ) List <String> sizes,
                              @RequestParam( required = false ) List<String> quantities,Model model) {

        System.out.println("------------------- Vô đây rồi  nè  ---------------");
        System.out.println(productDTO);
        System.out.println(sizes);
        System.out.println(quantities);
        System.out.println("Received files: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File Name: " + file.getOriginalFilename() + ", Size: " + file.getSize());
        }

        boolean sizeValid = sizes != null && !sizes.isEmpty() && quantities != null && !quantities.isEmpty();
        if (!sizeValid) {
            if (productDTO.getQuantity() == null) {
                model.addAttribute("errorMessage", "Số lượng sản phẩm không được để trống.");
            }

        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            List<ProductTypeDTO> productTypes = productTypeService.showAllProductTypes();
            model.addAttribute("productTypes", productTypes);

            List<ProductTypeDTO> productTypesParent = productTypes.stream()
                    .filter(type -> type.getParent_id() == null)
                    .toList();

            List<SizeDTO> size = sizeService.showSizesProduct();
            model.addAttribute("sizes", size);

            model.addAttribute("productTypesParent", productTypesParent);

            String title = "Thêm sản phẩm";
            model.addAttribute("title", title);

            model.addAttribute("buttonText", "Thêm");

            model.addAttribute("formAction", "/admin/product/add-product");
            return "Admin/product";
        }
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
        return "redirect:/admin/product";
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

    @GetMapping("/orders")
    public String renderOrdersManage(HttpSession session, Model model) {
        List<UserOrderDTO> orders = userOrderService.getAllUserOrders();
        model.addAttribute("orders", orders);
        return "Admin/orderManage";
    }
}