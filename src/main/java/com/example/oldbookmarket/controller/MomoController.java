package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoRequestDTO;
import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/momo")
public class MomoController {
    @Autowired
    MomoService momoService;

    @PostMapping("create-link")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewLink(@RequestBody AddOrderRequestDTO addOrderRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(momoService.createLinkMomo(addOrderRequestDTO));
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
