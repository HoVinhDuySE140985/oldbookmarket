package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusAndUser_Id(String status, Long userId );

}
