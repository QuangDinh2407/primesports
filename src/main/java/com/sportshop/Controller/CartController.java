package com.sportshop.Controller;

import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductServiceIml productServiceIml;

//    @GetMapping("/add-pro-to-cart")
//    public String AddProductToCart(CartDTO cartDTO, ProductDTO productDTO) {
//        System.out.println(productDTO.getProduct_id());
//        System.out.println(cartDTO.getCart_id());
//        productDTO = productServiceIml.findProductById(productDTO.getProduct_id());
//        CartDetailDTO tempCartDetailDTO = new CartDetailDTO();
//        boolean flag = true;//khởi tạo chưa có sản phẩm
//        for (CartDetailDTO a : cartDTO.getCartDetailItems()) {
//            if (productDTO.getProduct_id().equals(a.getProduct().getProduct_id())) {
//                a.setAmount(a.getAmount() + 1);
//                tempCartDetailDTO = a;
//                flag = false;//tìm thấy sản phẩm trong giỏ hàng thì +1
//                break;
//            }
//        }
//        //nếu chưa có sản phẩm thì tạo mới và thêm vào giỏ hàng
//        if (flag) {
//            CartDetailDTO a = new CartDetailDTO();
//            a.setProduct(productDTO);
//            a.setAmount(1);
//            a.setCart(cartDTO);
//            cartDTO.getCartDetailItems().add(a); // Thêm sản phẩm vào giỏ hàng
//        }
//        if (!cartDTO.getCartDetailItems().isEmpty()) {
//            int i = 1;
//            for (CartDetailDTO a : cartDTO.getCartDetailItems()) {
//                System.out.println("------");
//                System.out.println("Sản phẩm thứ " + i + " trong giỏ hàng:" + a.getProduct().getProduct_id());
//                System.out.println("Số lượng: " + a.getAmount());
//            }
//        }
//        return "redirect:product-detail/" + productDTO.getProduct_id();
//    }

    @GetMapping("/add-pro-to-cart")
    public String addProductToCart(HttpSession session, ProductDTO productDTO) {
        // Lấy giỏ hàng từ session, nếu chưa có thì khởi tạo
        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
        if (cartDTO == null) {
            cartDTO = new CartDTO();
            cartDTO.setCartDetailItems(new ArrayList<>());
            session.setAttribute("cart", cartDTO);
        }

        // Kiểm tra và lấy thông tin sản phẩm từ service
        ProductDTO product = productServiceIml.findProductById(productDTO.getProduct_id());
        if (product == null) {
            System.out.println("Sản phẩm không tồn tại!");
            return "redirect:/error"; // Chuyển hướng tới trang lỗi nếu sản phẩm không tồn tại
        }

        System.out.println("Product ID: " + product.getProduct_id());
        System.out.println("Cart ID: " + cartDTO.getCart_id());

        // Kiểm tra sản phẩm đã có trong giỏ hàng hay chưa
        CartDetailDTO existingCartDetail = null;
        for (CartDetailDTO item : cartDTO.getCartDetailItems()) {
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())) {
                existingCartDetail = item;
                break;
            }
        }

        if (existingCartDetail != null) {
            // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng
            existingCartDetail.setAmount(existingCartDetail.getAmount() + 1);
            System.out.println("Tăng số lượng sản phẩm: " + existingCartDetail.getProduct().getProduct_id() +
                    ", Số lượng mới: " + existingCartDetail.getAmount());
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới và thêm vào
            CartDetailDTO newCartDetail = new CartDetailDTO();
            newCartDetail.setProduct(product);
            newCartDetail.setAmount(1);
            newCartDetail.setCart(cartDTO);
            cartDTO.getCartDetailItems().add(newCartDetail);
            System.out.println("Thêm sản phẩm mới vào giỏ hàng: " + product.getProduct_id());
        }

        // Log sản phẩm trong giỏ hàng để kiểm tra
        if (!cartDTO.getCartDetailItems().isEmpty()) {
            int i = 1;
            for (CartDetailDTO a : cartDTO.getCartDetailItems()) {
                System.out.println("------");
                System.out.println("Sản phẩm thứ " + i + " trong giỏ hàng: " + a.getProduct().getProduct_id());
                System.out.println("Số lượng: " + a.getAmount());
                i++;
            }
        }

        // Cập nhật lại session
        session.setAttribute("cart", cartDTO);

        // Chuyển hướng tới trang chi tiết sản phẩm
        return "redirect:product-detail/" + product.getProduct_id();
    }


}
