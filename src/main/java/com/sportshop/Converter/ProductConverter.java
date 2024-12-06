package com.sportshop.Converter;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.stereotype.Component;
import com.sportshop.Entity.ProductImageEntity;
import com.sportshop.Entity.SizeDetailEntity;
import com.sportshop.Entity.SizeEntity;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductImageDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductConverter {
    // Chuyển từ Entity sang DTO
    public ProductDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        Map<String, Integer> sizeQuantities = entity.getSizeDetailItems()
                .stream()
                .collect(Collectors.toMap(
                        sizeDetail -> sizeDetail.getSize().getName_size(),
                        SizeDetailEntity::getQuantity
                ));

        ProductImageDTO imageDTO = new ProductImageDTO();
        return ProductDTO.builder()
                .product_id(entity.getProduct_id())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .rating(entity.getRating())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .imagePaths(entity.getProductImageItems()
                        .stream()
                        .map(imageEntity -> imageEntity.getImage_path()) // Giả sử ProductImageEntity có getImagePath
                        .collect(Collectors.toList()))
                .productTypeIds(entity.getProductTypeDetailItems()
                        .stream()
                        .map(detail -> detail.getProductType().getProductType_id())
                        .collect(Collectors.toList()))// Giả sử ProductTypeDetailEntity liên kết với ProductTypeEntity
                .updated_at(entity.getUpdated_at())
                .sizeQuantities(sizeQuantities)
                .imagePaths(entity.getProductImageItems()
                        .stream()
                        .map(ProductImageEntity::getImage_path)
                        .collect(Collectors.toList()))
                .productTypeIds(entity.getProductTypeDetailItems()
                        .stream()
                        .map(detail -> detail.getProductType().getProductType_id())
                        .collect(Collectors.toList()))
                .build();
    }

    // Chuyển từ DTO sang Entity
    public ProductEntity toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        List<SizeDetailEntity> sizeDetailEntities = new ArrayList<>();

        if (dto.getSizeQuantities() != null) {
            sizeDetailEntities = dto.getSizeQuantities().entrySet().stream()
                    .map(entry -> SizeDetailEntity.builder()
                            .quantity(entry.getValue())
                            .size(SizeEntity.builder().name_size(entry.getKey()).build())
                            .build())
                    .toList();
        }

        return ProductEntity.builder()
                .product_id(dto.getProduct_id())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .rating(dto.getRating())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .updated_at(dto.getUpdated_at())
                .SizeDetailItems(sizeDetailEntities)
                .build();
    }
}
