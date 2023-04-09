package com.example.oldbookmarket.service.serviceinterface;

import org.springframework.http.ResponseEntity;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;

public interface PaymentService {
    
    ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request);
}
