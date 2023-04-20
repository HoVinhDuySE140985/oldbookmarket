package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.RevenueResponseDTO;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("create-new-order-with-momo")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewOrderWithMomo(@RequestParam @Validated Long postId,
                                                              @RequestParam @Validated Long userId,
                                                              @RequestParam @Validated BigDecimal amount,
                                                              @RequestParam @Validated String paymentMethod,
                                                              @RequestParam @Validated String note,
                                                              @RequestParam @Validated String shipAddress) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(orderService.createNewOrderWithMomo(postId, userId, amount, paymentMethod, note, shipAddress));
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create-new-order-with-my-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewOrderWithYMyWallet(@RequestBody @Validated AddOrderRequestDTO addOrderRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            OrderResponseDTO orderResponseDTO = orderService.createNewOrderWithMyWallet(addOrderRequestDTO);
            if (orderResponseDTO != null) {
                responseDTO.setData(orderResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
            }
        } catch (Exception e) {
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

    @PutMapping("update_resent_date")
    public ResponseEntity<ResponseDTO> updateResentDate(@RequestParam(required = true) @Validated Long orderId,
                                                        @RequestParam(required = true) @Validated String resentDate) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(orderService.updateResentDate(orderId, resentDate));
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("cancel_order_by_orderId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> cancelOrder(@RequestParam Long orderId,
                                                   @RequestParam String cancelReason) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Order order = orderService.cancelOrder(orderId, cancelReason);
            responseDTO.setData(order);
            responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_All_Sell_Order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllSellOrder(@RequestParam @Validated Long userId,
                                                       @RequestParam @Validated String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllSellOrder(userId, status);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_All_Bought_Order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllBoughtOrder(@RequestParam @Validated Long userId,
                                                         @RequestParam @Validated String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllBoughtOrder(userId, status);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_all_order_by_status") // check lại xem ham nay cua admin hay cus
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllOrderByStatus(@RequestParam @Validated Long userId,
                                                           @RequestParam @Validated String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderService.getAllOrderByStatus(userId, status);
            responseDTO.setData(orderHistoryResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(orderHistoryResponseDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getRevenue(@RequestParam String month,
                                                  @RequestParam String year) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<RevenueResponseDTO> result = orderService.profitCalculation(month, year);
            responseDTO.setData(result);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
