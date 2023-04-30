package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.RevenueResponseDTO;
import com.example.oldbookmarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusAndUser_Id(String status, Long userId);

    Order findByCodeOrder(String codeOrder);

    @Query("Select o \n" +
            "From Order as o \n" +
            "where year(o.orderDate) = :year and month(o.orderDate) = :month and o.status = 'complete'")
    List<Order> findAllByOrderDate(int year, int month);

    @Query("Select o \n" +
            "From Order as o \n" +
            "where year(o.orderDate) = :year and o.status = 'complete'")
    List<Order> findAllByYear(int year);

    Order findOrderByCodeOrder(String orderCode);

    Order findOrderByPostId(Long postId);
}
