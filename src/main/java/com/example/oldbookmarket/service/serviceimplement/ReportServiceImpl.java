package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.respone.ReportResponseDTO;
import com.example.oldbookmarket.service.serviceinterface.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public ReportResponseDTO createNewReport(ReportRequestDTO reportRequestDTO) {
        return null;
    }
}
