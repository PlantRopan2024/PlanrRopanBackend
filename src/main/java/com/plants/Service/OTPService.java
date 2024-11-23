package com.plants.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OTPService {
	
	private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
    private final Map<String, String> otpData = new HashMap<>();
    private final Map<String, Long> otpTimestamps = new HashMap<>();
   
    public String generateOTP(String mobileNumber) {
        // Generate a random 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(999999));
        otpData.put(mobileNumber, otp);
        otpTimestamps.put(mobileNumber, System.currentTimeMillis());
        return otp;
    }
   
    

    public boolean validateOTP(String mobileNumber, String otp) {
    	
    	System.out.println("  hsap " + otpData);
        return otp.equals(otpData.get(mobileNumber));
    }
    
    
    public boolean verifyOtp(String mobileNumber, String otp) {
        if (!otpData.containsKey(mobileNumber)) {
            return false; // OTP not found for this mobile number
        }
        
        String storedOtp = otpData.get(mobileNumber);
        long otpTimestamp = otpTimestamps.get(mobileNumber);
        long currentTime = System.currentTimeMillis();

        // Check if OTP is expired
        if (currentTime - otpTimestamp > OTP_VALID_DURATION) {
            otpData.remove(mobileNumber);
            otpTimestamps.remove(mobileNumber);
            return false; // OTP expired
        }

        // Verify the OTP
        if (storedOtp.equals(otp)) {
            otpData.remove(mobileNumber); // Clear OTP after successful verification
            otpTimestamps.remove(mobileNumber);
            return true;
        } else {
            return false; // Invalid OTP
        }
    }

}
