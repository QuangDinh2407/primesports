package com.sportshop.Service;

import com.sportshop.Modal.ProductSize;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserOrderDTO;

import java.util.List;

public interface UserOrderService {
    public List<UserOrderDTO> getAllUserOrders();
    public UserOrderDTO getUserOrderById(String userOrderId) ;
    public UserOrderDTO saveOrUpdateUserOrder(UserOrderDTO userOrderDTO);
    public void deleteUserOrder(String userOrderId);
    public List<UserOrderDTO> findAllOrdersByUserId(String userInfoId);

    UserOrderDTO checkoutProduct(List<String>productIds,List<String> sizes, List<Integer> amounts);
    List<ProductSize> createOrder(UserOrderDTO userOrderDTO);

    Result cancelOrder(String orderId);

}
