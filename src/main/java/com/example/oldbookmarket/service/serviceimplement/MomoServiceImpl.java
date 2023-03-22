package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.Configuration.Environment;
import com.example.oldbookmarket.dto.request.MomoDTO.MomoRequestDTO;
import com.example.oldbookmarket.dto.respone.MomoResponseDTO;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.enumcode.RequestType;
import com.example.oldbookmarket.models.PaymentResponse;
import com.example.oldbookmarket.processor.CreateOrderMoMo;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.service.serviceinterface.MomoService;
import com.example.oldbookmarket.shared.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MomoServiceImpl implements MomoService {
    @Autowired
    private OrderRepo orderRepo;
    @Override
    public MomoResponseDTO createLinkMomo(MomoRequestDTO momoRequestDTO) {
        Order order = orderRepo.findById(momoRequestDTO.getOrderId()).get();
        LogUtils.init();

// chinh big decimal
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
//        Long transId = 2L;

        String orderInfo = "Pay With MoMo";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse  = null;
        try {
            captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, BigDecimal.valueOf(50000L).toString(), orderInfo, successMomo(order.getId()), momoRequestDTO.getNotifyURL(), "", RequestType.CAPTURE_WALLET, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MomoResponseDTO momoResponseDTO = MomoResponseDTO.builder()
                .url(captureWalletMoMoResponse.getPayUrl())
                .build();
        return momoResponseDTO;
    }

    @Override
    public String successMomo(Long orderId) {
        Order order = orderRepo.findById(orderId).get();
        order.setStatus("PAID");
        orderRepo.save(order);
        String url = "https://www.youtube.com/";
        return url;
    }
}
