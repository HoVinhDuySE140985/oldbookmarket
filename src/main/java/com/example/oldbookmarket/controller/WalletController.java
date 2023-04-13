package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.service.serviceinterface.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    WalletService walletService;

    @PostMapping("/recharge-into-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> rechargeIntoWallet(@RequestParam Long userId,
                                                          @RequestParam BigDecimal depositAmount) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(walletService.rechargeIntoWalletMoMo(userId,depositAmount));
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/view-money-in-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> viewMoneyInWallet(@RequestParam Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(walletService.viewWallet(userId));
            responseDTO.setSuccessCode(SuccessCode.Get_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
