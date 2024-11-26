package com.sportshop.Service.Iml;

import com.sportshop.Contants.StringConstant;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Entity.*;
import com.sportshop.Repository.*;
import com.sportshop.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceIml implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductTypeDetailRepository productTypeDetailRepository;

    // Đường dẫn lưu file ảnh (cấu hình trong application.properties)
    private final String uploadDir = StringConstant.PRODUCTIMAGE_URL;
    @Autowired
    private ProductConverter productConverter;

    @Transactional
    @Override
    public Result addProduct(ProductDTO productDTO, List<MultipartFile> files) {
        try {
            // 1. Lưu thông tin sản phẩm vào ProductEntity
            ProductEntity product = new ProductEntity();
            product.setName(productDTO.getName());
            product.setQuantity(productDTO.getQuantity());
            product.setPrice(productDTO.getPrice());
            product.setRating(0.0f); // Mặc định ban đầu
            product.setDescription(productDTO.getDescription());
            product.setStatus(productDTO.getStatus());
            product.setUpdated_at(new Date());
            product.setDeleted_at(null);

            product = productRepository.save(product); // Lưu sản phẩm vào database

//             2. Xử lý và lưu danh sách hình ảnh
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        // Tạo tên file duy nhất
                        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        Path filePath = Paths.get(uploadDir + File.separator+ filename);
                        System.out.println(filePath);

                        // Lưu file vào thư mục
                        Files.createDirectories(filePath.getParent()); // Tạo thư mục nếu chưa tồn tại
                        Files.write(filePath, file.getBytes());

                        // Lưu thông tin hình ảnh vào ProductImageEntity
                        ProductImageEntity image = new ProductImageEntity();
                        image.setImage_path(filename);
                        image.setProduct(product);
                        productImageRepository.save(image);
                    } else {
                        System.out.println("File trống, bỏ qua.");
                    }
                }
            }

            // 3. Lưu thông tin loại sản phẩm vào ProductTypeDetailEntity
            List<String> productTypeIds = productDTO.getProductTypeIds();
            if (productTypeIds != null && !productTypeIds.isEmpty()) {
                for (String typeId : productTypeIds) {
                    if(!Objects.equals(typeId, "")) {
                        ProductTypeDetailEntity typeDetail = new ProductTypeDetailEntity();
                        ProductTypeEntity productType = new ProductTypeEntity(); // Chỉ cần ID
                        productType.setProductType_id(typeId);
                        typeDetail.setProduct(product);
                        typeDetail.setProductType(productType);
                        productTypeDetailRepository.save(typeDetail);
                    }
                }
            }

            return new Result(true, "Thêm sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Thêm sản phẩm không thành công: " + e.getMessage());
        }
    }

    @Override
    public List<ProductDTO> showProducts(){
        List<ProductEntity> productEntityList = productRepository.findAll();
        return productEntityList.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }
}
