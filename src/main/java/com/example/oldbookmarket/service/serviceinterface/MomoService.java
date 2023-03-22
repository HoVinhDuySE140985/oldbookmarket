package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoRequestDTO;
import com.example.oldbookmarket.dto.respone.MomoResponseDTO;

public interface MomoService {
    MomoResponseDTO createLinkMomo(MomoRequestDTO momoRequestDTO);

    String successMomo(Long orderId);
}
