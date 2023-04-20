package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.response.EmailResponseDTO;
import com.example.oldbookmarket.service.serviceinterface.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public String sendSimpleMail(EmailResponseDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(dto.getEmail());
            message.setSubject(dto.getSubject());
            message.setText(dto.getMassage());
            this.javaMailSender .send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Mail Sent Successfully...";
    }

}
