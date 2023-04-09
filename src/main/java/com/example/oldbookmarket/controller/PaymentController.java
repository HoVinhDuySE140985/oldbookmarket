package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoConfirmResultResponse;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.shared.utils.Common;
import com.example.oldbookmarket.shared.utils.Utilities;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderService orderService;

    @PostMapping("/momo")
    @PreAuthorize("hasRole('CUSTOMER')")
//    public ResponseEntity<MomoResponse> paymentWithMomo(@RequestBody MomoClientRequest request) {
    public ResponseEntity<MomoResponse> paymentWithMomo(@RequestParam Long orderId, @RequestParam BigDecimal money) {
        return paymentService.getPaymentMomo(orderId,money);
    }


    @GetMapping("/MomoConfirm")
    @PermitAll
    public ResponseEntity<MomoConfirmResultResponse> momoConfirm(
            @RequestParam("partnerCode") String partnerCode,
            @RequestParam("orderId") String orderId,
            @RequestParam("requestId") String requestId,
            @RequestParam("amount") long amount,
            @RequestParam("orderInfo") String orderInfo,
            @RequestParam("orderType") String orderType,
            @RequestParam("transId") long transId,
            @RequestParam("resultCode") int resultCode,
            @RequestParam("message") String message,
            @RequestParam("payType") String payType,
            @RequestParam("responseTime") String responseTime,
            @RequestParam("extraData") String extraData,
            @RequestParam("signature") String signature) {
        String sign = "accessKey=" +
                Common.ACCESS_KEY + "&orderId=" + orderId + "&partnerCode=" + Common.PARTNER_CODE
                + "&requestId=" + requestId;
        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
            logger.error("Signiture HMAC loi!");
        }

        MomoConfirmResultResponse momoConfirmResultResponse = new MomoConfirmResultResponse(
                partnerCode, orderInfo, responseTime, amount, orderInfo, orderType, transId,
                resultCode, message, payType, resultCode, extraData, signatureHmac, Common.PARTNER_CODE);

        String msg = "";
        if (resultCode == 0) {
            // System.out.println("Giao Dich Thanh cong");

            msg = "giao dich thanh cong";
        } else if (resultCode == 9000) {
            msg = "giao dich duoc xac nhan, giao dich thang cong!";
        }
        logger.info("" + msg);
        System.out.println(resultCode);
        System.out.println(msg);
        // accessKey=WehkypIRwPP14mHb&orderId=23&partnerCode=MOMODJMX20220717&requestId=48468005-6de1-4140-839f-5f2d8d77a001
        return new ResponseEntity<>(momoConfirmResultResponse, HttpStatus.OK);
    }
}
