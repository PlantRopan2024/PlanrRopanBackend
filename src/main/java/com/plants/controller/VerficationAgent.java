package com.plants.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.userDao;
import com.plants.Service.ExcelService;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;

@RestController
public class VerficationAgent {

	@Autowired
	userDao userdao;
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	@Autowired
	private ExcelService excelService;

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
	
	@GetMapping("/downloadExcel/{agentId}")
	public ResponseEntity<byte[]> downloadExcel(@PathVariable int agentId) {
	    AgentMain agent = userdao.findAgentID(agentId);

	    byte[] excelData = excelService.generateExcel(agent);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    headers.setContentDispositionFormData("attachment", "agent_data.xlsx");

	    return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}
	
	@GetMapping("/downloadExcelPenVsApp")
	public ResponseEntity<byte[]> downloadExcel(@RequestParam String action) {
		 byte[] excelData = null ;
		HttpHeaders headers = new HttpHeaders();
		if(action.equals("PENDING")) {
			 List<AgentMain> pendingAgent = userdao.getpendingVerif();
			    excelData = excelService.generateExcel(pendingAgent, action); 
			    String filename = action + "_agent_data.xlsx";
			    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			    headers.setContentDispositionFormData("attachment", filename);
		}
		if(action.equals("APPROVED")) {
			 List<AgentMain> approvedAgent = userdao.getApprovedAgent();
			    excelData = excelService.generateExcel(approvedAgent, action);
			    String filename = action + "_agent_data.xlsx";
			    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			    headers.setContentDispositionFormData("attachment", filename);
		}
	    return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}

	@PostMapping("/findDetailAgent")
	@ResponseBody
	public AgentMain findDetailAgent(@RequestBody String AgentIDPk) {
		AgentMain getdetailRecord = this.userdao.findAgentID(AgentIDPk);
		return getdetailRecord;
	}
	
	@GetMapping("/downloadFile/{folderName}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String folderName,@PathVariable String fileName) {
        return Utils.getPathFileResponse(uploadDir,folderName, fileName);
    }

	@PostMapping("/approvedAgent")
	@ResponseBody
	public String agentApproved(@RequestBody Map<String, String> request) {
		String agentIDPkStr = request.get("agentIDPk");
		AgentMain agentMain = this.userdao.findAgentID(agentIDPkStr);
		agentMain.setAgentApproved(true);
		agentMain.setApprovedAt(LocalDateTime.now());
		this.userdao.save(agentMain);
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
