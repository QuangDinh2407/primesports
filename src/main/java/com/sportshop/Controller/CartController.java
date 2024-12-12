package com.sportshop.Controller;

import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Service.CartService;
import com.sportshop.Service.Iml.CartServicesIml;
import com.sportshop.Service.Iml.ProductServiceIml;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class CartController {

    @Autowired
    private ProductServiceIml productServiceIml;

    @Autowired
    private CartServicesIml cartServiceIml;

    @GetMapping("/add-pro-to-cart")
    public String addProductToCart(Model model, HttpSession session, @RequestParam("product_id") String productId, @RequestParam("amount") Integer quantity) {
        try {
            cartServiceIml.addProductToCart(session, productId, quantity);
        } catch (IllegalArgumentException e) {
            return "redirect:/error"; // Nếu sản phẩm không tồn tại, chuyển tới trang lỗi
        }

        // Chuyển hướng tới trang chi tiết sản phẩm
        return "redirect:product-detail/" + productId;
    }

}
