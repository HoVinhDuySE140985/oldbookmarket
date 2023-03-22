package com.example.oldbookmarket.dto.request.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrderRequestDTO {
    private Long postId;
    private Long userId;
    private Long addressId;
    private LocalDate orderDate;
//    private Double amount;
    private String note;
//    private String paymentMethod;
//    private String deliveryMethod;
//    private String status;

}
