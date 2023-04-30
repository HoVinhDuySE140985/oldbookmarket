package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import com.example.oldbookmarket.service.serviceinterface.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoConfirmResultResponse;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.shared.utils.Common;
import com.example.oldbookmarket.shared.utils.Utilities;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderService orderService;
    @Autowired
    WalletService walletService;

    @PostMapping("/momo")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<MomoResponse> paymentWithMomo(@RequestParam String codeOrder,
                                                        @RequestParam BigDecimal money,
                                                        @RequestParam Long userId,
                                                        @RequestParam String type) {
        return paymentService.getPaymentMomo(codeOrder,money,userId,type);
    }


    @GetMapping("/MomoConfirm/{type}/{userId}/{postId}/{paymentMethod}/{note}/{shipAddress}")
    @PermitAll
    public ResponseEntity<MomoConfirmResultResponse> momoConfirm(
            @PathVariable String type,
            @PathVariable Long userId,
            @PathVariable Long postId,
            @PathVariable String paymentMethod,
            @PathVariable String note,
            @PathVariable String shipAddress,
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
//        String redirectUrl = "https://www.youtube.com/";
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
        try {
            MomoConfirmResultResponse momoConfirmResultResponse = new MomoConfirmResultResponse(
                    partnerCode, orderInfo, responseTime, amount, orderInfo, orderType, transId,
                    resultCode, message, payType, resultCode, extraData, signatureHmac, Common.PARTNER_CODE);
            String msg = "";
            if (momoConfirmResultResponse.getResultCode() == 0) {
                // System.out.println("Giao Dich Thanh cong");

                msg = "giao dich thanh cong";
            } else if (momoConfirmResultResponse.getResultCode() == 9000) {
                msg = "giao dich duoc xac nhan, giao dich thang cong!";
                if (type.equalsIgnoreCase("Nạp Tiền")){
                    walletService.rechargeIntoWallet(userId, BigDecimal.valueOf(amount), orderId);
                }else {
                    orderService.createNewOrder(userId,postId,BigDecimal.valueOf(amount),paymentMethod,note,shipAddress,orderId);
                }
            }else if (momoConfirmResultResponse.getResultCode() == 1006){
                msg = "người dùng từ chối giao dịch!";
//            redirectUrl = "https://www.youtube.com/";
            }
            logger.info("" + msg);
            System.out.println(resultCode);
            System.out.println(msg);
            // accessKey=WehkypIRwPP14mHb&orderId=23&partnerCode=MOMODJMX20220717&requestId=48468005-6de1-4140-839f-5f2d8d77a001
        }catch (Exception e){
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://thriving-crumble-cf370e.netlify.app/")); // deploy lên thì chạy về trang cần trả về
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

}
