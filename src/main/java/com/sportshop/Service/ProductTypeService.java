package com.sportshop.Service;

import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.ModalDTO.ProductTypeDTO;

import java.util.List;

public interface ProductTypeService {
    List<ProductTypeDTO> showAllProductTypes();
}
