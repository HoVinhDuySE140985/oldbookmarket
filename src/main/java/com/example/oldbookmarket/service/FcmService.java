package com.example.oldbookmarket.service;

import com.example.oldbookmarket.dto.request.PnsRequest;
import com.google.firebase.messaging.BatchResponse;

public interface FcmService {
    public BatchResponse pushNotification(PnsRequest pnsRequest);
}
