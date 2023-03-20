package com.example.oldbookmarket.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Double amount;
    private String note;
    private String paymentMethod;
    private String deliveryMethod;
    private Long userId;
    private String status;
}