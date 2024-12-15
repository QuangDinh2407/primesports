package com.sportshop.Controller;

import com.sportshop.Converter.AccountConverter;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Converter.ProductTypeConverter;
import com.sportshop.Entity.*;
import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductImageEntity;
import com.sportshop.Modal.Mail;
import com.sportshop.Modal.ProductSize;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.*;
import com.sportshop.Service.*;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    ProductTypeConverter productTypeConverter;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    MailService mailService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountConverter accountConverter;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductTypeServiceIml productTypeServiceIml;
    @Autowired
    private ProductServiceIml productServiceIml;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    ProductConverter productConverter;

    @GetMapping("/test")
    public String test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password123"));
        System.out.println(encoder.encode("password456"));
        System.out.println(encoder.encode("password789"));
        return "test";
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
        return "/welcome";
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

    @GetMapping("/typepro")
    public List<ProductTypeDTO> getListHierarchyType() {
        List<ProductTypeDTO> listType = productTypeConverter.toListHierarchyDTO();
        return listType;
    }

    @GetMapping("/5pro")
    public List<ProductDTO>  hehe1() {
        List<ProductDTO> listType = productServiceIml.findTop5Rating(0);
        return listType;
    }
    @GetMapping("tst")
    public List<ProductEntity> renderTst (HttpSession session, Model model){
        List<ProductEntity> a = productRepository.findAll();
        return a;
    }

    @GetMapping("image")
    public List<ProductImageEntity> renderImage (HttpSession session, Model model){
        List<ProductImageEntity> a = productImageRepository.findAll();
        return a;
    }

    @GetMapping("/dash")
    public Map<String, Object> getTotalRevenueByMonth() {
        // Lấy dữ liệu doanh thu theo tháng từ repository
        List<Object[]> results = userOrderRepository.getTotalRevenueByMonth();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (Object[] result : results) {
            String label = "Tháng " + result[1];  // Tháng
            Double value = ((Number) result[2]).doubleValue();  // Doanh thu

            labels.add(label);
            values.add(value);
        }

        // Trả về một Map chứa cả labels và values
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("values", values);

        return chartData;
    }

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/api/vnpay/create-payment")
    public RedirectView createPayment(@RequestParam("amount") Float amount) throws Exception {
        String paymentUrl = vnPayService.createPaymentUrl(amount);
        return new RedirectView(paymentUrl);
    }

    @GetMapping("/prohe")
    public List<ProductDTO> prohe(@RequestParam("product_id") List<String> productIds) {
        System.out.println(productRepository.findByProductIds(productIds));
        return productRepository.findByProductIds(productIds).stream().map(productConverter::toDTO).collect(Collectors.toList());
    }

    @Autowired
    private SizeDetailRepository sizeDetailRepository;

    @Autowired
    SizeDetailService sizeDetailService;

    @GetMapping("/cuu")
    public String cuu(@RequestParam("product_id") String product_id,@RequestParam("size_id") String size_id,@RequestParam("amount") int amount)  {
        List <ProductSize> productSizeList = new ArrayList<>();
        ProductSize a = new ProductSize();
        a.setProductId(product_id);
        a.setSizeId(size_id);
        a.setAmount(amount);
        productSizeList.add(a);

        productSizeList.forEach(item ->{
            sizeDetailService.updateProductSize(item.getProductId(), item.getSizeId(), item.getAmount());
        });
        System.out.println(productSizeList);
        return "cuu";
    }

//    public void updateProductSize(String product_id, String size_id, int amount){
//        SizeDetailEntity sizeDetail = sizeDetailRepository.findByProductIdAndSizeId(product_id, size_id);
//        int updateAmount = sizeDetail.getQuantity();
//        sizeDetail.setQuantity(updateAmount- amount);
//        sizeDetailRepository.save(sizeDetail);
//    }

    @GetMapping("/cuu2")
    public String cuu2( @RequestParam(name = "type", defaultValue = "none") String type,
                        @RequestParam(name = "date", required = false) String date,
                        @RequestParam(name = "date_order", required = false) String dateOrder,
                        HttpSession session,
                        Model model)  {

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
                System.out.println("haha");
            }

        } catch (NumberFormatException e) {
            // Log lỗi nếu cần thiết
            e.printStackTrace();
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
        return "cuu";
    }

    @GetMapping("/cuu3")
    public String cuu3()  {
        List<Object[]> results = new ArrayList<>();
        results = userOrderRepository.getTotalRevenueByMonth();
//        Integer year = ((Integer) results.getFirst()[0]);
        System.out.println(results);
        System.out.println(results.getFirst()[0]);
        return "cuu";
    }
}
