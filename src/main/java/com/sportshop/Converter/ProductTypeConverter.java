package com.sportshop.Converter;

import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.Repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductTypeConverter {

    @Autowired
    ProductTypeRepository productTypeRepository;

    public ProductTypeDTO toDTO(ProductTypeEntity entity){
        if(entity == null){
            return null;
        }
        return ProductTypeDTO.builder()
                .productType_id(entity.getProductType_id())
                .name(entity.getName())
                .parent_id(entity.getParent_id())
                .build();
    }

    public List<ProductTypeDTO> toListHierarchyDTO(){

        List<ProductTypeEntity> allTypes = productTypeRepository.findAll();
        List<ProductTypeEntity> parentTypes = allTypes.stream()
                .filter(type -> type.getParent_id() == null)
                .collect(Collectors.toList());

        // Tạo danh sách DTO từ các parent
        List<ProductTypeDTO> productTypeDTOList = new ArrayList<>();

        for (ProductTypeEntity parent : parentTypes) {
            // Lấy danh sách tên của các con (child) liên quan đến parent hiện tại
            List<String> childNames = allTypes.stream()
                    .filter(type -> parent.getProductType_id().equals(type.getParent_id()))
                    .map(ProductTypeEntity::getName)
                    .collect(Collectors.toList());

            // Tạo DTO cho parent và gán danh sách tên con
            ProductTypeDTO dto = ProductTypeDTO.builder()
                    .productType_id(parent.getProductType_id())
                    .name(parent.getName())
                    .parent_id(parent.getParent_id())
                    .name_child(childNames)
                    .build();

            productTypeDTOList.add(dto);
        }

        return productTypeDTOList;
    }


}
