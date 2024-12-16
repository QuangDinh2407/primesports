package com.sportshop.Controller;

import com.sportshop.Converter.ProductReviewConverter;
import com.sportshop.Entity.ProductReviewEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.UserOrderDTO;
import com.sportshop.ModalDTO.UserOrderDetailDTO;
import com.sportshop.Service.AccountService;
import com.sportshop.Service.ProductReviewService;
import com.sportshop.Service.UserOrderService;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class customerController {

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ProductReviewConverter productReviewConverter;

    public String userInfo_id;

    @ModelAttribute
    public void getUser(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            UserDTO userDTO = userService.findbyEmail(email);
            model.addAttribute("userDTO", userDTO);
            userInfo_id = userDTO.getUser_id();
        }
    }

    @RequestMapping("")
    public String render() {
        return "/Customer/customer-info";
    }


    @PostMapping("/customer-info")
    public String updateInfo(UserDTO userDTO, Model model,@RequestParam("avatar") MultipartFile file) {

        Result rs = userService.updateInfoUser(userDTO,file);
        model.addAttribute("rs", rs);
        return "/Customer/customer-info";
    }

    @RequestMapping("/change-password-form")
    public String changePassword() {
        return "Customer/customer-change-password";
    }
//        System.out.println(userInfo_id);

    @RequestMapping("/order-history")
    public String viewOrderHistory(Model model) {
        List<UserOrderDTO> userOrders = userOrderService.findAllOrdersByUserId(userInfo_id);
        // Thêm vào model
        model.addAttribute("userOrders", userOrders);// Thêm danh sách UserOrderDTO vào model
        return "Customer/orders-history";  // Trả về view 'order-history'
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
        model.addAttribute("rs", result);
        return "Customer/customer-change-password";
    }

    @RequestMapping("/order-detail")
    public String OrderDetails(
            @RequestParam(value = "orderId", required = false) String orderId,
            Model model) {
            List<UserOrderDTO> userOrders = userOrderService.findAllOrdersByUserId(userInfo_id);
//        model.addAttribute("orders", userOrders);
            model.addAttribute("userOrders", userOrders);  // Thêm danh sách UserOrderDTO vào model
        if (orderId != null) {
            UserOrderDTO orderDetails = userOrderService.getUserOrderById(orderId);
            model.addAttribute("orderDetails", orderDetails);
        }
        Map<String, Boolean> reviewStatus = new HashMap<>();

        // Kiểm tra trạng thái đánh giá cho từng sản phẩm trong đơn hàng
        for (UserOrderDTO order : userOrders) {
            for (UserOrderDetailDTO detail : order.getUserOrderDetails()) {
                boolean hasReviewed = productReviewService.hasReviewed(detail.getProduct().getProduct_id(), userInfo_id);
                reviewStatus.put(detail.getProduct().getProduct_id(), hasReviewed);
            }
        }
        System.out.println(userOrders);
        // Thêm vào model
        model.addAttribute("reviewStatus", reviewStatus);
        // Thêm orderId vào model để có thể dùng trong form đánh giá
        model.addAttribute("orderId", orderId);
        return "Customer/orders-history-detail";

    }

    @RequestMapping("/product-review")
    public String saveProductReview(
            @RequestParam(value = "orderId", required = false) String orderId,
            String comment, Float rating, String productId, String userId
            ) {

        // Chuyển đổi và lưu đánh giá
        ProductReviewEntity review = productReviewConverter.toEntity(comment, rating, productId, userId);
        productReviewService.save(review);

        // Sau khi lưu đánh giá, chuyển hướng về chi tiết đơn hàng
        return "redirect:/customer/order-detail?orderId=" + orderId; // Chuyển hướng về chi tiết đơn hàng
    }

    @GetMapping("/cancel-order/{orderId}")
    public String cancelOrder(@PathVariable("orderId") String orderId, RedirectAttributes redirectAttributes)
    {
        Result rs = userOrderService.cancelOrder(orderId);
        redirectAttributes.addFlashAttribute("rs",rs);
        return "redirect:/customer/order-history" ;
    }


}