package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.Configuration.Environment;
import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponseDTO;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.enumcode.RequestType;
import com.example.oldbookmarket.models.PaymentResponse;
import com.example.oldbookmarket.processor.CreateOrderMoMo;
import com.example.oldbookmarket.repository.WalletRepo;
import com.example.oldbookmarket.service.serviceinterface.MomoService;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import com.example.oldbookmarket.shared.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MomoServiceImpl implements MomoService {
    @Autowired
    private OrderService orderService;

    @Autowired
    WalletRepo walletRepo;

    @Override
    public MomoResponseDTO createLinkMomo(AddOrderRequestDTO addOrderRequestDTO) {
        orderService.createNewOrder(addOrderRequestDTO);

        LogUtils.init();

// chinh big decimal
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
//        Long transId = 2L;

        String orderInfo = "Pay With MoMo";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse  = null;
        try {

            captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, "50000", orderInfo, "http://localhost:8080/api/order/add_user_to_order/"+ addOrderRequestDTO.getPostId()+"/"+addOrderRequestDTO.getUserId()+"/1", addOrderRequestDTO.getFailUrl(), "", RequestType.CAPTURE_WALLET, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MomoResponseDTO momoResponseDTO = MomoResponseDTO.builder()
                .url(captureWalletMoMoResponse.getPayUrl())
                .build();
        return momoResponseDTO;
    }

    @Override
    public Boolean depositMoneyIntoWallet(Long userId, BigDecimal amount) {
        Wallet userWallet = walletRepo.findById(userId).get();
        userWallet.setAmount(amount);
        return true;
    }
}
