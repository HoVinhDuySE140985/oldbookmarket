package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.ReportDTO.ReportRequestDTO;
import com.example.oldbookmarket.dto.respone.ReportResponseDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("createNewReport")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> createNewReport(@RequestBody ReportRequestDTO reportRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        ReportResponseDTO reportResponseDTO = reportService.createNewReport(reportRequestDTO);
        try {
            responseDTO.setData(reportRequestDTO);
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
