package com.plants.customer.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.RequestWrapper;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.userDao;
import com.plants.customer.Service.LocationService;
import com.plants.entities.AgentMain;
import com.plants.entities.CusPersonalDetail;
import com.plants.entities.CusPlanDetails;
import com.plants.entities.CusRequestWrapper;
import com.plants.entities.CustomerMain;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.auth.oauth2.GoogleCredentials;

@RestController
@RequestMapping("/CusOrderController")
public class CusOrderController {
	
	
	@Autowired
	userDao userdao;
	
	 @Autowired
	 LocationService locationService;
	/* @PostMapping("/CreateOrder")
	 public ResponseEntity<Map<String, String>> CreateOrder(@RequestBody RequestWrapper request) {     
		 Map<String, String> response = new HashMap<>();  
		
		
	     return ResponseEntity.ok(response);
	 }   */
	 
/*	 @PostMapping("/CreateOrder")
	 public ResponseEntity<Map<String, String>> CreateOrderCus( @RequestBody CusRequestWrapper request) {
	        // Extract data from the request
	        List<CusPersonalDetail> objectDetails = request.getCusPersonalDetail();
	        List<CusPlanDetails> planDetails = request.getPlans();
	        
	        System.out.println(" personal detail " + objectDetails.toString());
	        System.out.println(" plan detail " + planDetails.toString());
	        // Process the extracted data
	        
	        List<AgentMain> activeaagent = this.userdao.activeAgent();
	        System.out.println("activeaagent  size " + activeaagent.size());
	        System.out.println("activeaagent  " + activeaagent.toString());
	        
	       
	        CusPersonalDetail customerDetails = objectDetails.get(0);
	        
	       if(!activeaagent.isEmpty()) {
	    	   for(AgentMain agent : activeaagent) {
		        	if(agent.isActiveAgent() == true ) {
		        		
		        		boolean findDistance = this.locationService.isWithinRange(customerDetails.getLatitude(), customerDetails.getLoggitude(), agent.getLatitude(), agent.getLongitude());
		        		
		        	} 
		        }
	       }
	       
	        
	       
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Data received successfully");

	        return ResponseEntity.ok(response);
	}   */
	 
/*	 @PostMapping("/CreateOrder")
	 public ResponseEntity<Map<String, String>> CreateOrderCus(@RequestBody CusRequestWrapper request) {
	     // Extract data from the request
	     List<CusPersonalDetail> objectDetails = request.getCusPersonalDetail();
	     List<CusPlanDetails> planDetails = request.getPlans();
	     
	     System.out.println("personal detail " + objectDetails.toString());
	     System.out.println("plan detail " + planDetails.toString());
	     
	     // Fetch the list of active agents
	     List<AgentMain> activeAgents = this.userdao.activeAgent();
	     System.out.println("activeAgents size " + activeAgents.size());
	     System.out.println("activeAgents " + activeAgents.toString());

	     // Get customer details
	     CusPersonalDetail customerDetails = objectDetails.get(0);
	     
	     // Variables to keep track of the closest agent
	     AgentMain closestAgent = null;
	     double shortestDistance = Double.MAX_VALUE;  // Initialize with a large value
	     
	     // If there are active agents
	     if (!activeAgents.isEmpty()) {
	         for (AgentMain agent : activeAgents) {
	             if (agent.isActiveAgent()) {
	                 // Calculate the distance between customer and agent
	                 double distance = this.locationService.calculateDistance(
	                     customerDetails.getLatitude(), customerDetails.getLoggitude(),
	                     agent.getLatitude(), agent.getLongitude()
	                 );
	                 System.out.println("Distance to agent " + agent.getFirstName() + ": " + distance + " km");
	                 
	                 // Update closest agent if this agent is closer
	                 if (distance < shortestDistance) {
	                     shortestDistance = distance;
	                     closestAgent = agent;
	                 }
	             }
	         }
	     }

	     // Output the closest agent details
	     if (closestAgent != null) {
	         System.out.println("Closest agent ID: " + closestAgent.getFirstName());
	         System.out.println("Shortest distance: " + shortestDistance + " km");
	     } else {
	         System.out.println("No active agents within range.");
	     }
	     
	     // Return a response
	     Map<String, String> response = new HashMap<>();
	     response.put("message", "Data received successfully");
	     if (closestAgent != null) {
	         response.put("closestAgent", "Agent ID: " + closestAgent.getFirstName());
	         response.put("distance", String.valueOf(shortestDistance) + " km");
	     } else {
	         response.put("closestAgent", "No agents found in range");
	     }

	     return ResponseEntity.ok(response);
	 }   */
	 
	 @PostMapping("/CreateOrder")
	 public ResponseEntity<Map<String, Object>> CreateOrderCus(@RequestBody CusRequestWrapper request) {
	     // Extract data from the request
	     List<CusPersonalDetail> objectDetails = request.getCusPersonalDetail();
	     List<CusPlanDetails> planDetails = request.getPlans();
	     String notify="";
	     
	     System.out.println("personal detail " + objectDetails.toString());
	     System.out.println("plan detail " + planDetails.toString());
	     
	     // Fetch the list of active agents
	     List<AgentMain> activeAgents = this.userdao.activeAgent();
	     System.out.println("activeAgents size " + activeAgents.size());
	     System.out.println("activeAgents " + activeAgents.toString());

	     // Get customer details
	     CusPersonalDetail customerDetails = objectDetails.get(0);
	     
	     // List to keep track of agents within 5 km range
	     List<AgentMain> agentsWithinRange = new ArrayList<>();
	     
	     // If there are active agents
	     if (!activeAgents.isEmpty()) {
	         for (AgentMain agent : activeAgents) {
	             if (agent.isActiveAgent()) {
	                 // Check if the agent is within the 5 km range
	                 boolean isWithinRange = this.locationService.isWithinRange(customerDetails.getLatitude(), customerDetails.getLoggitude(), agent.getLatitude(), agent.getLongitude()
	                 );
	                 
	                 if (isWithinRange) {
	                     // Add agent to the list if they are within the range
	                     agentsWithinRange.add(agent);
	                     System.out.println("Agent within range: " + agent.getFirstName());
	                     
	                     
	                     // notification send firebase with help 
	                     notify=  sendNotificationToAgent(agent, "You have a new order nearby", "Please check the app for details.");
	                    System.out.println(" notify----" + notify);
	                 }
	             }
	         }
	     }

	     // Prepare the response
	     Map<String, Object> response = new HashMap<>();
	     response.put("message", "Data received successfully");

	     if (!agentsWithinRange.isEmpty()) {
	         System.out.println("Total agents within 5 km range: " + agentsWithinRange.size());
	         
	         response.put("notify", notify);
	         response.put("agentsWithinRange", agentsWithinRange);  // Send the list of agents within range
	     } else {
	         System.out.println("No agents found within 5 km range.");
	         response.put("agentsWithinRange", "No agents found within range");
	     }

	     return ResponseEntity.ok(response);
	 }
	 
	 
	 private String sendNotificationToAgent(AgentMain agent, String title, String message) {
		    String response = "";
		    try {
		        // Create the message to send
		        Message firebaseMessage = Message.builder()
		            .setToken(agent.getFcmTokenAgent())  // Use the agent's FCM token here
		            .setNotification(Notification.builder()
		                .setTitle(title)
		                .setBody(message)
		                .build())
		            .putData("agentId", String.valueOf(agent.getAgentIDPk())) // Add additional data if needed
		            .putData("action", "ACCEPT_REJECT") // Add action to distinguish the type of notification
		            .build();

		        // Send the message via Firebase
		        response = FirebaseMessaging.getInstance().send(firebaseMessage);
		        System.out.println("Successfully sent message: " + response);
		    } catch (FirebaseMessagingException e) {
		        e.printStackTrace();
		    }
		    return response;
		}
}
