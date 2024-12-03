package com.sportshop.Service;

import com.sportshop.ModalDTO.UserOrderDTO;

import java.util.List;

public interface UserOrderService {
    public List<UserOrderDTO> getAllUserOrders();
    public UserOrderDTO getUserOrderById(String userOrderId) ;
    public UserOrderDTO saveOrUpdateUserOrder(UserOrderDTO userOrderDTO);
    public void deleteUserOrder(String userOrderId);
    public List<UserOrderDTO> findAllOrdersByUserId(String userInfoId);
}
