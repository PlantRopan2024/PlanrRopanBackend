package com.plants.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.plants.Dao.userDao;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;

@RestController
public class VerficationAgent {

	@Autowired
	userDao userdao;

	@GetMapping("/verificationPendingAgent")
	@ResponseBody
	public List<AgentMain> verificationPendingAgent() {
		List<AgentMain> getverifcationRecord = this.userdao.getpendingVerif();
		return getverifcationRecord;
	}

	@GetMapping("/getApprovedAgent")
	@ResponseBody
	public List<AgentMain> getVerifiedAgent() {
		List<AgentMain> getapprovedAgent = this.userdao.getApprovedAgent();
		return getapprovedAgent;
	}

	@PostMapping("/findDetailAgent")
	@ResponseBody
	public AgentMain findDetailAgent(@RequestBody String AgentIDPk) {
		AgentMain getdetailRecord = this.userdao.findAgentID(AgentIDPk);
		
		
		//getdetailRecord.setBankPassBookImage(Utils.findImgPath(getdetailRecord.getBankPassBookImage()));
		//getdetailRecord.setSelfieImg(Utils.findImgPath(getdetailRecord.getSelfieImg()));
		//getdetailRecord.setAadharImgFrontSide(Utils.findImgPath(getdetailRecord.getAadharImgFrontSide()));
		//getdetailRecord.setAadharImgBackSide(Utils.findImgPath(getdetailRecord.getAadharImgBackSide()));
		
		System.out.println(" getdetailRecord   ---  " + getdetailRecord);
		return getdetailRecord;
	}

	@PostMapping("/approvedAgent")
	@ResponseBody
	public String agentApproved(@RequestBody Map<String, String> request) {
		String agentIDPkStr = request.get("agentIDPk");
		AgentMain agentMain = this.userdao.findAgentID(agentIDPkStr);
		this.userdao.updateAgentApproved(true,agentMain.getAgentIDPk());
		return null;
	}
	
	@PostMapping("/disApprovedAgent")
	@ResponseBody
	public String disApprovedAgent(@RequestBody Map<String, String> request) {
		String agentIDPkStr = request.get("agentIDPk");		
		AgentMain agentMain = this.userdao.findAgentID(agentIDPkStr);
		this.userdao.updateAgentApproved(false,agentMain.getAgentIDPk());
		return null;
	}
}
