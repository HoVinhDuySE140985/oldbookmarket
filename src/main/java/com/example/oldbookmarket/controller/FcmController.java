package com.example.oldbookmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.service.FcmService;
import com.google.firebase.messaging.BatchResponse;

@RestController
@RequestMapping("/api/fcm")
public class FcmController {
    
    
	 	@Autowired
         private FcmService fcmService;
 
         @PostMapping("/send-notification")
         public ResponseEntity<BatchResponse> sendSampleNotification(@RequestBody PnsRequest pnsRequest) {
             String token = pnsRequest.getToken();
             String title = pnsRequest.getTitle();
             String message = pnsRequest.getMessage();
             return new ResponseEntity<BatchResponse>(fcmService.pushNotification(pnsRequest), HttpStatus.OK);
         }
         
    
}
