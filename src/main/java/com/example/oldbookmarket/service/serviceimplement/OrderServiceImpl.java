package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.controller.PaymentController;
import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Ref;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    PostRepo postRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    RefundRepo refundRepo;

    @Autowired
    PaymentService paymentService;

    @Override
    public ResponseEntity<MomoResponse> createNewOrderWithMomo(AddOrderRequestDTO addOrderRequestDTO) {
        ResponseEntity<MomoResponse> response = null;
        try {
            String code = Utilities.randomAlphaNumeric(10);
//            response = paymentService.getPaymentMomo(code, addOrderRequestDTO.getAmount());
            User user = userRepo.findById(addOrderRequestDTO.getUserId()).get();
            Post post = postRepo.findById(addOrderRequestDTO.getPostId()).get();
            post.setPostStatus("deactivate");
            postRepo.save(post);
            if (post.getForm().equalsIgnoreCase("bán")) {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(addOrderRequestDTO.getShipAddress())
                        .orderDate(LocalDate.now())
                        .post(post)
                        .codeOrder(code)
                        .amount(post.getPrice())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("VÍ MOMO")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .paymentStatus("PAID")
                        .build();
                orderRepo.save(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    @Transactional
    public OrderResponseDTO createNewOrderWithMyWallet(AddOrderRequestDTO addOrderRequestDTO) {
        OrderResponseDTO orderResponseDTO = null;
        User user = userRepo.findById(addOrderRequestDTO.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Post post = postRepo.findById(addOrderRequestDTO.getPostId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        post.setPostStatus("deactivate");
        postRepo.save(post);
        if (post.getForm().equalsIgnoreCase("bán")) {
            // kiểm tra tiền trong ví người mua neu ko du thi gui thong bao nap tien va tiep tuc
            // neu du tien thi tao don hang va tru tien cua nguoi mua chuyen vao tk admin
            // sau khi ket giao dich chuyen tien tu TK admin vao tai khoan nguoi ban - hoa hong
            Wallet walletBuyer = walletRepo.findById(user.getId()).get();
            if (walletBuyer.getAmount().compareTo(post.getPrice()) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(200), "VÍ CỦA BẠN KHÔNG ĐỦ TIỀN VUI LONG NẠP TIỀN VÀ THỬ LẠI");
            } else {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(addOrderRequestDTO.getShipAddress())
                        .orderDate(LocalDate.now())
                        .post(post)
                        .amount(post.getPrice())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("VÍ CỦA TÔI")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .paymentStatus("PAID")
                        .build();
                order = orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .userId(order.getUser().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
                        .status(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .build();
                // tru tien trong vi va luu lai
                walletBuyer.setAmount(walletBuyer.getAmount().subtract(order.getAmount()));
                walletRepo.save(walletBuyer);
                // cong tien vao vi admin
                User admin = userRepo.findUserByRole_Id(1L);
                Wallet adminWallet = walletRepo.findById(admin.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                adminWallet.setAmount(adminWallet.getAmount().add(order.getAmount()));
                walletRepo.save(adminWallet);
                // neu don hang co trang thai thanh cong thi
                //tự động gọi hàm sheduled để check và chuyển tiền vào ví người bán
            }
        } else {
            Wallet walletBuyer = walletRepo.findById(user.getId()).get();
            if (walletBuyer.getAmount().compareTo(post.getInitPrice()) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(200), "VÍ CỦA BẠN KHÔNG ĐỦ TIỀN VUI LONG NẠP TIỀN VÀ THỬ LẠI");
            } else {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(addOrderRequestDTO.getShipAddress())
                        .orderDate(LocalDate.now())
                        .post(post)
                        .amount(post.getInitPrice())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("VÍ CỦA TÔI")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .paymentStatus("DEPOSITED")
                        .build();
                order = orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .userId(order.getUser().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
                        .status(order.getStatus())
                        .form(post.getForm())
                        .paymentStatus(order.getPaymentStatus())
                        .build();
                // tru tien trong vi va luu lai
                walletBuyer.setAmount(walletBuyer.getAmount().subtract(order.getAmount()));
                walletRepo.save(walletBuyer);
                // cong tien vao vi admin
                User admin = userRepo.findUserByRole_Id(1L);
                Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                adminWallet.setAmount(adminWallet.getAmount().add(order.getAmount()));
                walletRepo.save(adminWallet);
                // neu don hang đến tay người nhận và sau khi người nhận xác nhận ngày gửi lại và người bán xác nhận đã nhận được hàng
                //tự động gọi hàm sheduled để check và chuyển tiền vào ví người mua đồng thời trừ tiền hoa hồng
            }
        }
        return orderResponseDTO;
    }

    @Override
    public Boolean converOrderStatus(Long orderId) {
        Order order = orderRepo.findById(orderId).get();
        try {
            if (order.getStatus().equalsIgnoreCase("WaitingForConfirmation")) {
                order.setStatus("processing");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("processing")) {
                order.setStatus("delivery");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("delivery")) {
                order.setStatus("complete");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("resent")) {
                order.setStatus("received");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("complete")) {
                order.setStatus("resent");
                orderRepo.save(order);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Order> getAllOrder(Long userId, String orderStatus) {
        List<Order> orderList = new ArrayList<>();
        try {
            orderList = orderRepo.findAllByStatusAndUser_Id(orderStatus, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public Order cancelOrder(Long orderId, String cancelReason) {
        Order order = orderRepo.findById(orderId).get();
        if (order.getStatus().equalsIgnoreCase("processing")) {
            User userReceivered = userRepo.findById(order.getUser().getId()).get();
            Post post = postRepo.findById(order.getPost().getId()).get();
            post.setPostStatus("active");
            postRepo.save(post);
            order.setStatus("cancel");
            order.setCancelReason(cancelReason);
            orderRepo.save(order);
            Refund refund = Refund.builder()
                    .order(order)
                    .createAt(LocalDate.now())
                    .amount(order.getAmount())
                    .user(userReceivered)
                    .build();
            refundRepo.save(refund);

        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(200), "Huy Đơn Không Thành Công");
        }
        return order;
    }

//    @Override
//    public OrderResponseDTO addToOrder(Long orderId, Long userId, Long addressId) {
//        OrderResponseDTO orderResponseDTO = null;
//        try {
//            Order order = orderRepo.findById(orderId).get();
//            if (order != null) {
//                User user = userRepo.findById(userId).get();
//                Address address = addressRepo.findById(addressId).get();
//                String shipadrress = address.getCity() + "," + address.getProvince() + "," + address.getDistrict() + "," + address.getWard() + "," + address.getStreet();
//                order.setUser(user);
//                order.setShipAddress(shipadrress);
//                order.setPaymentStatus("PAID");
//                order = orderRepo.save(order);
//                orderResponseDTO = OrderResponseDTO.builder()
//                        .orderId(order.getId())
//                        .postId(order.getPost().getId())
//                        .shipAddress(order.getShipAddress())
//                        .orderDate(order.getOrderDate())
//                        .amount(order.getAmount())
//                        .note(order.getNote())
//                        .paymentMethod(order.getPaymentMethod())
//                        .deliveryMethod(order.getDeliveryMethod())
//                        .userId(order.getUser().getId())
//                        .status(order.getStatus())
//                        .paymentStatus(order.getPaymentStatus())
//                        .build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return orderResponseDTO;
//    }

    @Override
    public Boolean updateResentDate(Long orderId, String resentDate) {
        Order order = orderRepo.findById(orderId).get();
        try {
            if (order.getStatus().equalsIgnoreCase("complete")) {
                order.setResentDate(LocalDate.parse(resentDate));
                order.setStatus("resent");
                orderRepo.save(order);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllSellOrder(Long userId, String status) {
        List<Order> orderList = new ArrayList<>();
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getPost().getUser().getId().equals(userId)) {
                    if (status.equalsIgnoreCase("null")) {
                        OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                .order_date(order.getOrderDate())
                                .cancelReason(order.getCancelReason())
                                .resentDate(order.getResentDate())
                                .postImage(order.getPost().getImageUrl())
                                .title(order.getPost().getTitle())
                                .amount(order.getAmount())
                                .deliveryMethod(order.getDeliveryMethod())
                                .status(order.getStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .receiverName(order.getUser().getName())
                                .phoneNumber(order.getUser().getPhoneNumber())
                                .shipAddress(order.getShipAddress())
                                .paymentStatus(order.getPaymentStatus())
                                .orderId(order.getId())
                                .build();
                        orderHistoryResponseDTOS.add(history);
                    } else {
                        if (order.getStatus().equalsIgnoreCase(status)) {
                            OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                    .order_date(order.getOrderDate())
                                    .cancelReason(order.getCancelReason())
                                    .resentDate(order.getResentDate())
                                    .postImage(order.getPost().getImageUrl())
                                    .title(order.getPost().getTitle())
                                    .amount(order.getAmount())
                                    .deliveryMethod(order.getDeliveryMethod())
                                    .status(order.getStatus())
                                    .paymentMethod(order.getPaymentMethod())
                                    .receiverName(order.getUser().getName())
                                    .phoneNumber(order.getUser().getPhoneNumber())
                                    .shipAddress(order.getShipAddress())
                                    .paymentStatus(order.getPaymentStatus())
                                    .orderId(order.getId())
                                    .build();
                            orderHistoryResponseDTOS.add(history);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllBoughtOrder(Long userId, String status) {
        List<Order> orderList = new ArrayList<>();
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getUser().getId().equals(userId)) {
                    if (status.equalsIgnoreCase("null")) {
                        OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                .order_date(order.getOrderDate())
                                .cancelReason(order.getCancelReason())
                                .resentDate(order.getResentDate())
                                .postImage(order.getPost().getImageUrl())
                                .title(order.getPost().getTitle())
                                .amount(order.getAmount())
                                .deliveryMethod(order.getDeliveryMethod())
                                .status(order.getStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .shipAddress(order.getShipAddress())
                                .paymentStatus(order.getPaymentStatus())
                                .orderId(order.getId())
                                .form(order.getPost().getForm())
                                .sellerName(order.getPost().getUser().getName())
                                .build();
                        orderHistoryResponseDTOS.add(history);
                    } else {
                        if (order.getStatus().equalsIgnoreCase(status)) {
                            OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                    .order_date(order.getOrderDate())
                                    .cancelReason(order.getCancelReason())
                                    .resentDate(order.getResentDate())
                                    .postImage(order.getPost().getImageUrl())
                                    .title(order.getPost().getTitle())
                                    .amount(order.getAmount())
                                    .deliveryMethod(order.getDeliveryMethod())
                                    .status(order.getStatus())
                                    .paymentMethod(order.getPaymentMethod())
                                    .shipAddress(order.getShipAddress())
                                    .paymentStatus(order.getPaymentStatus())
                                    .form(order.getPost().getForm())
                                    .orderId(order.getId())
                                    .sellerName(order.getPost().getUser().getName())
                                    .build();
                            orderHistoryResponseDTOS.add(history);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllOrderByStatus(Long userId, String status) {
        List<Order> orderList = null;
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getPost().getUser().getId().equals(userId) && order.getStatus().equalsIgnoreCase(status)) {
                    OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                            .order_date(order.getOrderDate())
                            .cancelReason(order.getCancelReason())
                            .resentDate(order.getResentDate())
                            .postImage(order.getPost().getImageUrl())
                            .title(order.getPost().getTitle())
                            .amount(order.getAmount())
                            .deliveryMethod(order.getDeliveryMethod())
                            .status(order.getStatus())
                            .paymentMethod(order.getPaymentMethod())
                            .shipAddress(order.getShipAddress())
                            .paymentStatus(order.getPaymentStatus())
                            .orderId(order.getId())
                            .build();
                    orderHistoryResponseDTOS.add(history);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }
}
