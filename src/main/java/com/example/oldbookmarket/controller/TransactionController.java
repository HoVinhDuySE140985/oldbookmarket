package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.TransactionDTO.TransResponseDTO;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransService transService;

    @GetMapping("get-all-transaction")
    @PermitAll
    public ResponseEntity<ResponseDTO> getSubCategoryByCateId(@RequestParam @Validated Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<TransResponseDTO> list = transService.getAllTransaction(userId);
            responseDTO.setData(list);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ResponseEntity.ok().body(responseDTO);
    }
}
