package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.emailDTO.EmailResponseDTO;

public interface EmailService {
    String sendSimpleMail(EmailResponseDTO dto);
}
