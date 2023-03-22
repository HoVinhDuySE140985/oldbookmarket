package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.respone.ReportResponseDTO;

public interface ReportService {
    ReportResponseDTO createNewReport(ReportRequestDTO reportRequestDTO);
}
