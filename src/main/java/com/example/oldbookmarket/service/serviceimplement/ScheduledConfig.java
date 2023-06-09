package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Controller
public class ScheduledConfig {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    FcmService fcmService;

    @Autowired
    ComplaintRepo complaintRepo;

//    @Scheduled(cron = "0 0 0 ? * * " , zone = "Asia/Ho_Chi_Minh")
    @Scheduled(fixedDelay = 120000)
    public void load5sForCheckOrderStatus(){
        List<Order> orderList = orderRepo.findAll();
        Transaction transaction = null;
        for (Order order: orderList) {
            User seller = userRepo.findById(order.getPost().getUser().getId()).get();
            User admin = userRepo.findUserByRole_Id(1L);
            User buyer = userRepo.findById(order.getUser().getId()).get();
//            List<Complaint> complaints = complaintRepo.findAllByOrder_CodeOrder(order.getCodeOrder());
//            LocalDate futureDay = order.getDateShipComplete().plusDays(3);

            //bán
            if (order.getPost().getForm().equalsIgnoreCase("Bán")){
                // dơn thành công
                if (order.getStatus().equalsIgnoreCase("complete") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
//                    if (order.getDateShipComplete().equals(futureDay) && complaints.isEmpty()){
                        BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.80));

                        Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                        adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                        walletRepo.save(adminWallet);

                        Wallet sellerWallet = walletRepo.findByUserId(seller.getId());
                        System.out.println(sellerWallet.getWalletId());
                        sellerWallet.setAmount(sellerWallet.getAmount().add(amountToBePaid));
                        walletRepo.save(sellerWallet);
                        transaction = Transaction.builder()
                                .type("Nhận Tiền Thanh Toán")
                                .createAt(LocalDate.now())
                                .orderCode(order.getCodeOrder())
                                .paymentMethod("Ví Của Tôi")
                                .wallet(sellerWallet)
                                .order(order)
                                .amount(amountToBePaid)
                                .build();
                        transactionRepo.save(transaction);
                        order.setPaymentStatus("PAYMENT_COMPLETED");
                        orderRepo.save(order);
//                    }
                }
                //đơn hàng cancel
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
                    BigDecimal amountToBePaid = order.getAmount();

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Tiền Hoàn Trả")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(buyerWallet)
                            .amount(amountToBePaid)
                            .order(order)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("REFUND_COMPLETED");
                    order.setStatus("REFUND");
                    orderRepo.save(order);
                }
            }else {
                // trao đổi thanh cong
                if (order.getStatus().equalsIgnoreCase("received") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
//                    LocalDate futureDateReceive = order.getResentDate().plusDays(8);
//                    LocalDate currentDay = LocalDate.now();
//                    if (currentDay.equals(futureDateReceive) && complaints.isEmpty() ){
                        BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.90));

                        Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                        adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                        walletRepo.save(adminWallet);

                        Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                        buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                        walletRepo.save(buyerWallet);
                        transaction = Transaction.builder()
                                .type("Nhận Lại Tiền Cọc")
                                .createAt(LocalDate.now())
                                .orderCode(order.getCodeOrder())
                                .paymentMethod("Ví Của Tôi")
                                .wallet(buyerWallet)
                                .order(order)
                                .amount(amountToBePaid)
                                .build();
                        transactionRepo.save(transaction);
                        order.setPaymentStatus("EXCHANGE_COMPLETED");
                        orderRepo.save(order);
//                    }
                }
                // trao đổi bị cancel
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
                    BigDecimal amountToBePaid = order.getAmount();

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Lại Tiền Cọc")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(buyerWallet)
                            .amount(amountToBePaid)
                            .order(order)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("REFUND_COMPLETED");
                    order.setStatus("REFUND");
                    orderRepo.save(order);
                }
            }
        }

        // ẩn bài đăng khi hết hạn
        List<Post> posts = postRepo.findAll();
        for (Post post : posts){
            if (post.getPostStatus().equalsIgnoreCase("active") && post.getExpDate().equals(LocalDate.now())){
                post.setPostStatus("expired");
                postRepo.save(post);
                List<String> fcmKey1 = new ArrayList<>();
                if (!post.getUser().getFcmKey().isEmpty() && post.getUser().getFcmKey() != null) {
                    fcmKey1.add(post.getUser().getFcmKey());
                }
                if (!fcmKey1.isEmpty() || fcmKey1.size() > 0) { // co key
                    // pushnoti
                    PnsRequest pnsRequest = new PnsRequest(fcmKey1, "Bài đăng hết hạn",
                            "Bạn có bài đăng hết hạn vui lòng gia hạn thêm để hiển thị");
                    fcmService.pushNotification(pnsRequest);
                }
            }
        }
    }
}
