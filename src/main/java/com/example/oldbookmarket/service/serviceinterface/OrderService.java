package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.orderDTO.DataResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.RevenueResponseDTO;
import com.example.oldbookmarket.entity.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    ResponseEntity<MomoResponse> createNewOrderWithMomo(Long postId, Long userId, BigDecimal amount,String paymentMethod,String note,String shipAddress);
    ResponseEntity<MomoResponse> createNewOrder(Long postId, Long userId, BigDecimal amount,String paymentMethod,String note,String shipAddress, String orderCode);
    OrderResponseDTO createNewOrderWithMyWallet(AddOrderRequestDTO addOrderRequestDTO);
    Boolean converOrderStatus(Long orderId);
    List<OrderResponseDTO> getALLOrder(String orderCode);
    Order cancelOrder(Long orderId, String cancelReason);
    Boolean updateResentDate(Long orderId, String resentDate);
    List<OrderHistoryResponseDTO> getAllSellOrder(Long userId, String status);
    List<OrderHistoryResponseDTO> getAllBoughtOrder(Long userId,String status);
    List<OrderHistoryResponseDTO> getAllOrderByStatus(Long userId, String status);
    List<RevenueResponseDTO> profitCalculation( String month, String year);
    Boolean refundOrderToWallet(Long orderId);

    DataResponseDTO getAllUserAndOrder();
}
