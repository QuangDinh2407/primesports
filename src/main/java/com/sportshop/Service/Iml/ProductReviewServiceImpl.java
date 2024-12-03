//package com.sportshop.Service.Iml;
//
//import com.sportshop.Converter.ProductReviewConverter;
//import com.sportshop.Entity.ProductReviewEntity;
//import com.sportshop.ModalDTO.ProductReviewDTO;
//import com.sportshop.Repository.ProductReviewRepository;
//import com.sportshop.Service.ProductReviewService;
//import com.sportshop.Repository.ProductRepository;
//import com.sportshop.Repository.UserInfoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ProductReviewServiceImpl implements ProductReviewService {
//
//    @Autowired
//    private ProductReviewRepository productReviewRepository;
//
//    @Autowired
//    private ProductReviewConverter productReviewConverter;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserInfoRepository userInfoRepository;
//
//    @Override
//    public ProductReviewDTO save(ProductReviewDTO productReviewDTO) {
//        ProductReviewEntity entity = productReviewConverter.toEntity(productReviewDTO);
//
//        // Set Product và UserInfo từ Repository
//        entity.setProduct(productRepository.findById(productReviewDTO.getProduct_id()).orElse(null));
//        entity.setUserInfo(userInfoRepository.findById(productReviewDTO.getUserInfo_id()).orElse(null));
//        entity.setCreated_at(new Date());
//        entity.setUpdated_at(new Date());
//
//        ProductReviewEntity savedEntity = productReviewRepository.save(entity);
//        return productReviewConverter.toDTO(savedEntity);
//    }
//
//    @Override
//    public List<ProductReviewDTO> findByProductId(String productId) {
//        List<ProductReviewEntity> entities = productReviewRepository.findByProduct_Product_id(productId);
//        return entities.stream().map(productReviewConverter::toDTO).collect(Collectors.toList());
//    }
//}
