package com.sportshop.Service;


import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Result addProduct(ProductDTO productDTO, List<MultipartFile> files );
}
