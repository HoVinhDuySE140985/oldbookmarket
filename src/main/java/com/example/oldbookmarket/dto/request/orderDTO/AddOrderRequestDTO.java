package com.example.oldbookmarket.dto.request.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrderRequestDTO {
    private Long postId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String note;
    private String shipAddress;
//    private String momoStatusCode;
}
