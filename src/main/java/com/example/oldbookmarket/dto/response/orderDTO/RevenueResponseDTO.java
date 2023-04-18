package com.example.oldbookmarket.dto.response.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevenueResponseDTO {
    private BigDecimal amount;
    private int day;
    private int month;
}
