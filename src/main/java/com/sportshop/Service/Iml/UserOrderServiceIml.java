package com.sportshop.Service.Iml;
import com.sportshop.Converter.ProductConverter;
import com.sportshop.Entity.SizeDetailEntity;
import com.sportshop.Entity.UserOrderEntity;

import com.sportshop.Converter.UserOrderConverter;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Modal.ProductSize;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.SizeDetailRepository;
import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.PaymentTypeService;
import com.sportshop.Service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Autowired
    PaymentTypeService paymentTypeService;

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
    public UserOrderDTO checkoutProduct(List<String> productIds,List<String> sizes, List<Integer> amounts) {
        UserOrderDTO order = new UserOrderDTO();
        List <PaymentTypeDTO> listPayment = paymentTypeService.listPayment();
        List<ProductDTO> listPro = productRepository.findByProductIds(productIds).stream().map(productConverter::toDTO).collect(Collectors.toList());
        Map<String, ProductDTO> productMap = listPro.stream()
                .collect(Collectors.toMap(ProductDTO::getProduct_id, product -> product));

        List<UserOrderDetailDTO> orderDetails = IntStream.range(0, productIds.size())
                .mapToObj(i -> {
                    String productId = productIds.get(i);
                    ProductDTO product = productMap.get(productId);

                    UserOrderDetailDTO detail = new UserOrderDetailDTO();
                    detail.setProduct(product);
                    detail.setSize(sizes.get(i));
                    detail.setAmount(amounts.get(i));
                    detail.setPrice(product.getPrice());
                    return detail;
                })
                .collect(Collectors.toList());

        order.setPaymentType(listPayment.getFirst());
        order.setUserOrderDetails(orderDetails);
        Float totalPrice = orderDetails.stream()
                .map(detail -> detail.getPrice() * detail.getAmount())
                .reduce(0f, Float::sum);
        order.setTotal_price(totalPrice);
        order.setCreated_at(new Date());
        return order;
    }

    @Override
    public List <ProductSize> createOrder(UserOrderDTO userOrderDTO) {
       UserOrderEntity userOrderEntity = new UserOrderEntity();
       userOrderEntity = userOrderConverter.toEntity(userOrderDTO);
       userOrderRepository.save(userOrderEntity);
       List <ProductSize> productSizeList = new ArrayList<>();
       userOrderEntity.getUserOrderDetailItems().forEach(item ->{
           ProductSize productSize = new ProductSize();
           productSize.setProductId(item.getProduct().getProduct_id());
           productSize.setSizeId(item.getSize().getSize_id());
           productSize.setAmount(item.getAmount());
           productSizeList.add(productSize);
       });
        return productSizeList;
    }
}
