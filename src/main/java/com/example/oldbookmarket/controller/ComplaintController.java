package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.ComplaintDTO.ComplaintRequestDTO;
import com.example.oldbookmarket.dto.response.complaintDTO.ComplaintResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    @Autowired
    ComplaintService complaintService;
    @PostMapping("create-complaint")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewComplaint(@RequestBody ComplaintRequestDTO complaintRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        ComplaintResponseDTO complaintResponseDTO = complaintService.createComplaint(complaintRequestDTO);
        try {
            responseDTO.setData(complaintResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-complaint")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> getAllComplaint(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ComplaintResponseDTO> complaintResponseDTOS = complaintService.getAllComplaint();
        try {
            responseDTO.setData(complaintResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
