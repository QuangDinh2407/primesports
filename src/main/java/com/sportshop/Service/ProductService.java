package com.sportshop.Service;


import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Result addProduct(ProductDTO productDTO, List<MultipartFile> files );

    List<ProductDTO> findTop5Rating(String status);

    Page<ProductDTO> getAll(SearchProduct searchProduct, Pageable pageable);

    ProductDTO findProductById(String id);

    Result addProduct(ProductDTO productDTO, List<MultipartFile> files, List <String> sizes, List<String> quantities );

    Result deleteProduct(String productId);

    Result updateProduct(ProductDTO productDTO, List<MultipartFile> files, List<String> sizes, List<String> quantities);

    List<ProductDTO> showProducts();

    List<ProductDTO> filterProducts(List<String> productTypes,String beginPrice,String endPrice, String status);
    
    ProductDTO getProductById(String productId);
}
