package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @PostMapping("create-new-order")
    public ResponseEntity<ResponseDTO> createNewOrder(AddOrderRequestDTO orderRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-status/{orderId}")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable Long orderId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-order/{userId}")
    public ResponseEntity<ResponseDTO> getAllOrder(@PathVariable Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("cancel-order/{orderId}")
    public ResponseEntity<ResponseDTO> cancelOrder(@PathVariable Long orderId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
