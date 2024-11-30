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

    public String saveOrUpdateVoucherDetail(ShopVoucherDetailDTO svdDTO, List<String> productId, String voucherId) {
        ShopVoucherEntity shopVoucherEntity = shopVoucherRepository.findShopVoucherByID(voucherId);

        for (String product : productId) {
            //nếu chưa có thì thêm mới
            if (shopVoucherDetailRepository.findShopVoucherDetailByVoucherAndProduct(voucherId, product) == null) {
                ProductEntity productEntity = productRepository.findProductByID(product);

                ShopVoucherDetailEntity shopVoucherDetailEntity = new ShopVoucherDetailEntity();
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
