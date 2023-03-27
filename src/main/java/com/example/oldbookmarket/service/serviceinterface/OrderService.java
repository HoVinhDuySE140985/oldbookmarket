package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createNewOrder(AddOrderRequestDTO addOrderRequestDTO);
    Order converOrderStatus(Long orderId);
    List<Order> getAllOrder(Long userId, String orderStatus);
    Order cancelOrder(Long orderId);

    OrderResponseDTO addToOrder(String orderId, String userId, String addressId);

}
