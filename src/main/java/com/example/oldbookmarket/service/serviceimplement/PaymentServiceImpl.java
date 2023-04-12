package com.example.oldbookmarket.service.serviceimplement;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.repository.OrderRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.oldbookmarket.dto.request.MomoDTO.MomoClientRequest;
import com.example.oldbookmarket.dto.request.MomoDTO.MomoRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.shared.utils.Common;
import com.example.oldbookmarket.shared.utils.Utilities;


@Service
public class PaymentServiceImpl implements PaymentService{
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    OrderRepo orderRepo;

    @Override
    public ResponseEntity<MomoResponse> getPaymentMomo(Long orderId, BigDecimal money) {

        // request url
        String url = Common.MOMO_URI;

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object

        // build the request
        MomoRequest momoReq = new MomoRequest();
        // CustomerInfoMomoRequest customerInfo = new CustomerInfoMomoRequest("dat",
        // "0123456789", "dat@gmail.com");
//        String orderId = String.join("-", oList); //12 //23-12-23-32
        // String requestId = 
        // Order order = orderRepository.getOrderById(Long.parseLong(request.getOrderId()));

        // long amount = 200000;
        // byte[] array = new byte[10]; // length is bounded by 7
        // new Random().nextBytes(array);


        // String requestId = new String(array, Charset.forName("UTF-8"));
        // String requestId = String.valueOf(order.getId());

        DecimalFormat df = new DecimalFormat("#");

        String amount = String.valueOf(df.format(money));

        String sign = "accessKey=" + Common.ACCESS_KEY + "&amount=" + amount + "&extraData="
                + "&ipnUrl=" + Common.IPN_URL_MOMO + "&orderId=" + orderId + "&orderInfo="
                + "Thanh toan momo"
                + "&partnerCode=" + Common.PARTNER_CODE + "&redirectUrl=" + Common.REDIRECT_URL_MOMO
                + "&requestId=" + orderId + "&requestType=captureWallet";

        // accessKey=$accessKey&amount=$amount&extraData=$extraData
        // &ipnUrl=$ipnUrl&orderId=$orderId&orderInfo=$orderInfo
        // &partnerCode=$partnerCode&redirectUrl=$redirectUrl
        // &requestId=$requestId&requestType=$requestType

        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
                logger.error("signatureHmac loi !");
        }

        momoReq.setPartnerCode(Common.PARTNER_CODE);
        momoReq.setSignature(signatureHmac);
        momoReq.setAmount(money);
        momoReq.setExtraData("");
        momoReq.setIpnUrl(Common.IPN_URL_MOMO);
        momoReq.setLang("vi");
        momoReq.setOrderId(orderId.toString());
        momoReq.setOrderInfo("Thanh toan momo");
        momoReq.setRedirectUrl(Common.REDIRECT_URL_MOMO); //* */
        momoReq.setRequestId(orderId.toString());
        momoReq.setRequestType("captureWallet");

        HttpEntity<MomoRequest> req = new HttpEntity<>(momoReq, headers);

        // send POST request
        try {
            ResponseEntity<MomoResponse> response = restTemplate.postForEntity(url, req, MomoResponse.class);

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            String arr[] = String.valueOf(e.getMessage()).split(",");
            String ar[] = arr[1].split(":");
            String message = ar[1].replaceAll("\"", "");
            System.out.println("" + e.getMessage());
            // throw new AppException(HttpStatus.BAD_REQUEST.value(),
            //         new CustomResponseObject(Common.ADDING_FAIL, message));
            logger.error("Bad request !!!");

        }
        return null;
    }
}
