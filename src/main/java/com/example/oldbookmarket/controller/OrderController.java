package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
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

    @PutMapping("convert-order-status_by_orderId")
    @PermitAll
    public ResponseEntity<ResponseDTO> updateStatus(@RequestParam Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(orderService.converOrderStatus(orderId));
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("get-all-order-by/{userId}/{orderStatus}")
//    public ResponseEntity<ResponseDTO> getAllOrder(@PathVariable Long userId, @PathVariable String orderStatus) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        try {
//            List<Order> orderList = orderService.getAllOrder(userId, orderStatus);
//            responseDTO.setData(orderList);
//            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @PutMapping("update_resent_date/{orderId}")
    public ResponseEntity<ResponseDTO> updateResentDate(@RequestParam(required = true) Long orderId,
                                                        @RequestParam(required = true) String resentDate){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(orderService.updateResentDate(orderId,resentDate));
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ResponseEntity.ok().body(responseDTO);
    }
    @PutMapping("cancel_order_by_orderId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> cancelOrder(@RequestParam Long orderId,
                                                   @RequestParam String cancelReason) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Order order = orderService.cancelOrder(orderId,cancelReason);
            responseDTO.setData(order);
            responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_All_Sell_Order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllSellOrder(@RequestParam Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllSellOrder(userId);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_All_Bought_Order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllBoughtOrder(@RequestParam Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllBoughtOrder(userId);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_all_order_by_status")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllOrderByStatus(@RequestParam Long userId,
                                                           @RequestParam String status){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllOrderByStatus(userId,status);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
