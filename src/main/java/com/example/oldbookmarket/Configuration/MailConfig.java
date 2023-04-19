//package com.example.oldbookmarket.Configuration;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//    private final static String MY_EMAIL ="hovinhduy147@gmail.com";
//    private final static String MY_PASSWORD ="duy0389369367";
//
//    @Bean
//    public JavaMailSender mailSender(){
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername(MY_EMAIL);
//        mailSender.setPassword(MY_PASSWORD);
//
//        return mailSender;
//    }
//
//}
