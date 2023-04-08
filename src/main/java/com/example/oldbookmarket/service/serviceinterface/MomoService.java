package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponseDTO;

import java.math.BigDecimal;

public interface MomoService {
    MomoResponseDTO createLinkMomo(AddOrderRequestDTO addOrderRequestDTO);

    Boolean depositMoneyIntoWallet(Long userId, BigDecimal amount);
}
