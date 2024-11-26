package com.sportshop.Converter;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductConverter {

    // Chuyển từ Entity sang DTO
    public ProductDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductDTO.builder()
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .rating(entity.getRating())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .imagePaths(entity.getProductImageItems()
                        .stream()
                        .map(imageEntity -> imageEntity.getImage_path()) // Giả sử ProductImageEntity có `getImagePath`
                        .collect(Collectors.toList()))
                .productTypeIds(entity.getProductTypeDetailItems()
                        .stream()
                        .map(detail -> detail.getProductType().getProductType_id()) // Giả sử ProductTypeDetailEntity liên kết với ProductTypeEntity
                        .collect(Collectors.toList()))
                .build();
    }

    // Chuyển từ DTO sang Entity
    public ProductEntity toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return ProductEntity.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .rating(dto.getRating())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }
}
