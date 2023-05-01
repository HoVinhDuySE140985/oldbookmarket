package com.example.oldbookmarket.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String senderEmail;
    private String receiverEmail;
    private String message;
    private String date;
    private Status status;
}
