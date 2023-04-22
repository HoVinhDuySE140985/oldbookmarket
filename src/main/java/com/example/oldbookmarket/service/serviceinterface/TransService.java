package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.TransactionDTO.TransResponseDTO;
import com.example.oldbookmarket.entity.Transaction;

import java.util.List;

public interface TransService {
    List<TransResponseDTO> getAllTransaction(Long userId);
}
