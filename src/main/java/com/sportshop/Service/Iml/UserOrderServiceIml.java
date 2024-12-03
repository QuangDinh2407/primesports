package com.sportshop.Service.Iml;
import com.sportshop.Entity.UserOrderEntity;

import com.sportshop.Converter.UserOrderConverter;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.ModalDTO.UserOrderDTO;
import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserOrderServiceIml implements UserOrderService {

    private final UserOrderRepository userOrderRepository;
    private final UserOrderConverter userOrderConverter;

    @Autowired
    public UserOrderServiceIml(UserOrderRepository userOrderRepository, UserOrderConverter userOrderConverter) {
        this.userOrderRepository = userOrderRepository;
        this.userOrderConverter = userOrderConverter;
    }

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
}
