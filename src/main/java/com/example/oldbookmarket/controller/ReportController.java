package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.response.reportDTO.ReportResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("createNewReport")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> createNewReport(@RequestBody @Validated ReportRequestDTO reportRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ReportResponseDTO reportResponseDTO = reportService.createNewReport(reportRequestDTO);
            responseDTO.setData(reportResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllReport(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<ReportResponseDTO> reportRequestDTOList = reportService.getAllReport();
            responseDTO.setData(reportRequestDTOList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
