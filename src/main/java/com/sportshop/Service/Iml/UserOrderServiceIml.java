package com.sportshop.Service.Iml;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.UserOrderEntity;

import com.sportshop.Converter.UserOrderConverter;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.UserOrderDTO;
import com.sportshop.ModalDTO.UserOrderDetailDTO;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
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
    public Page<UserOrderDTO> getAllUserOrdersPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UserOrderEntity> userOrders = userOrderRepository.findAll(pageable);
        return userOrders.map(userOrderConverter::toDTO); // Chuyển đổi từng phần tử từ Entity sang DTO
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

    @Override
    public Result updateUserOrderStatus(String status, String userOrderId){
        try{
            Optional<UserOrderEntity> optionalOrder = userOrderRepository.findById(userOrderId);
            if (optionalOrder.isEmpty()) {
                return new Result(false, "Cập nhật đơn hàng không thành công");
            }
            UserOrderEntity order = optionalOrder.get();
            order.setStatus(status);
            order.setUpdated_at(new Date());
            userOrderRepository.save(order);
            return new Result(true, "Cập nhật đơn hàng thành công");
        } catch (Exception e) {
            return new Result(false, "Đã xảy ra lỗi trong quá trình cập nhật");
        }

    }

    @Override
    public List<UserOrderDTO> filterOrders(String beginPrice, String endPrice, String beginDate, String endDate, String status) {
        Float parsedBeginPrice = beginPrice != null && !beginPrice.isEmpty() ? Float.parseFloat(beginPrice) : null;
        Float parsedEndPrice = endPrice != null && !endPrice.isEmpty() ? Float.parseFloat(endPrice) : null;
        Date parsedBeginDate = beginDate != null && !beginDate.isEmpty() ? parseDate(beginDate) : null;
        Date parsedEndDate = endDate != null && !endDate.isEmpty() ? parseDate(endDate) : null;


        List<UserOrderEntity> filteredOrders = userOrderRepository.filterOrders(status != null && !status.isEmpty() ? status : null, parsedBeginPrice, parsedEndPrice, parsedBeginDate, parsedEndDate);

        return filteredOrders.stream()
                .map(userOrderConverter::toDTO)
                .collect(Collectors.toList());
    }





}
