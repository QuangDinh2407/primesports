package com.sportshop.Controller;

import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.*;
import com.sportshop.Service.*;
import com.sportshop.Service.Iml.*;
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
import java.time.LocalDate;
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

    @Autowired
    private ShopVoucherDetailServiceIml shopVoucherDetailServiceIml;

    @Autowired
    private ShopVoucherServiceIml shopVoucherServiceIml;

    @Autowired
    private ShopVoucherService shopVoucherService;

    @Autowired
    private ProductTypeServiceIml productTypeServiceIml;

    @Autowired
    private ShopVoucherDetailRepository shopVoucherDetailRepository;
    @Autowired
    private ShopVoucherRepository shopVoucherRepository;

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
        Double revenueToday = userOrderRepository.getTotalRevenueToday() != null ? userOrderRepository.getTotalRevenueToday() : 0.0;
        Double revenueTotal = userOrderRepository.getTotalRevenueOfShop() != null ? userOrderRepository.getTotalRevenueOfShop() : 0.0;
        Double totalImportPrice = productRepository.getTotalImportPrice() != null ? productRepository.getTotalImportPrice() : 0.0;

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

        if (!results.isEmpty()) {
            Object[] firstResult = results.getFirst();
            if (firstResult != null && firstResult[0] != null) {
                year = ((Number) firstResult[0]).intValue();
            } else {
                year = LocalDate.now().getYear();
            }
        } else {
            year = LocalDate.now().getYear();
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("values", values);

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
        Result rs = userService.updateInfoAdmin(userDTO,file);
        model.addAttribute("rs",rs);
        return "Admin/admin-info";
    }

    @PostMapping("/manage-customer/edit")
    public String renderEditAccount1(@Valid AccountDTO accountDTO , BindingResult bindingResult, @RequestParam("avatar") MultipartFile file,
                                    RedirectAttributes redirectAttribute ){

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

        System.out.println("------------------- Vô đây rồi  nè 1 ---------------");
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
            System.out.println(bindingResult);
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
    public String renderOrdersManage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     HttpSession session, Model model) {
        Page<UserOrderDTO> orders = userOrderService.getAllUserOrdersPagination(page, pageSize);
        model.addAttribute("orders", orders.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", orders.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", pageSize);
        return "Admin/orderManage";
    }

    @GetMapping("/manage-voucher")
    public String adminVoucher (Model model){
        shopVoucherServiceIml.updateExpiredVouchers();
        List<ShopVoucherDTO> listDTO= shopVoucherService.findAll();
        listDTO.sort((v1, v2) -> v2.getEnded_at().compareTo(v1.getEnded_at()));
        List<ProductTypeDTO> listProductTypeDTO=productTypeServiceIml.showAllProductTypes();
        model.addAttribute("vCListDTO",listDTO);
        model.addAttribute("listProductTypeDTO",listProductTypeDTO);
        model.addAttribute("shopVoucherDTO",new ShopVoucherDTO());
        model.addAttribute("shopVoucherDTOEdit",new ShopVoucherDTO());
        return "Admin/manage-voucher";
    }

    @PostMapping("/manage-voucher/add")
    public String renderAddVoucher(@Valid ShopVoucherDTO shopVoucherDTO,  BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            List<ShopVoucherDTO> listDTO= shopVoucherService.findAll();
            List<ProductTypeDTO> listProductTypeDTO=productTypeServiceIml.showAllProductTypes();

            String errorExisted="";
            if(shopVoucherRepository.findByCode(shopVoucherDTO.getCode())!=null){
                errorExisted="Code dã tồn tại!";
            }

            model.addAttribute("errorExisted",errorExisted);
            model.addAttribute("shopVoucherDTO",shopVoucherDTO);
            model.addAttribute("vCListDTO",listDTO);
            model.addAttribute("listProductTypeDTO",listProductTypeDTO);
            return "Admin/manage-voucher";
        }else{
            Date now=new Date();
            shopVoucherDTO.setCreated_at(now);
            String rs = shopVoucherServiceIml.saveOrUpdateVoucher(shopVoucherDTO);
        }
        return "redirect:/admin/manage-voucher";
    }

    @GetMapping("/manage-voucher/edit")
    public String renderEditVoucher1(ShopVoucherDTO shopVoucherDTO,
                                    RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam(value="voucher_id", required = false) String voucher_id,
                                    @RequestParam(value = "listTypeEdit", required = false) List<String> listSelectTypes,
                                     @RequestParam(value = "finalListProduct", required = false) List<String> finalListProduct,
                                     @RequestParam(defaultValue = "0") int page) {
        SearchProduct searchProduct = new SearchProduct();
        searchProduct.setTypes(listSelectTypes);
        Pageable pageable = page > 0 ? PageRequest.of(page-1, 10) : PageRequest.of(page, 10) ;

        Page <ProductDTO> listPro = productService.getAll(searchProduct, pageable);
        System.out.println("final: "+finalListProduct);
        // Thêm các dữ liệu vào model
        model.addAttribute("productPage", listPro); // Truyền Page để Thymeleaf hiển thị phân trang
        model.addAttribute("pageSize", 10);
        model.addAttribute("existProductInVoucher", finalListProduct);
        model.addAttribute("voucher_id", voucher_id);
        model.addAttribute("listSelectTypes", listSelectTypes); // Truyền listTypeEdit để Thymeleaf giữ thông tin lọc
        model.addAttribute("finalListProduct", finalListProduct);
        return "Admin/manage-voucher-edit-detail";
    }

    @GetMapping("/manage-voucher/edit-page")
    public String renderEditPageVoucher(@RequestParam("voucher_id") String voucherId, Model model) {
        System.out.println(voucherId);
        // Lấy thông tin voucher từ cơ sở dữ liệu dựa vào voucherId
        ShopVoucherDTO shopVoucherDTO = shopVoucherServiceIml.findByIdVoucher(voucherId);
        List<ProductTypeDTO> listProductTypeDTO=productTypeServiceIml.showAllProductTypes();

        // Truyền thông tin vào model để hiển thị trong form chỉnh sửa
        model.addAttribute("voucher_id",voucherId);
        model.addAttribute("shopVoucherDTO", shopVoucherDTO);
        model.addAttribute("listProductTypeDTO",listProductTypeDTO);
        // Trả về trang chỉnh sửa voucher
        return "Admin/manage-voucher-edit";
    }


    @PostMapping("/manage-voucher/edit")
    public String renderEditVoucher(@Valid ShopVoucherDTO shopVoucherDTO,BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam(value="voucher_id", required = false) String voucher_id,
                                    @RequestParam(value = "listTypeEdit[]", required = false) List<String> listSelectTypes,
                                    @RequestParam(defaultValue = "0") int page) {
        if(bindingResult.hasErrors()){
            System.out.println("Binding errors: " + bindingResult.getAllErrors());
            List<ShopVoucherDTO> listDTO= shopVoucherService.findAll();
            List<ProductTypeDTO> listProductTypeDTO=productTypeServiceIml.showAllProductTypes();

            model.addAttribute("voucher_id", voucher_id);
            model.addAttribute("shopVoucherDTO",shopVoucherDTO);
            model.addAttribute("vCListDTO",listDTO);
            model.addAttribute("listProductTypeDTO",listProductTypeDTO);
            return "Admin/manage-voucher-edit";
        }
        SearchProduct searchProduct = new SearchProduct();
        searchProduct.setTypes(listSelectTypes);
        Pageable pageable = page > 0 ? PageRequest.of(page-1, 10) : PageRequest.of(page, 10) ;

        Page <ProductDTO> listPro = productService.getAll(searchProduct, pageable);

        // Lấy các sản phẩm đã có trong voucher
        List<String> existProductInVoucher = shopVoucherDetailServiceIml.getProductIdsByVoucher(voucher_id);

        shopVoucherServiceIml.saveOrUpdateVoucher(shopVoucherDTO);
//        System.out.println("exist:"+existProductInVoucher);
        // Thêm các dữ liệu vào model
        model.addAttribute("productPage", listPro); // Truyền Page để Thymeleaf hiển thị phân trang
        model.addAttribute("pageSize", 10);
        model.addAttribute("existProductInVoucher", existProductInVoucher);
        model.addAttribute("voucher_id", voucher_id);
        model.addAttribute("listSelectTypes", listSelectTypes); // Truyền listTypeEdit để Thymeleaf giữ thông tin lọc
        model.addAttribute("finalListProduct", existProductInVoucher);
        return "Admin/manage-voucher-edit-detail";
    }



    @PostMapping("/manage-voucher/delete")
    public String renderDelVoucher(ShopVoucherDTO shopVoucherDTO,
                                   @RequestParam("shopVoucher_id") String shopVoucher_id,
                                   RedirectAttributes redirectAttributes) {
        String rs=shopVoucherServiceIml.deleteInfoVoucher(shopVoucherDTO);
        return "redirect:/admin/manage-voucher";
    }

    @PostMapping("/manage-voucher/add_product")
    public String addProductInVoucherDetail(
            @RequestParam("voucher_id") String voucher_id,
            @RequestParam(value = "finalListProduct", required = false) List<String> selectedProductIds,
            RedirectAttributes redirectAttributes) {

        ShopVoucherDetailDTO shopVoucherDetailDTO =new ShopVoucherDetailDTO();
        Date now=new Date();
        shopVoucherDetailDTO.setCreated_at(now);
        shopVoucherDetailDTO.setUpdated_at(now);
        System.out.println("add: "+selectedProductIds);
        shopVoucherDetailServiceIml.deleteProductByVoucherId(voucher_id);
        if (selectedProductIds != null && !selectedProductIds.isEmpty()) {
            for (String productId : selectedProductIds) {
                shopVoucherDetailServiceIml.saveOrUpdateVoucherDetail(shopVoucherDetailDTO,selectedProductIds,voucher_id);
            }
        }
        return "redirect:/admin/manage-voucher";
    }
}