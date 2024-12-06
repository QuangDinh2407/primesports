package com.sportshop.Service.Iml;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Entity.UserOrderEntity;

import com.sportshop.Converter.UserOrderConverter;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.UserOrderDTO;
import com.sportshop.ModalDTO.UserOrderDetailDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserOrderServiceIml implements UserOrderService {

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    UserOrderConverter userOrderConverter;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;


    @Override
    public List<UserOrderDTO> findAllOrdersByUserId(String userInfo_id) {
        List<UserOrderEntity> userOrders = userOrderRepository.findAllOrdersByUser(userInfo_id);
        return userOrders.stream()
                .map(userOrderConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserOrderDTO> getAllUserOrders() {
        List<UserOrderEntity> userOrders = userOrderRepository.findAll();
        return userOrders.stream()
                .map(userOrderConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserOrderDTO getUserOrderById(String userOrderId) {
        UserOrderEntity userOrder = userOrderRepository.findById(userOrderId)
                .orElseThrow(() -> new RuntimeException("UserOrder not found with ID: " + userOrderId));
        return userOrderConverter.toDTO(userOrder);
    }

    @Override
    public UserOrderDTO saveOrUpdateUserOrder(UserOrderDTO userOrderDTO) {
        UserOrderEntity userOrderEntity = userOrderConverter.toEntity(userOrderDTO);
        UserOrderEntity savedUserOrder = userOrderRepository.save(userOrderEntity);
        return userOrderConverter.toDTO(savedUserOrder);
    }

    @Override
    public void deleteUserOrder(String userOrderId) {
        if (!userOrderRepository.existsById(userOrderId)) {
            throw new RuntimeException("UserOrder not found with ID: " + userOrderId);
        }
        userOrderRepository.deleteById(userOrderId);
    }

    @Override
    public UserOrderDTO checkoutProduct(List<String> productIds) {
        UserOrderDTO order = new UserOrderDTO();
        List<ProductDTO> listPro = productRepository.findByProductIds(productIds).stream().map(productConverter::toDTO).collect(Collectors.toList());
        List<UserOrderDetailDTO> orderDetails = listPro.stream().map(product -> {
            UserOrderDetailDTO detail = new UserOrderDetailDTO();
            detail.setProduct(product);
            detail.setAmount(1);
            detail.setPrice(product.getPrice());
            return detail;
        }).collect(Collectors.toList());

        order.setUserOrderDetails(orderDetails);

        float totalPrice = orderDetails.stream()
                .map(detail -> detail.getPrice() * detail.getAmount())
                .reduce(0f, Float::sum);

        order.setTotal_price(totalPrice);
        order.setCreated_at(new Date());
        return order;
    }
}
