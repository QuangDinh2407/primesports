package com.sportshop.Service.Iml;

import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Service.CartService;
import com.sportshop.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class CartServicesIml implements CartService {

    @Autowired
    private ProductService productService;

    public CartDTO addProductToCart(HttpSession session, String productId, Integer quantity) {
        // Lấy giỏ hàng từ session, nếu chưa có thì khởi tạo
        CartDTO cartDTO = (CartDTO) session.getAttribute("newCart");
        if (cartDTO == null) {
            cartDTO = new CartDTO();
            cartDTO.setCartDetailItems(new ArrayList<>());
            session.setAttribute("newCart", cartDTO);
            System.out.println("new Cart other là: "+cartDTO.getCart_id());
        }

        // Lấy thông tin sản phẩm
        ProductDTO product = productService.findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại!");
        }

        // Kiểm tra sản phẩm đã có trong giỏ hàng hay chưa
        CartDetailDTO existingCartDetail = null;
        for (CartDetailDTO item : cartDTO.getCartDetailItems()) {
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())) {
                existingCartDetail = item;
                break;
            }
        }

        if (existingCartDetail != null) {
            // Tăng số lượng sản phẩm đã có
            existingCartDetail.setAmount(existingCartDetail.getAmount() + quantity);
        } else {
            // Thêm sản phẩm mới
            CartDetailDTO newCartDetail = new CartDetailDTO();
            newCartDetail.setProduct(product);
            newCartDetail.setAmount(quantity);
            newCartDetail.setCart(cartDTO);
            cartDTO.getCartDetailItems().add(newCartDetail);
        }

        double total = 0.0;
        int quantityProduct=0;
        for (CartDetailDTO a : cartDTO.getCartDetailItems()) {
            total += a.getProduct().getPrice() * a.getAmount(); // Giả sử mỗi sản phẩm có số lượng
            quantityProduct+=a.getAmount();
        }
        System.out.println(total);

        // Cập nhật session
        session.setAttribute("quantityProduct", quantityProduct);
        session.setAttribute("totalPrice", Math.round(total * 10.0) / 10.0);
        session.setAttribute("newCart", cartDTO);

        return cartDTO;
    }

    public String saveOrUpdateCart(CartDTO cartDTO){
        CartDTO cart = new CartDTO();
        if(cart.getCart_id()==null){
            saveOrUpdateCart(cart);
            return "Tạo mới thành công";
        }else{
            cart.setCart_id(cart.getCart_id());
            saveOrUpdateCart(cart);
            return "Cập nhật thành công";
        }
    }
}
