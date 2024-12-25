package com.sportshop.Service.Iml;

import com.sportshop.Converter.CartConverter;
import com.sportshop.Converter.CartDetailConverter;
import com.sportshop.Converter.UserInfoConverter;
import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.CartDetailRepository;
import com.sportshop.Repository.CartRepository;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.CartService;
import com.sportshop.Service.ProductService;
import com.sportshop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoConverter userInfoConverter;
    @Autowired
    UserService userService;
    @Autowired
    private CartConverter cartConverter;
    @Autowired
    CartDetailRepository cartDetailRepository;

    @Override
    public CartDTO addProductToCart(HttpSession session, String productId, Integer quantity, String size) {
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
            if (item.getProduct().getProduct_id().equals(product.getProduct_id()) && item.getSize().equals(size)) {
                existingCartDetail = item;
                break;
            }
        }
        Double total = cartDTO.getTotalPrice();
        Integer quantityProduct = cartDTO.getQuantityProduct();

        if (existingCartDetail != null) {
            // Tăng số lượng sản phẩm đã có
            existingCartDetail.setAmount(existingCartDetail.getAmount() + quantity);
            existingCartDetail.setSize(size);
            total += existingCartDetail.getProduct().getPrice() * quantity;
        } else {
            // Thêm sản phẩm mới
            CartDetailDTO newCartDetail = new CartDetailDTO();
            newCartDetail.setCartdetail_id(UUID.randomUUID().toString());
            newCartDetail.setProduct(product);
            newCartDetail.setAmount(quantity);
            newCartDetail.setCart(cartDTO);
            newCartDetail.setSize(size);
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

    @Override
    public CartDTO getAllItem(String email) {
        UserDTO userDTO = userService.findbyEmail(email);
        return userDTO.getCart();
    }

    @Override
    public CartDTO moveCart(CartDTO userCart, CartDTO newCart) {
            if (newCart.getCartDetailItems() != null && !newCart.getCartDetailItems().isEmpty()) {
                for (CartDetailDTO newItem : newCart.getCartDetailItems()) {
                    boolean found = false;
                    // Duyệt qua danh sách các sản phẩm trong userCart
                    for (CartDetailDTO userItem : userCart.getCartDetailItems()) {
                        if (userItem.getProduct().getProduct_id().equals(newItem.getProduct().getProduct_id()) && userItem.getSize().equals(newItem.getSize())) {
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

    private void deleteOld(CartDTO cartDTO){
        cartDetailRepository.deleteByCartId(cartDTO.getCart_id());
    }


    @Override
    public String saveOrUpdateCart(CartDTO cartDTO){
        deleteOld(cartDTO);
        CartEntity cartEntity=cartRepository.findByCartId(cartDTO.getCart_id());
        cartEntity.setCartDetailItems(cartDTO.getCartDetailItems().stream().map(cartDetailDTO -> cartDetailConverter.toEntity(cartDetailDTO,cartEntity)).collect(Collectors.toList()));
        System.out.println(cartEntity.getCartDetailItems());
        cartRepository.save(cartEntity);
        return "Cập nhật thành công";
    }

    @Override
    public CartDTO findCart(String cart_id) {
        CartEntity cartEntity = cartRepository.findByCartId(cart_id);
        return cartConverter.toDTO(cartEntity);  // Chuyển đổi cartEntity thành CartDTO
    }

    @Override
    public void deleteItems(CartDTO cartDTO, List<String> productId, HttpSession session) {
        for (CartDetailDTO cartDetailDTO : cartDTO.getCartDetailItems())
        {
            for (String product : productId)
            {
                if (product.equals(cartDetailDTO.getProduct().getProduct_id()))
                {
                    CartDetailEntity cartDetailEntity = cartDetailRepository.findByCartDetailId(cartDetailDTO.getCartdetail_id());
                    cartDetailRepository.deleteByCartDetailId(cartDetailDTO.getCartdetail_id());
                }
            }
        }
    }

    @Override
    public List<String> getAllCartId() {
        return cartRepository.findAllCartIds();
    }

}
