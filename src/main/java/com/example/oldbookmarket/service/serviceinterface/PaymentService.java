package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import org.springframework.http.ResponseEntity;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;

import java.math.BigDecimal;

public interface PaymentService {
    
//    ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request);
    ResponseEntity<MomoResponse> getPaymentMomo(String codeOrder, BigDecimal money, Long userId, String type);
    ResponseEntity<MomoResponse> getPaymentMomoV1(String codeOrder, Long postId, Long userId, BigDecimal amount, String paymentMethod,String note, String shipAddress, String type);

}
