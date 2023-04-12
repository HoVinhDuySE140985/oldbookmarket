package com.example.oldbookmarket.service.serviceinterface;

import org.springframework.http.ResponseEntity;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;

import java.math.BigDecimal;

public interface PaymentService {
    
//    ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request);
    ResponseEntity<MomoResponse> getPaymentMomo(Long orderId, BigDecimal money);
}
