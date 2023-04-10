package com.example.oldbookmarket.dto.response.walletDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WalletResponseDTO {
    private BigDecimal amount;
}
