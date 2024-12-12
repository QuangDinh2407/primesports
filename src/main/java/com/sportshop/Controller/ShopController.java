package com.sportshop.Controller;

import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.*;
import com.sportshop.Service.Iml.CartServicesIml;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import com.sportshop.Service.Iml.UserServiceIml;
import com.sportshop.Service.ProductService;
import com.sportshop.Service.UserService;
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

import java.util.List;
import java.util.UUID;

@Controller
public class ShopController {

    @Autowired
    ProductTypeServiceIml productTypeServiceIml;

    @Autowired
    ProductService productService;

    @Autowired
    private ProductServiceIml productServiceIml;

    @Autowired
    private UserServiceIml userServiceIml;

    @Autowired
    private CartServicesIml cartServicesIml;

//    @ModelAttribute
//    public void checkLoginToCreateCart(HttpSession session){
////        session.invalidate(); // Hủy toàn bộ session
//        String email = (String) session.getAttribute("email");
//        if (email == null) {
//            if(session.getAttribute("newCart")==null){
//                CartDTO newCart = new CartDTO();
////                newCart.setCart_id(UUID.randomUUID().toString());
//                System.out.println("new Cart là: "+newCart.getCart_id());
//                session.setAttribute("newCart", newCart);
//            }
//        }
//        else{
//            CartDTO newCart= (CartDTO) session.getAttribute("newCart");
//            UserDTO userDTO=userServiceIml.findbyEmail(email);
//            if(newCart!=null){
//                System.out.println(newCart.getCart_id());
//                userDTO.setCart(cartServicesIml.moveCart(userDTO.getCart(),newCart));
////                session.removeAttribute("quantityProduct");
////                session.removeAttribute("totalPrice");
////                session.removeAttribute("newCart");
//            }
//            session.setAttribute("userCart", userDTO.getCart());
//            for(CartDetailDTO x:userDTO.getCart().getCartDetailItems()){
//                System.out.println(x.getProduct().getName());
//            }
//            session.setAttribute("userInfo",userDTO);
//        }
//    }

    @ModelAttribute
    public void checkLoginToCreateCart(HttpSession session){
        String email = (String) session.getAttribute("email");
        if (email == null) {
            if(session.getAttribute("cartDTO")==null){
                CartDTO newCart = new CartDTO();
                session.setAttribute("cartDTO", newCart);
            }
        }
        else{
            CartDTO newCart= (CartDTO) session.getAttribute("cartDTO");
            UserDTO userDTO= (UserDTO) session.getAttribute("userInfo");
            if (userDTO == null){
                userDTO = userServiceIml.findbyEmail(email);
            }
            if (newCart == null){
                userDTO.getCart().setIsMerge(true);
            }
            else{
                if(!newCart.getIsMerge()){
                    userDTO.setCart(cartServicesIml.moveCart(userDTO.getCart(),newCart));
                }
            }
            session.setAttribute("cartDTO", userDTO.getCart());
            session.setAttribute("userInfo",userDTO);
        }
    }

    @ModelAttribute
    public void getSearchModal(Model model) {
        if (!model.containsAttribute("searchProduct")) {
            SearchProduct searchProduct = new SearchProduct();
            model.addAttribute("searchProduct", searchProduct);
        }
    }

    @GetMapping("")
    public String renderShop(Model model){
        List<ProductDTO> listPro = productService.findTop5Rating(0);
        model.addAttribute("listPro", listPro);
        return "homepage";
    }

//    @GetMapping("/header")
//    public String headerRender(HttpSession session,Model model, CartDTO cartDTO) {
//        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
//        CartDTO cart=(CartDTO) session.getAttribute("newCart");
//        model.addAttribute("newCart", cart);
//        return "templates/header1";
//    }

    @GetMapping("/header")
    public String headerRender(HttpSession session,Model model) {
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        CartDTO cart=(CartDTO) session.getAttribute("cartDTO");
        model.addAttribute("cartDTO", cart);
        return "templates/header1";
    }

    @GetMapping("/footer")
    public String footerRender() {

        return "templates/footer";
    }


    @GetMapping("/all-product")
    public String renderAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @Valid SearchProduct searchProduct,
            BindingResult bindingResult,
            Model model) {

        Pageable pageable = page > 0 ? PageRequest.of(page-1, size) : PageRequest.of(page, size) ;

        Page <ProductDTO> listPro = productService.getAll(searchProduct, pageable);

        model.addAttribute("listPro", listPro);
        model.addAttribute("size", size);
        model.addAttribute("listPro", listPro);
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        model.addAttribute("searchProduct", searchProduct);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
        }
        //System.out.println(listPro.getContent());
        return "all-product";
    }

//    @GetMapping("/product-detail/{id}")
//    public String renderDetailProduct(@PathVariable("id") String id, Model model,HttpSession session) {
//        ProductDTO proDTO= productServiceIml.findProductById(id);
//
//        //lấy 5 sản phẩm được rating cao
//        List<ProductDTO> relatedProducts=productServiceIml.findTop5Rating("available");
//
//        //xử lý voucher (Từ sản phẩm lấy được voucher -> lấy voucher giảm giá nhiều nhất)
//        model.addAttribute("newCart",(CartDTO) session.getAttribute("newCart"));
//        model.addAttribute("productDTO", proDTO);
//        model.addAttribute("relatedProducts", relatedProducts);
//        return "product-detail";
//    }

    @GetMapping("/product-detail/{id}")
    public String renderDetailProduct(@PathVariable("id") String id, Model model,HttpSession session) {
        ProductDTO proDTO= productServiceIml.findProductById(id);

        //lấy 5 sản phẩm được rating cao
        List<ProductDTO> relatedProducts=productServiceIml.findTop5Rating(0);

        //xử lý voucher (Từ sản phẩm lấy được voucher -> lấy voucher giảm giá nhiều nhất)
        model.addAttribute("cartDTO",(CartDTO) session.getAttribute("cartDTO"));
        model.addAttribute("productDTO", proDTO);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }

}
