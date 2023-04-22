package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.response.reportDTO.ReportResponseDTO;
import com.example.oldbookmarket.entity.Report;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.ReportRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            User approvedBy = userRepo.findById(reportRequestDTO.getApprovedBy()).get();
            User userReported = userRepo.findById(reportRequestDTO.getUserReportedId()).get();
            Report report = Report.builder()
                    .user(approvedBy)
                    .createAt(LocalDate.now())
                    .orderCode(reportRequestDTO.getOrderCode())
                    .emailReported(userReported.getEmail())
                    .reason(reportRequestDTO.getReason())
                    .build();
            report = reportRepo.save(report);
            reportResponseDTO = ReportResponseDTO.builder()
                    .id(report.getId())
                    .approvedBy(report.getUser().getId())
                    .emailReported(report.getEmailReported())
                    .orderCode(report.getOrderCode())
                    .reason(report.getReason())
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
                ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                        .id(report.getId())
                        .orderCode(report.getOrderCode())
                        .emailReported(report.getEmailReported())
                        .approvedBy(report.getUser().getId())
                        .build();
                reportResponseDTOS.add(reportResponseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportResponseDTOS;
    }
}
