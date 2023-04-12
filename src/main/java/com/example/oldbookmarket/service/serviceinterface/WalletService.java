package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.walletDTO.WalletResponseDTO;
import com.example.oldbookmarket.entity.Wallet;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface WalletService {
    ResponseEntity<MomoResponse> rechargeIntoWallet(Long userId, Long purchaseId, BigDecimal depositAmount);
    WalletResponseDTO viewWallet(Long userId);
}
