package com.sportshop.Service.Iml;

import com.sportshop.Converter.ProductConverter;
import com.sportshop.Converter.ProductTypeConverter;
import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.ProductTypeDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ProductTypeRepository;
import com.sportshop.Service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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

    @Autowired
    private ProductRepository productRepository;

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

    public Page<ProductDTO> findProductsByTypeNames(List<String> listSelectTypes, Pageable pageable) {
        // Kiểm tra xem listSelectTypes có null hoặc trống không, tránh lỗi khi gọi findByNameIn
        if (listSelectTypes == null || listSelectTypes.isEmpty()) {
            return Page.empty(pageable);  // Nếu không có loại sản phẩm nào được chọn, trả về một trang trống
        }

        // Lấy tất cả ProductTypeEntity từ cơ sở dữ liệu với điều kiện name nằm trong listSelectTypes
        Page<ProductTypeEntity> productTypesPage = productTypeRepository.findByNameIn(listSelectTypes, pageable);

        // Khởi tạo Set để tự động loại bỏ các phần tử trùng lặp
        Set<ProductEntity> productSet = new HashSet<>();

        // Duyệt qua tất cả các ProductTypeEntity và lấy sản phẩm liên quan
        productTypesPage.forEach(type -> {
            // Duyệt qua các ProductTypeDetailEntity và thêm sản phẩm vào Set
            type.getProductTypeDetailItems().forEach(detail -> {
                productSet.add(detail.getProduct()); // Set tự động loại bỏ sản phẩm trùng
            });
        });

        // Chuyển đổi Set thành List và map qua ProductConverter
        List<ProductDTO> productList = productSet.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());

        // Cung cấp phân trang cho danh sách sản phẩm
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productList.size());
        List<ProductDTO> pageContent = productList.subList(start, end);

        // Trả về đối tượng Page với nội dung phân trang
        return new PageImpl<ProductDTO>(pageContent, pageable, productList.size());
    }


    private ProductTypeDTO convertToDTO(ProductTypeEntity entity) {
        return ProductTypeDTO.builder()
                .productType_id(entity.getProductType_id())
                .name(entity.getName())
                .parent_id(entity.getParent_id())
                .build();
    }

}
