package com.sportshop.Service.Iml;

import com.sportshop.Converter.ProductConverter;
import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ProductTypeDetailEntity;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

import java.util.stream.Collectors;

@Service
public class ProductTypeServiceIml implements ProductTypeService {
    @Autowired
    ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductConverter productConverter;

    @Transactional
    @Override
    public List<ProductTypeDTO> showAllProductTypes(){
        List<ProductTypeEntity> productTypes = productTypeRepository.findAll();
        return productTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findProductsByTypeNames(List<String> listSelectTypes) {
        // Lấy tất cả ProductTypeEntity từ cơ sở dữ liệu
        List<ProductTypeEntity> productTypes = productTypeRepository.findByNameIn(listSelectTypes);

        // Khởi tạo Set để tự động loại bỏ các phần tử trùng lặp
        Set<ProductEntity> productSet = new HashSet<>();

        // Duyệt qua tất cả các ProductTypeEntity và lấy sản phẩm liên quan
        productTypes.forEach(type -> {
            // Duyệt qua các ProductTypeDetailEntity và thêm sản phẩm vào Set
            type.getProductTypeDetailItems().forEach(detail -> {
                productSet.add(detail.getProduct()); // Set tự động loại bỏ sản phẩm trùng
            });
        });

        // Chuyển đổi Set thành List và map qua ProductConverter để trả về DTO
        return productSet.stream()
                .map(productConverter::toDTO)
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
