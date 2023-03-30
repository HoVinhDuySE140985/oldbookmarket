package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.entity.Address;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.AddressRepo;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.PostRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public OrderResponseDTO createNewOrder(AddOrderRequestDTO addOrderRequestDTO) {
//        BigDecimal comparePrice = BigDecimal.valueOf(500000);
        OrderResponseDTO orderResponseDTO = null;
        try {
//            User user = userRepo.findById(addOrderRequestDTO.getUserId()).get();
            Post post = postRepo.findById(addOrderRequestDTO.getPostId()).get();
            post.setPostStatus("deactivate");
            postRepo.save(post);
//            Address address = addressRepo.findAddressByIdAndUserId(addOrderRequestDTO.getAddressId(), addOrderRequestDTO.getUserId());
//            String shipadrress = address.getCity() + "," + address.getProvince() + "," + address.getDistrict() + "," + address.getWard() + "," + address.getStreet();
            if (post.getForm().equalsIgnoreCase("bán")) {
                Order order = Order.builder()
//                        .shipAddress(shipadrress)
                        .post(post)
                        .orderDate(addOrderRequestDTO.getOrderDate())
                        .amount(addOrderRequestDTO.getAmount())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("MOMO")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("processing")
//                        .user(user)
                        .build();
                orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
//                        .userId(order.getUser().getId())
                        .status(order.getStatus())
                        .build();
            } else {
                Order order = Order.builder()
//                        .shipAddress(shipadrress)
                        .post(post)
                        .orderDate(addOrderRequestDTO.getOrderDate())
                        .amount(addOrderRequestDTO.getAmount())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("MOMO")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("processing")
//                        .user(user)
                        .build();
                orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
//                        .userId(order.getUser().getId())
                        .status(order.getStatus())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderResponseDTO;
    }

    @Override
    public Order converOrderStatus(Long orderId) {
        Order order = orderRepo.getById(orderId);
        try {
            if (order.getStatus().equalsIgnoreCase("processing")) {
                order.setStatus("packed");
                return orderRepo.save(order);
            }
            if (order.getStatus().equalsIgnoreCase("packed")) {
                order.setStatus("delivery");
                return orderRepo.save(order);
            }
            if (order.getStatus().equalsIgnoreCase("delivery")) {
                order.setStatus("receive");
                return orderRepo.save(order);
            }
            if (order.getStatus().equalsIgnoreCase("receive")) {
                order.setStatus("complete");
                return orderRepo.save(order);
            }
            orderRepo.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
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
    public Order cancelOrder(Long orderId) {
        Order order = new Order();

        order = orderRepo.findById(orderId).get();
        if (order.getStatus().equalsIgnoreCase("processing")) {
            Post post = postRepo.findById(order.getPost().getId()).get();
            post.setPostStatus("active");
            postRepo.save(post);
            order.setStatus("cancel");
            orderRepo.save(order);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(200), "Huy Đơn Không Thành Công");

        }

        return order;
    }

    @Override
    public OrderResponseDTO addToOrder(Long orderId, Long userId,Long addressId) {
        OrderResponseDTO orderResponseDTO = null;
        try {
            Order order = orderRepo.findById(orderId).get();
            if (order != null){
                User user = userRepo.findById(userId).get();
                Address address = addressRepo.findById(addressId).get();
                String shipadrress = address.getCity() + "," + address.getProvince() + "," + address.getDistrict() + "," + address.getWard() + "," + address.getStreet();
                order.setUser(user);
                order.setShipAddress(shipadrress);
                order.setPaymentStatus("PAID");
                order = orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
                        .userId(order.getUser().getId())
                        .status(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .url("https://www.youtube.com/")
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderResponseDTO;
    }
}
