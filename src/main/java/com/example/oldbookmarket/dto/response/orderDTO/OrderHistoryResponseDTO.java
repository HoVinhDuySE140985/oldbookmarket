package com.example.oldbookmarket.dto.response.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderHistoryResponseDTO {
    private Long orderId;
    private LocalDate order_date;
    private String postImage;
    private String title;
    private BigDecimal amount;
    private String deliveryMethod;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private String cancelReason;
    private LocalDate resentDate;
    private String receiverName;
    private String sellerName;
    private String phoneNumber;
    private String shipAddress;
    private String form;
    private String orderCode;
}
