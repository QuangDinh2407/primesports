package com.sportshop.Service.Iml;

import com.sportshop.Converter.ProductTypeConverter;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceIml implements ProductTypeService {

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeConverter productTypeConverter;

    @Override
    public List<ProductTypeDTO> getListHierarchyType() {
        return productTypeConverter.toListHierarchyDTO();
    }
}
