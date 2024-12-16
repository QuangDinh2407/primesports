package com.sportshop.Service.Iml;

import com.sportshop.Contants.StringConstant;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Modal.Result;
import com.sportshop.Modal.SearchProduct;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Entity.*;
import com.sportshop.Repository.*;
import com.sportshop.Service.CloudinaryService;
import com.sportshop.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductTypeDetailRepository productTypeDetailRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    private SizeDetailRepository sizeDetailRepository;

    // Đường dẫn lưu file ảnh (cấu hình trong application.properties)
    private final String uploadDir = StringConstant.PRODUCTIMAGE_URL;

    @Autowired
    private SizeRepository sizeRepository;

    @Transactional
    @Override
    public Result addProduct(ProductDTO productDTO, List<MultipartFile> files, List <String> sizes, List<String> quantities) {
        try {
            // 1. Lưu thông tin sản phẩm vào ProductEntity
            ProductEntity product = new ProductEntity();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            if (sizes != null && quantities != null) {
                int totalQuantity = 0;
                for (String quantity : quantities) {
                    totalQuantity += Integer.parseInt(quantity);
                }
                product.setQuantity(totalQuantity);
            } else {
                product.setQuantity(productDTO.getQuantity());
            }

            product.setRating(0.0f); // Mặc định ban đầu
            product.setDescription(productDTO.getDescription());
            product.setStatus(productDTO.getStatus());
            product.setUpdated_at(new Date());
            product.setDeleted_at(null);

            product = productRepository.save(product); // Lưu sản phẩm vào database
//            2. Lưu thông tin size và quantity

            if (sizes != null && quantities != null && sizes.size() == quantities.size()) {
                for (int i = 0; i < sizes.size(); i++) {
                    String sizeName = sizes.get(i);
                    int quantity = Integer.parseInt(quantities.get(i)); // Chuyển đổi từ String sang int

                    // Kiểm tra xem SizeEntity đã tồn tại chưa
                    SizeEntity sizeEntity = sizeRepository.findById(sizeName).orElse(null);
                    if (sizeEntity == null) {
                        // Nếu không tồn tại, tạo mới SizeEntity
                        sizeEntity = new SizeEntity();
                        sizeEntity.setSize_id(sizeName);
                        sizeEntity.setName_size(sizeName);  // Giả sử sizeName là tên kích thước
                        sizeEntity = sizeRepository.save(sizeEntity); // Lưu vào database
                    }

                    // Lưu size và quantity vào SizeDetailEntity
                    SizeDetailEntity sizeDetailEntity = new SizeDetailEntity();
                    sizeDetailEntity.setProduct(product);
                    sizeDetailEntity.setSize(sizeEntity);
                    sizeDetailEntity.setQuantity(quantity);

                    // Lưu vào cơ sở dữ liệu
                    sizeDetailRepository.save(sizeDetailEntity);
                }
            }

//            3. Xử lý và lưu danh sách hình ảnh
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        ProductImageEntity image = new ProductImageEntity();
                        try {
                            String imagePath = cloudinaryService.uploadFileToFolder(file, "customer");
                            image.setImage_path(imagePath);
                            image.setProduct(product);
                            productImageRepository.save(image);
                        } catch (IOException e) {
                            throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
                        }

                    } else {
                        System.out.println("File trống, bỏ qua.");
                    }
                }
            }

            // 4. Lưu thông tin loại sản phẩm vào ProductTypeDetailEntity
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
    public Result addProduct(ProductDTO productDTO, List<MultipartFile> files) {
        return null;
    }

    @Override
    public List<ProductDTO> findTop5Rating(int quantity) {
        List<ProductEntity> listPro = productRepository.findTop5ByQuantityGreaterThanOrderByRatingDesc(quantity);
        return listPro.stream().map(productConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page <ProductDTO> getAll(SearchProduct searchProduct, Pageable pageable) {
        List<String> types = (searchProduct.getTypes() != null && searchProduct.getTypes().isEmpty()) ? null : searchProduct.getTypes();
        Page<ProductEntity> productPage = productRepository.searchProducts(
                searchProduct.getName(),
                searchProduct.getMinPrice(),
                searchProduct.getMaxPrice(),
                searchProduct.getRating(),
                types,
                pageable
        );
        return productPage.map(productConverter::toDTO);
    }

    @Override
    public ProductDTO findProductById(String id) {
        // Tìm kiếm sản phẩm theo product_id
        ProductEntity productEntity = productRepository.findByProductId(id);

        // Nếu không tìm thấy sản phẩm, trả về null hoặc throw exception tuỳ theo yêu cầu
        if (productEntity == null) {
            return null;
            // Hoặc có thể throw exception như sau:
            // throw new ProductNotFoundException("Product not found for id: " + id);
        }

        // Chuyển đổi từ ProductEntity sang ProductDTO
        return productConverter.toDTO(productEntity);
    }

    @Override
    public Result updateProduct(ProductDTO productDTO, List<MultipartFile> files, List<String> sizes, List<String> quantities) {
        try {
            // 1. Lấy sản phẩm hiện tại từ cơ sở dữ liệu
            ProductEntity product = productRepository.findById(productDTO.getProduct_id())
                    .orElseThrow(() -> new Exception("Sản phẩm không tồn tại"));

            // 2. Cập nhật thông tin sản phẩm
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            if (sizes != null && quantities != null) {
                int totalQuantity = 0;
                for (String quantity : quantities) {
                    totalQuantity += Integer.parseInt(quantity);
                }
                product.setQuantity(totalQuantity);
            } else {
                product.setQuantity(productDTO.getQuantity());
            }

            product.setDescription(productDTO.getDescription());
            product.setStatus(productDTO.getStatus());
            product.setUpdated_at(new Date());
            productRepository.save(product);  // Cập nhật sản phẩm

            // 3. Cập nhật size và quantity
            if (sizes != null && quantities != null && sizes.size() == quantities.size()) {
                // Xóa size cũ
                sizeDetailRepository.deleteByProduct(product);

                for (int i = 0; i < sizes.size(); i++) {
                    String sizeName = sizes.get(i);
                    int quantity = Integer.parseInt(quantities.get(i));

                    // Kiểm tra SizeEntity
                    SizeEntity sizeEntity = sizeRepository.findByNameSize(sizeName).orElse(null);
                    if (sizeEntity == null) {
                        sizeEntity = new SizeEntity();
                        sizeEntity.setSize_id(sizeName);
                        sizeEntity.setName_size(sizeName);
                        sizeEntity = sizeRepository.save(sizeEntity);
                    }

                    // Lưu SizeDetailEntity mới vào cơ sở dữ liệu
                    SizeDetailEntity sizeDetailEntity = new SizeDetailEntity();
                    sizeDetailEntity.setProduct(product);
                    sizeDetailEntity.setSize(sizeEntity);
                    sizeDetailEntity.setQuantity(quantity);
                    sizeDetailRepository.save(sizeDetailEntity);
                }
            }

            // 4. Cập nhật ảnh nếu có
            if (files != null && files.stream().anyMatch(file -> !file.isEmpty())) {
                // Xóa ảnh cũ chỉ khi có ảnh mới
                productImageRepository.deleteByProduct(product);

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        ProductImageEntity image = new ProductImageEntity();
                        try {
                            String imagePath = cloudinaryService.uploadFileToFolder(file, "customer");
                            image.setImage_path(imagePath);
                            image.setProduct(product);
                            productImageRepository.save(image);
                        } catch (IOException e) {
                            throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
                        }
                    }
                }
            } else {
                System.out.println("Không có ảnh mới, giữ nguyên ảnh cũ.");
            }


            // 5. Cập nhật loại sản phẩm (nếu có thay đổi)
            List<String> productTypeIds = productDTO.getProductTypeIds();
            if (productTypeIds != null && !productTypeIds.isEmpty()) {
                // Xóa loại cũ
                productTypeDetailRepository.deleteByProduct(product);

                for (String typeId : productTypeIds) {
                    if (!Objects.equals(typeId, "")) {
                        ProductTypeDetailEntity typeDetail = new ProductTypeDetailEntity();
                        ProductTypeEntity productType = new ProductTypeEntity();
                        productType.setProductType_id(typeId);
                        typeDetail.setProduct(product);
                        typeDetail.setProductType(productType);
                        productTypeDetailRepository.save(typeDetail);
                    }
                }
            }

            return new Result(true, "Cập nhật sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Cập nhật sản phẩm không thành công: " + e.getMessage());
        }
    }


    @Override
    public Result deleteProduct(String productId) {
        try {
            String productIdStr = productId;
            ProductEntity product = productRepository.findById(productIdStr).orElse(null);
            if (product != null) {
                product.setStatus("DISABLE");
                productRepository.save(product);

                return new Result(true, "Xóa sản phẩm thành công");
            } else {
                return new Result(false, "Sản phẩm không tồn tại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Xóa sản phẩm không thành công: " + e.getMessage());
        }
    }



    @Override
    public List<ProductDTO> showProducts(){
        List<ProductEntity> productEntityList = productRepository.findAll();
        return productEntityList.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(String productId){
        ProductEntity productEntity = productRepository.findById(productId).orElse(null);
        return productConverter.toDTO(productEntity);
    }

    @Override
    public List<ProductDTO> filterProducts(List<String> productTypes,String beginPrice,String endPrice, String status){
        if (productTypes == null || productTypes.isEmpty()) {
            productTypes = null;
        }

        Double minPrice = (beginPrice != null && !beginPrice.isEmpty()) ? Double.valueOf(beginPrice) : null;
        Double maxPrice = (endPrice != null && !endPrice.isEmpty()) ? Double.valueOf(endPrice) : null;

        if (status == null || status.isEmpty()) {
            status = null;
        }

        System.out.println("Product Types: " + productTypes);
        System.out.println("Price Range: " + beginPrice + " - " + endPrice);
        System.out.println("Status: " + status);

        List<ProductEntity> productEntities = productRepository.filterProducts(productTypes, minPrice, maxPrice, status);
        return productEntities.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

}
