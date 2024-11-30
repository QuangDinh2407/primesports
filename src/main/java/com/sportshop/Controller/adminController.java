package com.sportshop.Controller;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.Entity.ShopVoucherEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.Custom.ProductTypeDetailRepository;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ShopVoucherDetailRepository;
import com.sportshop.Repository.ShopVoucherRepository;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import com.sportshop.Service.Iml.ShopVoucherDetailServiceIml;
import com.sportshop.Service.Iml.ShopVoucherServiceIml;
import com.sportshop.Service.ProductTypeService;
import com.sportshop.Service.ShopVoucherService;
import com.sportshop.Service.UserService;
import com.sportshop.Utils.StringToDateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    UserService userService;

    @Autowired
    ServletContext context;

    @Autowired
    ShopVoucherService shopVoucherService;

    @Autowired
    private ShopVoucherServiceIml shopVoucherServiceIml;

    @Autowired
    ProductTypeServiceIml productTypeServiceIml;

    @Autowired
    ShopVoucherRepository shopVoucherRepository;

    @Autowired
    ShopVoucherDetailRepository shopVoucherDetailRepository;

    @Autowired
    private ProductTypeDetailRepository productTypeDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopVoucherDetailServiceIml shopVoucherDetailServiceIml;

    @ModelAttribute
    public void getUser(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            UserDTO userDTO = userService.findbyEmail(email);
            model.addAttribute("userDTO", userDTO);
        }
    }

    @GetMapping("")
    public String render (HttpSession session, Model model){
        return "redirect:/admin/home";
    }

    @GetMapping("/admin-info")
    public String renderuserInfo (HttpSession session, Model model){
        return "Admin/admin-info";
    }

    @GetMapping("/home")
    public String renderHome (HttpSession session, Model model){
        return "Admin/home";
    }

    @GetMapping("/dashboard")
    public String renderdashboard (HttpSession session, Model model){
        return "Admin/dashboard";
    }

    @PostMapping("/admin-info")
    public String updateInfo (UserDTO userDTO,Model model, @RequestParam("avatar") MultipartFile file){
        System.out.println(userDTO);
        Result rs = userService.updateInfoUser(userDTO,file);
        model.addAttribute("rs",rs);
        return "Admin/admin-info";
    }

    @GetMapping("/manage-voucher")
    public String adminVoucher (Model model){
        shopVoucherServiceIml.updateExpiredVouchers();
        List<ShopVoucherDTO> listDTO= shopVoucherService.findAll();
        listDTO.sort((v1, v2) -> v2.getEnded_at().compareTo(v1.getEnded_at()));
        List<ProductTypeDTO> listProductTypeDTO=productTypeServiceIml.showAllProductTypes();
        model.addAttribute("vCListDTO",listDTO);
        model.addAttribute("listProductTypeDTO",listProductTypeDTO);

        return "Admin/manage-voucher";
    }

    @PostMapping("/manage-voucher/add")
    public String renderAddVoucher(ShopVoucherDTO shopVoucherDTO,
         RedirectAttributes redirectAttributes,
         @RequestParam("started_at_add") String started_at_add,
         @RequestParam("ended_at_add") String ended_at_add) {
        shopVoucherDTO.setStarted_at(StringToDateUtil.convertStringToDate(started_at_add));
        shopVoucherDTO.setEnded_at(StringToDateUtil.convertStringToDate(ended_at_add));
        Date now=new Date();
        shopVoucherDTO.setCreated_at(now);

        String rs = shopVoucherServiceIml.saveOrUpdateVoucher(shopVoucherDTO);

        return "redirect:/admin/manage-voucher";
    }

    @PostMapping("/manage-voucher/edit")
    public String renderEditVoucher(ShopVoucherDTO shopVoucherDTO,
            RedirectAttributes redirectAttributes, Model model,
        @RequestParam("voucher_id") String voucher_id,
        @RequestParam("started_at1") String started_at1,
        @RequestParam("ended_at1") String ended_at1,
        @RequestParam(value = "listTypeEdit[]", required = false) List<String> listSelectTypes) {
        shopVoucherDTO.setStarted_at(StringToDateUtil.convertStringToDate(started_at1));
        shopVoucherDTO.setEnded_at(StringToDateUtil.convertStringToDate(ended_at1));

        Date now=new Date();
        shopVoucherDTO.setUpdated_at(now);
        String rs = shopVoucherServiceIml.saveOrUpdateVoucher(shopVoucherDTO);

        List<String>existProductInVoucher=shopVoucherDetailServiceIml.convertProductToString(shopVoucherDetailRepository.findProductByShopVoucherId(voucher_id));
        List<ProductDTO> listPro = productTypeServiceIml.findProductsByTypeNames(listSelectTypes);
        model.addAttribute("listPro",listPro);
        model.addAttribute("existProductInVoucher",existProductInVoucher);
        model.addAttribute("voucher_id",voucher_id);
        return "Admin/manage-voucher-editdetail";
    }

    @PostMapping("/manage-voucher/delete")
    public String renderDelVoucher(ShopVoucherDTO shopVoucherDTO,
                                   @RequestParam("code") String code,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("Code to delete: " + code);
        String rs=shopVoucherServiceIml.deleteInfoVoucher(shopVoucherDTO);
        return "redirect:/admin/manage-voucher";
    }

    @PostMapping("/manage-voucher/add_product")
    public String addProductInVoucherDetail(
            @RequestParam("voucher_id") String voucher_id,
            @RequestParam(value = "selectedProducts[]", required = false) List<String> selectedProductIds,
            RedirectAttributes redirectAttributes) {

        ShopVoucherDetailDTO shopVoucherDetailDTO =new ShopVoucherDetailDTO();
        Date now=new Date();
        shopVoucherDetailDTO.setCreated_at(now);
        shopVoucherDetailDTO.setUpdated_at(now);

        if (selectedProductIds != null && !selectedProductIds.isEmpty()) {
            for (String productId : selectedProductIds) {
                System.out.println("Sản phẩm ID: " + productId);
                shopVoucherDetailServiceIml.saveOrUpdateVoucherDetail(shopVoucherDetailDTO,selectedProductIds,voucher_id);
            }
        } else {
            System.out.println("Không có sản phẩm nào được chọn.");
            //xóa những sản phẩm trong voucher id đó
            shopVoucherDetailServiceIml.deleteProductByVoucherId(voucher_id);
        }

        return "redirect:/admin/manage-voucher";
    }

}
