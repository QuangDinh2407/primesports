package com.sportshop.Controller;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Modal.Checkout;
import com.sportshop.Modal.ProductSize;
import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.*;
import com.sportshop.Service.Iml.ProductServiceIml;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import com.sportshop.Service.Iml.SizeDetailServiceIml;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
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
    UserOrderService userOrderService;

    @Autowired
    PaymentTypeService paymentTypeService;

    @Autowired
    VNPayService vnPayService;

    @Autowired
    SizeDetailService sizeDetailService;

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
    private CartServicesIml cartServicesIml;

    @ModelAttribute
    public void checkLoginToCreateCart(HttpSession session){
//        session.invalidate(); // Hủy toàn bộ session
        String email = (String) session.getAttribute("email");
        if (email == null) {
            if(session.getAttribute("newCart")==null){
                CartDTO newCart = new CartDTO();
//                newCart.setCart_id(UUID.randomUUID().toString());
                System.out.println("new Cart là: "+newCart.getCart_id());
                session.setAttribute("newCart", newCart);
            }
        }
        else{
            CartDTO newCart= (CartDTO) session.getAttribute("newCart");
            UserDTO userDTO=userServiceIml.findbyEmail(email);
            if(newCart!=null){
                System.out.println(newCart.getCart_id());
                userDTO.setCart(cartServicesIml.moveCart(userDTO.getCart(),newCart));
//                session.removeAttribute("quantityProduct");
//                session.removeAttribute("totalPrice");
//                session.removeAttribute("newCart");
            }
            session.setAttribute("userCart", userDTO.getCart());
            for(CartDetailDTO x:userDTO.getCart().getCartDetailItems()){
                System.out.println(x.getProduct().getName());
            }
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

    @GetMapping("/header")
    public String headerRender(HttpSession session,Model model, CartDTO cartDTO) {
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        CartDTO cart=(CartDTO) session.getAttribute("newCart");
        model.addAttribute("newCart", cart);
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

        Result rs = (Result) model.asMap().get("rs");
        Pageable pageable = page > 0 ? PageRequest.of(page-1, size) : PageRequest.of(page, size) ;

        Page <ProductDTO> listPro = productService.getAll(searchProduct, pageable);

        model.addAttribute("listPro", listPro);
        model.addAttribute("size", size);
        model.addAttribute("listPro", listPro);
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        model.addAttribute("searchProduct", searchProduct);
        model.addAttribute("rs",rs);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
        }
        //System.out.println(listPro.getContent());
        return "all-product";
    }

    @GetMapping("/product-detail/{id}")
    public String renderDetailProduct(@PathVariable("id") String id, Model model,HttpSession session) {
        ProductDTO proDTO= productServiceIml.findProductById(id);

        //lấy 5 sản phẩm được rating cao
        List<ProductDTO> relatedProducts=productServiceIml.findTop5Rating(0);

        //xử lý voucher (Từ sản phẩm lấy được voucher -> lấy voucher giảm giá nhiều nhất)
        model.addAttribute("newCart",(CartDTO) session.getAttribute("newCart"));
        model.addAttribute("productDTO", proDTO);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }


    @GetMapping("/checkout")
    public String headerCheckout(@RequestParam("product_id") List<String> productIds,
                                 @RequestParam("size") List<String> sizes,
                                 @RequestParam("amount") List<Integer> amounts,
                                 HttpSession session, Model model) {

        UserOrderDTO userOrderDTO = userOrderService.checkoutProduct(productIds,sizes, amounts);
        session.setAttribute("userOrderDTO",userOrderDTO);
        model.addAttribute("userOrderDTO",userOrderDTO);
        return "checkout";
    }

    @GetMapping("/shipping-info")
    public String headerCheckout(HttpSession session,Model model) {
        UserOrderDTO userOrderDTO = (UserOrderDTO) session.getAttribute("userOrderDTO");
        List <PaymentTypeDTO> listPayment = paymentTypeService.listPayment();
        model.addAttribute("userOrderDTO",userOrderDTO);
        model.addAttribute("listPayment",listPayment);
        return "shipping-info";
    }

    @PostMapping("/order-product")
    public String orderProduct(UserOrderDTO userOrderDTOForm, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws Exception {
        UserOrderDTO userOrderDTOSession = (UserOrderDTO) session.getAttribute("userOrderDTO");
        userOrderDTOSession.setPaymentType(userOrderDTOForm.getPaymentType());
        userOrderDTOSession.setShipping_address(userOrderDTOForm.getShipping_address());
        userOrderDTOSession.setShipping_name(userOrderDTOForm.getShipping_name());
        userOrderDTOSession.setShipping_phone(userOrderDTOForm.getShipping_phone());
        if(userOrderDTOSession.getPaymentType().getName().equals("Chuyển khoản ngân hàng"))
        {
            String paymentUrl = vnPayService.createPaymentUrl(userOrderDTOSession.getTotal_price() + 30000);
            return "redirect:" + paymentUrl;
        }
        else{
            Result rs = new Result();
            rs.setSuccess(true);
            rs.setMessage("Đặt hàng thành công!");
            String email = (String) session.getAttribute("email");
            userOrderDTOSession.setUserEmail(email);
            List <ProductSize> productSizeList = userOrderService.createOrder(userOrderDTOSession);
            redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
            redirectAttributes.addFlashAttribute("rs", rs);
            return "redirect:/update-quantity";
        }
    }

    @GetMapping("/update-quantity")
    public String orderSummary(Model model,RedirectAttributes redirectAttributes) {
        // Nhận dữ liệu từ redirectAttributes
        Result rs = (Result) model.asMap().get("rs");
        List<ProductSize> productSizeList = (List<ProductSize>) model.asMap().get("productSizeList");
        model.addAttribute("rs", rs);
        productSizeList.forEach(item ->{
            sizeDetailService.updateProductSize(item.getProductId(), item.getSizeId(), item.getAmount());
        });
        redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/all-product";
    }

    @GetMapping("/api/vnpay/return")
    public String handleReturn(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode ,Model model,HttpSession session,RedirectAttributes redirectAttributes) {
        Result rs = new Result();
        UserOrderDTO userOrderDTOSession = (UserOrderDTO) session.getAttribute("userOrderDTO");
        if (vnp_ResponseCode.equals("00"))
        {
            rs.setSuccess(true);
            rs.setMessage("Đặt hàng thành công!");
            String email = (String) session.getAttribute("email");
            userOrderDTOSession.setUserEmail(email);
            List <ProductSize> productSizeList = userOrderService.createOrder(userOrderDTOSession);
            redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
            redirectAttributes.addFlashAttribute("rs", rs);
            return "redirect:/update-quantity";
        }
        else{
            rs.setSuccess(false);
            rs.setMessage("Thanh toán không thành công!");
            List <PaymentTypeDTO> listPayment = paymentTypeService.listPayment();
            model.addAttribute("userOrderDTO",userOrderDTOSession);
            model.addAttribute("listPayment",listPayment);
            model.addAttribute("rs",rs);
            return "shipping-info";
        }

    }
}
