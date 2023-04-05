package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createNewOrder(AddOrderRequestDTO addOrderRequestDTO);
    Boolean converOrderStatus(Long orderId);
    List<Order> getAllOrder(Long userId, String orderStatus);
    Order cancelOrder(Long orderId, String cancelReason);

    OrderResponseDTO addToOrder(Long orderId, Long userId, Long addressId);

    Boolean updateResentDate(Long orderId, String resentDate);

}
