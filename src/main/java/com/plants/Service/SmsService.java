package com.plants.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

//    public void sendOtp(String to, String otp) {
//        try {
//            Message message = Message.creator(
//                new PhoneNumber(to),
//                new PhoneNumber(fromNumber),
//                "Your OTP is: " + otp
//            ).create();
//
//            logger.info("OTP sent successfully. Message SID: " + message.getSid());
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Failed to send OTP", e);
//            throw new RuntimeException("Failed to send OTP", e);
//        }
//    }
    
    public void sendOtp(String mobileNumber, String otp) {
        try {
        	// Remove '+' from the mobile number if present
            mobileNumber = mobileNumber.replace("+", "");
        	 String apiUrl = "http://cloud.smsindiahub.in/vendorsms/pushsms.aspx?"
                     + "APIKey=xuhxiqhebkyxUEfFeIF51g"
                     + "&msisdn=" + mobileNumber
                     + "&sid=AREPLY"
                     + "&msg=Your%20One%20Time%20Password%20is%20" + otp + ".%20Thanks%20SMSINDIAHUB"
                     + "&fl=0"
                     + "&gwid=2";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send OTP", e);
            throw new RuntimeException("Failed to send OTP", e);
        }
    }
}

