package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.service.serviceinterface.FcmService;
import com.example.oldbookmarket.service.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.oldbookmarket.dto.Message;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    FcmService fcmService;
    @Autowired
    UserService userService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        List<String> fcmKey = new ArrayList<>();
        User user = userService.findUserByName(message.getReceiverName());
        if (!user.getFcmKey().isEmpty() && user.getFcmKey() != null) {
            fcmKey.add(user.getFcmKey());
        }
        if (!fcmKey.isEmpty() || fcmKey.size() > 0) { // co key
            // pushnoti
            PnsRequest pnsRequest = new PnsRequest(fcmKey, "Bạn có tin nhắn mới",
                    "Mã đơn hàng của bạn là :" + message.getSenderName());
            fcmService.pushNotification(pnsRequest);
        }
        System.out.println(message.toString());
        return message;
    }
}
