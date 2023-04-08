package com.example.oldbookmarket.service;

import org.springframework.http.ResponseEntity;

import com.example.oldbookmarket.dto.request.MomoClientRequest;
import com.example.oldbookmarket.dto.response.MomoResponse;

public interface PaymentService {
    
    ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request);
}
