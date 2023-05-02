package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.ComplaintDTO.ComplaintRequestDTO;
import com.example.oldbookmarket.dto.response.complaintDTO.ComplaintResponseDTO;

import java.util.List;

public interface ComplaintService {
    ComplaintResponseDTO createComplaint(ComplaintRequestDTO complaintRequestDTO) ;
    List<ComplaintResponseDTO> getAllComplaint();

    Boolean rejectComplaint(Long senderId, Long complaintId);
}
