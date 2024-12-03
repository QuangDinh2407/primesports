package com.sportshop.Service.Iml;

import com.sportshop.Converter.ProductTypeConverter;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public List<ProductTypeDTO> showAllProductTypes(){
        List<ProductTypeEntity> productTypes = productTypeRepository.findAll();
        return productTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductTypeDTO convertToDTO(ProductTypeEntity entity) {
        return ProductTypeDTO.builder()
                .productType_id(entity.getProductType_id())
                .name(entity.getName())
                .parent_id(entity.getParent_id())
                .build();
    }

}
