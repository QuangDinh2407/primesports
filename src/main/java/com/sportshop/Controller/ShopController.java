package com.sportshop.Controller;

import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.Iml.ProductTypeServiceIml;
import com.sportshop.Service.ProductService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {

    @Autowired
    ProductTypeServiceIml productTypeServiceIml;

    @Autowired
    ProductService productService;

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
    public String headerRender(Model model) {
        model.addAttribute("listType",productTypeServiceIml.getListHierarchyType());
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
        return "all-product";
    }


}
