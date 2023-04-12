package com.example.oldbookmarket.dto.response.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long postId;
    private String shipAddress;
    private LocalDate orderDate;
    private BigDecimal amount;
    private String note;
    private String paymentMethod;
    private String deliveryMethod;
    private Long userId;
    private String status;
    private String paymentStatus;
    private String form;
}
