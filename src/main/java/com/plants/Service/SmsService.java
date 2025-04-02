package com.plants.Service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SmsService {

    private static final Logger logger = Logger.getLogger(SmsService.class.getName());
    
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

