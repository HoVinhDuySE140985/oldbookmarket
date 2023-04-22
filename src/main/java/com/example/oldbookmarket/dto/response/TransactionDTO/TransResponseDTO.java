package com.example.oldbookmarket.dto.response.TransactionDTO;

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
public class TransResponseDTO {
    private Long transId;
    private String orderCode;
    private BigDecimal amount;
    private LocalDate createAt;
    private String type;
}
