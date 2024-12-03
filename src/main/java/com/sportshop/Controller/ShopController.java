package com.sportshop.Controller;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
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
import org.springframework.validation.FieldError;
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
    UserService userService;

//    @ModelAttribute
//    public void checkLoginToCreateCart(HttpSession session,Model model){
//        String email = (String) session.getAttribute("email");
//
//        if (email == null) {
//            if(!model.containsAttribute("newCart")){
//                CartDTO newCart = new CartDTO();
//                newCart.setCart_id(UUID.randomUUID().toString());
//                System.out.println(newCart);
//                model.addAttribute("newCart", newCart);
//            }
//        }
//    }

    @ModelAttribute
    public void checkLoginToCreateCart(HttpSession session){
        String email = (String) session.getAttribute("email");
        if (email == null) {
            if(session.getAttribute("newCart")==null){
                CartDTO newCart = new CartDTO();
                newCart.setCart_id(UUID.randomUUID().toString());
                System.out.println(newCart);
                session.setAttribute("newCart", newCart);
            }
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
        List<ProductDTO> listPro = productService.findTop5Rating("available");
        model.addAttribute("listPro", listPro);
        return "homepage";
    }

    @GetMapping("/header")
    public String headerRender(CartDTO cart,Model model) {
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        model.addAttribute("newCart", new CartDTO());
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

    @GetMapping("/product-detail/{id}")
    public String renderDetailProduct(@PathVariable("id") String id, Model model,HttpSession session) {
        ProductDTO proDTO= productServiceIml.findProductById(id);
        System.out.println(proDTO.getName());

        //lấy 5 sản phẩm được rating cao
        List<ProductDTO> relatedProducts=productServiceIml.findTop5Rating("available");

        //xử lý voucher (Từ sản phẩm lấy được voucher -> lấy voucher giảm giá nhiều nhất)
        model.addAttribute("newCart",(CartDTO) session.getAttribute("newCart"));
        model.addAttribute("productDTO", proDTO);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }

}
