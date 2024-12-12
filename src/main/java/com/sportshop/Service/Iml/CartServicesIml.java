package com.sportshop.Service.Iml;

import com.sportshop.Converter.CartConverter;
import com.sportshop.Converter.CartDetailConverter;
import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Repository.CartRepository;
import com.sportshop.Service.CartService;
import com.sportshop.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServicesIml implements CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartDetailConverter cartDetailConverter;

    @Autowired
    private CartRepository cartRepository;

    public CartDTO addProductToCart(HttpSession session, String productId, Integer quantity) {
        // Lấy giỏ hàng từ session, nếu chưa có thì khởi tạo
        CartDTO cartDTO = (CartDTO) session.getAttribute("cartDTO");


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
        System.out.println(existingCartDetail);
        Double total = cartDTO.getTotalPrice();
        Integer quantityProduct = cartDTO.getQuantityProduct();

        if (existingCartDetail != null) {
            // Tăng số lượng sản phẩm đã có
            existingCartDetail.setAmount(existingCartDetail.getAmount() + quantity);
            total += existingCartDetail.getProduct().getPrice() * quantity;
        } else {
            // Thêm sản phẩm mới
            CartDetailDTO newCartDetail = new CartDetailDTO();
            newCartDetail.setCartdetail_id(UUID.randomUUID().toString());
            newCartDetail.setProduct(product);
            newCartDetail.setAmount(quantity);
            newCartDetail.setCart(cartDTO);
            cartDTO.getCartDetailItems().add(newCartDetail);
            total += newCartDetail.getProduct().getPrice() * newCartDetail.getAmount();

        }
        quantityProduct += quantity;
        cartDTO.setTotalPrice(total);
        cartDTO.setQuantityProduct(quantityProduct);
        // Cập nhật session
        session.setAttribute("cartDTO", cartDTO);

        // Lưu xuống DB nếu đăng nhập rồi
        String email = (String) session.getAttribute("email");
        if (email != null) {
            saveOrUpdateCart(cartDTO);
        }
        return cartDTO;
    }

    public CartDTO moveCart(CartDTO userCart, CartDTO newCart) {
            if (newCart.getCartDetailItems() != null && !newCart.getCartDetailItems().isEmpty()) {
                for (CartDetailDTO newItem : newCart.getCartDetailItems()) {
                    boolean found = false;
                    // Duyệt qua danh sách các sản phẩm trong userCart
                    for (CartDetailDTO userItem : userCart.getCartDetailItems()) {
                        if (userItem.getProduct().getProduct_id().equals(newItem.getProduct().getProduct_id())) {
                            userItem.setAmount(userItem.getAmount() + newItem.getAmount());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        userCart.getCartDetailItems().add(newItem);
                    }
                    Double total = userCart.getTotalPrice();
                    Integer quantityProduct = userCart.getQuantityProduct();
                    total +=  newItem.getProduct().getPrice() * newItem.getAmount();
                    quantityProduct += newItem.getAmount();
                    userCart.setTotalPrice(total);
                    userCart.setQuantityProduct(quantityProduct);
                }
            }
        userCart.setIsMerge(true);
        saveOrUpdateCart(userCart);
        return userCart;
    }

    public String saveOrUpdateCart(CartDTO cartDTO){

        CartEntity cartEntity=cartRepository.findById(cartDTO.getCart_id());
        cartEntity.setCartDetailItems(cartDTO.getCartDetailItems().stream().map(cartDetailDTO -> cartDetailConverter.toEntity(cartDetailDTO,cartEntity)).collect(Collectors.toList()));
        cartRepository.save(cartEntity);
        return "Cập nhật thành công";
    }
}
