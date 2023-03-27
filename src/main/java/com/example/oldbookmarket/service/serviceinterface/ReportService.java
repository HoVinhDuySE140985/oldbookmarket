package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.response.reportDTO.ReportResponseDTO;

import java.util.List;

public interface ReportService {
    ReportResponseDTO createNewReport(ReportRequestDTO reportRequestDTO);
    List<ReportResponseDTO> getAllReport();
}
