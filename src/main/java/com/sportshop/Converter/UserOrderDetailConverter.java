package com.sportshop.Converter;

import com.sportshop.Entity.*;
import com.sportshop.ModalDTO.UserOrderDetailDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.ShopVoucherRepository;
import com.sportshop.Repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserOrderDetailConverter {

    @Autowired
    ProductConverter productConverter;
    @Autowired
    private ShopVoucherConverter shopVoucherConverter;
    @Autowired
    private ShopVoucherRepository shopVoucherRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductRepository productRepository;

    public UserOrderDetailEntity toEntity(UserOrderDetailDTO userOrderDetailDTO, UserOrderEntity userOrderEntity) {
        if (userOrderDetailDTO == null) {
            return null;
        }

        UserOrderDetailEntity userOrderDetailEntity = new UserOrderDetailEntity();
        userOrderDetailEntity.setAmount(userOrderDetailDTO.getAmount());
        userOrderDetailEntity.setPrice(userOrderDetailDTO.getPrice());
        ProductEntity productEntity = productRepository.findByProductId(userOrderDetailDTO.getProduct().getProduct_id());
        userOrderDetailEntity.setProduct(productEntity);

        ShopVoucherEntity shopVoucherEntity = null;
        if (userOrderDetailDTO.getShopVoucher() != null) {
            shopVoucherEntity = shopVoucherRepository.findShopVoucherByID(userOrderDetailDTO.getShopVoucher().getShopVoucher_id());
            userOrderDetailEntity.setShopVoucher(shopVoucherEntity);
        }
        SizeEntity sizeEntity = sizeRepository.findByName(userOrderDetailDTO.getSize());
        userOrderDetailEntity.setSize(sizeEntity);

        userOrderDetailEntity.setUserOrder(userOrderEntity);

        return userOrderDetailEntity;
    }

}
