package com.sportshop.Controller;


import com.sportshop.Converter.CartConverter;
import com.sportshop.Modal.ActionCart;
import com.sportshop.Modal.ProductSize;
import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.CartRepository;
import com.sportshop.Service.*;
import com.sportshop.Service.Iml.*;
import com.sportshop.Service.ProductService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

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

    @Autowired
    CartServicesIml cartServicesIml;
    @Autowired
    private ShopVoucherService shopVoucherService;
    @Autowired
    private CartConverter cartConverter;
    @Autowired
    private CartRepository cartRepository;

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
            CartDTO cartUpdate= (CartDTO) session.getAttribute("cartDTOUpdate");
            if (cartUpdate != null)
            {
                session.setAttribute("cartDTO", cartUpdate);
            }
            else {
                session.setAttribute("cartDTO", userDTO.getCart());
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

    @ModelAttribute
    public void getActionCartModal(Model model) {
        if (!model.containsAttribute("actionCart")) {
            ActionCart actionCart = new ActionCart();
            model.addAttribute("actionCart", actionCart);
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

        Result rs = (Result) model.asMap().get("rs");
        Pageable pageable = page > 0 ? PageRequest.of(page-1, size) : PageRequest.of(page, size) ;

        Page <ProductDTO> listPro = productService.getAll(searchProduct, pageable);
        model.addAttribute("listPro", listPro);
        model.addAttribute("size", size);
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
        model.addAttribute("searchProduct", searchProduct);
        model.addAttribute("rs",rs);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
        }
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
    public String renderDetailProduct(@PathVariable("id") String id, Model model,HttpSession session, HttpServletRequest request) {
        ProductDTO proDTO= productServiceIml.findProductById(id);
        Map<String, Integer> sizeQuantitiesFilter = proDTO.getSizeQuantities();
        sizeQuantitiesFilter.entrySet().removeIf(entry -> entry.getValue() == 0);
        proDTO.setSizeQuantities(sizeQuantitiesFilter);

        //lấy 5 sản phẩm được rating cao
        List<ProductDTO> relatedProducts=productServiceIml.findTop5Rating(0);

        //xử lý voucher (Từ sản phẩm lấy được voucher -> lấy voucher giảm giá nhiều nhất)
        model.addAttribute("cartDTO",(CartDTO) session.getAttribute("cartDTO"));
        model.addAttribute("productDTO", proDTO);
        model.addAttribute("relatedProducts", relatedProducts);
        String currentURL = request.getRequestURL().toString();
        session.setAttribute("currentURL", currentURL);

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

    @PostMapping("/checkoutAll")
    public String checkOutAll(@Valid UserOrderDTO userOrderDTOForm, BindingResult bindingResult, HttpSession session, Model model) {

        UserOrderDTO userOrderDTOSession = (UserOrderDTO) session.getAttribute("userOrderDTO");
        userOrderDTOSession.setPaymentType(userOrderDTOForm.getPaymentType());
        userOrderDTOSession.setShipping_address(userOrderDTOForm.getShipping_address());
        userOrderDTOSession.setShipping_name(userOrderDTOForm.getShipping_name());
        userOrderDTOSession.setShipping_phone(userOrderDTOForm.getShipping_phone());

        if (bindingResult.hasErrors()) {
            userOrderDTOForm.setUserEmail(userOrderDTOSession.getUserEmail());
            userOrderDTOForm.setTotal_price(userOrderDTOSession.getTotal_price());
            userOrderDTOForm.setUserOrderDetails(userOrderDTOSession.getUserOrderDetails());
            List <PaymentTypeDTO> listPayment = paymentTypeService.listPayment();
            model.addAttribute("listPayment",listPayment);
            model.addAttribute("userOrderDTO", userOrderDTOForm);
            // Trả về giao diện chứa form
            return "shipping-info";
        }
        model.addAttribute("userOrderDTO",userOrderDTOSession);
        return "checkoutAll";
    }

    @GetMapping("/shipping-info")
    public String headerCheckout(HttpSession session,Model model) {
        UserOrderDTO userOrderDTO = (UserOrderDTO) session.getAttribute("userOrderDTO");
        List <PaymentTypeDTO> listPayment = paymentTypeService.listPayment();
        model.addAttribute("userOrderDTO",userOrderDTO);
        model.addAttribute("listPayment",listPayment);
        return "shipping-info";
    }

    @PostMapping("/add-voucher")
    public String addVoucher(HttpSession session,Model model, @RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        UserOrderDTO userOrderDTO = (UserOrderDTO) session.getAttribute("userOrderDTO");
        Result rsVoucher = shopVoucherService.findByCodeAndProductId(code,userOrderDTO);
        redirectAttributes.addFlashAttribute("rsVoucher",rsVoucher);
        return "redirect:/shipping-info";
    }

    @PostMapping("/order-product")
    public String orderProduct(HttpSession session, Model model, RedirectAttributes redirectAttributes) throws Exception {
        UserOrderDTO userOrderDTOSession = (UserOrderDTO) session.getAttribute("userOrderDTO");
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
            List <ProductSize> productSizeList = userOrderService.createOrder(userOrderDTOSession,email);
            redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
            redirectAttributes.addFlashAttribute("rs", rs);
            return "redirect:/update-quantity";
        }
    }

    @GetMapping("/update-quantity")
    public String orderSummary(Model model,RedirectAttributes redirectAttributes,HttpSession session) {
        // Nhận dữ liệu từ redirectAttributes
        Result rs = (Result) model.asMap().get("rs");
        List<ProductSize> productSizeList = (List<ProductSize>) model.asMap().get("productSizeList");
        model.addAttribute("rs", rs);
        productSizeList.forEach(item ->{
            sizeDetailService.updateProductSize(item.getProductId(), item.getSizeId(), item.getAmount());
        });
        redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/update-cart";
    }

    @GetMapping("/update-cart")
    public String updateteCart(Model model,HttpSession session,RedirectAttributes redirectAttributes) {
        Result rs = (Result) model.asMap().get("rs");
        List<ProductSize> productSizeList = (List<ProductSize>) model.asMap().get("productSizeList");
        List<String> productIds = (List<String>) session.getAttribute("productIds");
        CartDTO cart=(CartDTO) session.getAttribute("cartDTO");
        if(productIds != null)
        {
            cartServicesIml.deleteItems(cart,productIds,session);
            session.removeAttribute("productIds");
        }
        CartDTO cartDTOnew = cartServicesIml.findCart(cart.getCart_id());
        cartDTOnew.setIsMerge(true);
        session.setAttribute("cartDTOUpdate", cartDTOnew);
        redirectAttributes.addFlashAttribute("productSizeList", productSizeList);
        redirectAttributes.addFlashAttribute("rs", rs);
        return "redirect:/all-product";
    }


    @GetMapping("/api/vnpay/return")
    public String handleReturn(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode ,Model model,HttpSession session,RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
        Result rs = new Result();
        UserOrderDTO userOrderDTOSession = (UserOrderDTO) session.getAttribute("userOrderDTO");
        if (vnp_ResponseCode.equals("00"))
        {
            rs.setSuccess(true);
            rs.setMessage("Đặt hàng thành công!");
            String email = (String) session.getAttribute("email");
            userOrderDTOSession.setUserEmail(email);
            List <ProductSize> productSizeList = userOrderService.createOrder(userOrderDTOSession,email);
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

    @GetMapping("/cart-detail/{cart_id}")
    public String renderDetailCart(@PathVariable("cart_id") String cart_id, Model model,HttpSession session) {
        CartDTO cartDTO=cartServicesIml.findCart(cart_id);
        String email = (String) session.getAttribute("email");
        
        model.addAttribute("cartDTO",cartDTO);
        CartDTO cartDTOnew = cartServicesIml.findCart(cart_id);
        cartDTOnew.setIsMerge(true);
        session.setAttribute("cartDTOUpdate", cartDTOnew);
        return "cart-detail";
    }


    @PostMapping("/action-cart")
    public String handlerAction(ActionCart actionCart, RedirectAttributes redirectAttributes,HttpSession session) {
        CartDTO cartDTO=cartServicesIml.findCart(actionCart.getCartId());
        if (actionCart.getAction().equals("delete"))
        {
            if (!actionCart.getProductId().isEmpty())
            {
                cartServicesIml.deleteItems(cartDTO,actionCart.getProductId(),session);

            }
            return "redirect:/cart-detail/"+ actionCart.getCartId();
        }
        else {
            redirectAttributes.addAttribute("product_id",actionCart.getProductId());
            redirectAttributes.addAttribute("size", actionCart.getSize());
            redirectAttributes.addAttribute("amount",actionCart.getAmount());
            session.setAttribute("productIds",actionCart.getProductId());
            return "redirect:/checkout";
        }

    }

}
