package com.example.oldbookmarket.dto.response.orderDTO;

import com.example.oldbookmarket.dto.response.bookDTO.BookPendingResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private String orderCode;
    private String postImage;
    private List<BookPendingResponseDTO> listBooks;
    private int isCheck;
}
