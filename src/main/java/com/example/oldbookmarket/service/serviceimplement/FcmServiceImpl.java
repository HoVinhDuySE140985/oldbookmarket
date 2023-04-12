package com.example.oldbookmarket.service.serviceimplement;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.oldbookmarket.dto.request.PnsRequest;
import com.example.oldbookmarket.service.FcmService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

@Service
public class FcmServiceImpl implements FcmService {
    Logger logger = LoggerFactory.getLogger(FcmServiceImpl.class);

    private FirebaseApp firebaseApp;

    @PostConstruct
	public void initialize() {
		try {
			java.io.File file = ResourceUtils
					.getFile("classpath:fir-fcm-8d942-firebase-adminsdk-vbl92-892b0e9574.json");
			FileInputStream serviceAccount = new FileInputStream(file);
			logger.info("init");
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			if (FirebaseApp.getApps().isEmpty()) {
				this.firebaseApp = FirebaseApp.initializeApp(options);
			} else {
				this.firebaseApp = FirebaseApp.getInstance();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

    @Override
    public BatchResponse pushNotification(PnsRequest pnsRequest) {
        List<String> registrationTokens = Arrays.asList(
			    pnsRequest.getToken()
			);
		MulticastMessage message = MulticastMessage.builder()
				.setNotification(new Notification(pnsRequest.getTitle(), pnsRequest.getMessage()))
			    .putData("score", "850")
			    .addAllTokens(registrationTokens)
			    .build();
		BatchResponse response = null;
		try {
			response = FirebaseMessaging.getInstance().sendMulticast(message);
		} catch (FirebaseMessagingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("" + response.getSuccessCount());
		System.out.println("" + response);
		return response;
    }   



}
