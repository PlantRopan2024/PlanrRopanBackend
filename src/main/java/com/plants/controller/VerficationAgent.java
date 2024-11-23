package com.plants.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.plants.Dao.userDao;
import com.plants.entities.AgentMain;

@RestController
public class VerficationAgent {
	
	@Autowired
	userDao userdao;
	
	@GetMapping("/verificationPendingAgent")
	@ResponseBody
	public List<AgentMain> verificationPendingAgent() {
		List<AgentMain> getverifcationRecord= this.userdao.getpendingVerif();
		return getverifcationRecord;
	}
	
	@GetMapping("/getVerifiedAgent")
	@ResponseBody
	public List<AgentMain> getVerifiedAgent() {
		List<AgentMain> getVerifiedAgent= this.userdao.getVerified();
		return getVerifiedAgent;
	}
	
	
	
	@PostMapping("/findDetailAgent")
	 @ResponseBody
	    public AgentMain findDetailAgent(@RequestBody String AgentIDPk) {
		AgentMain getdetailRecord = this.userdao.findAgentID(AgentIDPk);
		 System.out.println("aaaaaaa" + getdetailRecord.getSelfieImg());
		
		    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
		                        .path("/images/")
		                        .path(getdetailRecord.getSelfieImg())
		                        .toUriString();
		                        
		    System.out.println("imsa" + imageUrl);
			getdetailRecord.setSelfieImg(imageUrl);
			System.out.println(" getdetailRecord   ---  " + getdetailRecord);
	        return getdetailRecord;
	    }
}
