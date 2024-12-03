package com.sportshop.Converter;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.SizeEntity;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.ModalDTO.SizeDTO;
import org.springframework.stereotype.Component;

@Component
public class SizeConverter {

    public SizeDTO toDTO(SizeEntity entity) {
        if (entity == null) {
            return null;
        }
        SizeDTO dto = new SizeDTO();

        return SizeDTO.builder()
                .size_id(entity.getSize_id())
                .name_size(entity.getName_size())
                .build();

    }

    public SizeEntity toEntity(SizeDTO dto) {
        if (dto == null) {
            return null;
        }
        return SizeEntity.builder()
                .size_id(dto.getSize_id())
                .name_size(dto.getName_size())
                .build();
    }

}
