package com.plants.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SmsService {

    private static final Logger logger = Logger.getLogger(SmsService.class.getName());

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    @jakarta.annotation.PostConstruct
    public void init() {
        if (accountSid == null || authToken == null || fromNumber == null) {
            logger.severe("Twilio properties are not set. Please check your application properties.");
            throw new IllegalStateException("Twilio properties are not set.");
        }

        try {
            Twilio.init(accountSid, authToken);
            logger.info("Twilio initialized with account SID: " + accountSid);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize Twilio", e);
            throw new IllegalStateException("Failed to initialize Twilio", e);
        }
    }

    public void sendOtp(String to, String otp) {
        try {
            Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromNumber),
                "Your OTP is: " + otp
            ).create();

            logger.info("OTP sent successfully. Message SID: " + message.getSid());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send OTP", e);
            throw new RuntimeException("Failed to send OTP", e);
        }
    }
}

