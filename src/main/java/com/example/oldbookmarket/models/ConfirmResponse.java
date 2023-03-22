package com.example.oldbookmarket.models;

import com.example.oldbookmarket.enumcode.ConfirmRequestType;

public class ConfirmResponse extends Response {
    private Long amount;
    private Long transId;
    private String requestId;
    private ConfirmRequestType requestType;
}
