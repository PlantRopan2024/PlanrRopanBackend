package com.plants.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.FeedBackRepository;
import com.plants.entities.FeedBackPlantRopan;

@RestController
@RequestMapping("/feedBackCont")
public class FeedbackController {
	
	@Autowired
	private FeedBackRepository feedBackRepository;
	
	@PostMapping("/SendFeedBack")
    public ResponseEntity<Map<String, Object>> receiveFeedback(@RequestBody Map<String, Object> feedbackData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = (String) feedbackData.get("name");
            String email = (String) feedbackData.get("email");
            String phone = (String) feedbackData.get("phone");
            String message = (String) feedbackData.get("message");

            FeedBackPlantRopan feedBackPlantRopan = new FeedBackPlantRopan();
            feedBackPlantRopan.setName(name);
            feedBackPlantRopan.setEmail(email);
            feedBackPlantRopan.setPhone(phone);
            feedBackPlantRopan.setMessage(message);

            feedBackRepository.save(feedBackPlantRopan);

            response.put("message", "Thanks for sharing your feedback!");
            response.put("status", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // log it if needed
            response.put("message", "An error occurred while submitting feedback.");
            response.put("status", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
