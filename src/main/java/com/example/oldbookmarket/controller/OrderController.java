package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("create-new-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewOrder(@RequestBody AddOrderRequestDTO addOrderRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            OrderResponseDTO orderResponseDTO = orderService.createNewOrder(addOrderRequestDTO);
            if (orderResponseDTO != null) {
                responseDTO.setData(orderResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping ("add_user_to_order/{orderId}/{userId}/{addressId}")
    public ResponseEntity<ResponseDTO> addUserToOrder(@PathVariable Long orderId,
                                                      @PathVariable Long userId,
                                                      @PathVariable Long addressId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            OrderResponseDTO finalOrderResponseDTO = orderService.addToOrder(orderId,userId,addressId);
            responseDTO.setData(finalOrderResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("convert-order-status/{orderId}")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Order order = orderService.converOrderStatus(orderId);
            responseDTO.setData(order);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-order-by/{userId}/{orderStatus}")
    public ResponseEntity<ResponseDTO> getAllOrder(@PathVariable Long userId, @PathVariable String orderStatus) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<Order> orderList = orderService.getAllOrder(userId, orderStatus);
            responseDTO.setData(orderList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("cancel-order/{orderId}")
    public ResponseEntity<ResponseDTO> cancelOrder(@PathVariable Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Order order = orderService.cancelOrder(orderId);
            responseDTO.setData(order);
            responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
