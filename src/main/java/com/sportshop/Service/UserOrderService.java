package com.sportshop.Service;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserOrderDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserOrderService {
    public List<UserOrderDTO> getAllUserOrders();
    public UserOrderDTO getUserOrderById(String userOrderId) ;
    public UserOrderDTO saveOrUpdateUserOrder(UserOrderDTO userOrderDTO);
    public void deleteUserOrder(String userOrderId);
    public List<UserOrderDTO> findAllOrdersByUserId(String userInfoId);
    public Page<UserOrderDTO> getAllUserOrdersPagination(int page, int pageSize);
    public Result updateUserOrderStatus(String status, String userOrderId);
    UserOrderDTO checkoutProduct(List<String>productIds);
    public List<UserOrderDTO> filterOrders(String beginPrice,String endPrice,String beginDate,String endDate,String status);
}
