package com.sportshop.Service.Iml;

import com.sportshop.Converter.ShopVoucherDetailConverter;
import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.Entity.ShopVoucherEntity;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ShopVoucherDetailRepository;
import com.sportshop.Repository.ShopVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ShopVoucherDetailServiceIml {

    @Autowired
    private ShopVoucherDetailRepository shopVoucherDetailRepository;

    @Autowired
    private ShopVoucherDetailConverter shopVoucherDetailConverter;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopVoucherRepository shopVoucherRepository;

    public List<ShopVoucherDetailDTO> findAll() {
        List<ShopVoucherDetailEntity> entities = shopVoucherDetailRepository.findAll();
        return entities.stream()
                .map(shopVoucherDetailConverter::toDTO)
                .collect(Collectors.toList());

    }

    public List<String> convertProductToString(List<ProductEntity> productEntityList) {
        List<String> result = new ArrayList<>();
        for (ProductEntity pe : productEntityList) {
            result.add(pe.getProduct_id().toString());
        }
        return result;
    }

    public List<String> getProductIdsByVoucher(String voucherId) {
        // Lấy danh sách sản phẩm từ repository
        List<ProductEntity> products = shopVoucherDetailRepository.findProductByShopVoucherId(voucherId);

        // Chuyển đổi danh sách sản phẩm thành danh sách String ID
        return products.stream()
                .map(ProductEntity::getProduct_id) // Thay getId() bằng field tương ứng
                .collect(Collectors.toList());
    }

    public String saveOrUpdateVoucherDetail(ShopVoucherDetailDTO svdDTO, List<String> productId, String voucherId) {
        ShopVoucherEntity shopVoucherEntity = shopVoucherRepository.findShopVoucherByID(voucherId);
        for (String product : productId) {
            if (shopVoucherDetailRepository.findShopVoucherDetailByVoucherAndProduct(voucherId, product) == null) {
                ShopVoucherDetailEntity shopVoucherDetailEntity = new ShopVoucherDetailEntity();
                ProductEntity productEntity = productRepository.findProductByID(product);
                shopVoucherDetailEntity.setCreated_at(svdDTO.getCreated_at());
                shopVoucherDetailEntity.setUpdated_at(svdDTO.getUpdated_at());
                shopVoucherDetailEntity.setProduct(productEntity);
                shopVoucherDetailEntity.setShopVoucher(shopVoucherEntity);
                shopVoucherDetailRepository.save(shopVoucherDetailEntity);
            }
        }
        return "success";
    }

    public String deleteProductByVoucherId(String voucherId) {
        shopVoucherDetailRepository.deleteProductInVoucher(voucherId);
        return "success";
    }

}