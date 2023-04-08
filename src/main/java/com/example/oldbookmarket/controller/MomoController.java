package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @PostMapping("deposit_money_into_wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> depositMoneyIntoWallet(@RequestParam Long userId,
                                                              @RequestParam BigDecimal amount) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(momoService.depositMoneyIntoWallet(userId,amount));
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
