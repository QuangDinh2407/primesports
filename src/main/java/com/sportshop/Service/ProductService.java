package com.sportshop.Service;


import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Result addProduct(ProductDTO productDTO, List<MultipartFile> files );

    List<ProductDTO> findTop5Rating(String status);

    Page<ProductDTO> getAll(SearchProduct searchProduct, Pageable pageable);

}
