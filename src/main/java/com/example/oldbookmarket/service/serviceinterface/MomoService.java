package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoRequestDTO;
import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.respone.MomoResponseDTO;

public interface MomoService {
    MomoResponseDTO createLinkMomo(AddOrderRequestDTO addOrderRequestDTO);

//    String successMomo(AddOrderRequestDTO addOrderRequestDTO);
}
