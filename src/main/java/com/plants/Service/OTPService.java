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
        Random random = new Random();
        int otp;
        
        do {
            otp = random.nextInt(999999); // Generates a number between 0 and 999999
        } while (otp < 100000); // Ensures it is at least 100000 (6 digits)

        String otpString = String.valueOf(otp);
    	// String otpString= "123456";
        otpData.put(mobileNumber, otpString);
        otpTimestamps.put(mobileNumber, System.currentTimeMillis());
        
        return otpString;
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
