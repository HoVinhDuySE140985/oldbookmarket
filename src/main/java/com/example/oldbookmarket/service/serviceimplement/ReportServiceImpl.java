package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.respone.ReportResponseDTO;
import com.example.oldbookmarket.entity.Report;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.ReportRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepo reportRepo;

    @Autowired
    UserRepo userRepo;
    @Override
    public ReportResponseDTO createNewReport(ReportRequestDTO reportRequestDTO) {
        ReportResponseDTO reportResponseDTO = null;
        try {
            User user = userRepo.findById(reportResponseDTO.getUserId()).get();
            Report report = Report.builder()
                    .user(user)
                    .reason(reportRequestDTO.getReason())
                    .build();
            report = reportRepo.save(report);
            reportResponseDTO = ReportResponseDTO.builder()
                    .id(report.getId())
                    .userId(report.getUser().getId())
                    .reason(report.getReason())
                    .createAt(report.getCreateAt())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportResponseDTO;
    }

    @Override
    public List<ReportResponseDTO> getAllReport() {
        List<Report> reportList = null;
        List<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
        try {
            reportList = reportRepo.findAll();
            for (Report report : reportList){
                ReportResponseDTO reportResponseDTO = new ReportResponseDTO();
                reportResponseDTO.setId(report.getId());
                reportResponseDTO.setUserId(report.getUser().getId());
                reportResponseDTO.setReason(report.getReason());
                reportResponseDTO.setCreateAt(report.getCreateAt());
                reportResponseDTOS.add(reportResponseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportResponseDTOS;
    }
}
