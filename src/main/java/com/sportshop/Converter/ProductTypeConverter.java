package com.sportshop.Converter;

import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.ModalDTO.ProductTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeConverter {

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

    public ProductTypeEntity toEntity(ProductTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        return ProductTypeEntity.builder()
                .productType_id(dto.getProductType_id())
                .name(dto.getName())
                .parent_id(dto.getParent_id())
                .build();
    }

}
