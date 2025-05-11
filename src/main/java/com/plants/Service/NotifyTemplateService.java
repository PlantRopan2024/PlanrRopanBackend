package com.plants.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.plants.Dao.NotifyTemplateRepo;
import com.plants.entities.NotifyTemplate;

@Service
public class NotifyTemplateService {
	
	@Autowired
	private NotifyTemplateRepo notifyTemplateRepo;
	
	
	public ResponseEntity<Map<String, Object>> addNotifyTemplate(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			String title = request.get("title");
			String messageText = request.get("messageText");

		//	Role findRole = this.roleRepo.getRoleName(roleName);
//			if(Objects.nonNull(findRole)) {
//				response.put("message", "Role Name Already Added");
//				response.put("status", false);
//				return ResponseEntity.ok(response);
//			}
			NotifyTemplate notifyTemplate = new NotifyTemplate();
			notifyTemplate.setTemplateId(generateNotifyTemplateId());
			notifyTemplate.setTitle(title);
			notifyTemplate.setActive(false);
			notifyTemplate.setMessageText(messageText);
			notifyTemplate.setCreatedAt(LocalDateTime.now());
			notifyTemplate.setUpdatedAt(LocalDateTime.now());
			this.notifyTemplateRepo.save(notifyTemplate);
			response.put("message", " Notify Template Added");
			response.put("status", true);
		}catch(Exception e){
			e.printStackTrace();
			response.put("message", "Something Went Wrong!");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private String generateNotifyTemplateId() {
		String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String notifyFormat = "NOTIFY" + currentDate + "000";
		// Fetch the latest order ID for today
		Integer lastSequenceNumber = notifyTemplateRepo.getMaxSequenceTemplateId();
		System.out.println(" Order  Number last digit -- " + lastSequenceNumber);
		int sequenceNumber = 1;
		String notifyIdGenerated = "";
		if (lastSequenceNumber != null) {
			sequenceNumber = lastSequenceNumber + 1;
			notifyIdGenerated = notifyFormat + sequenceNumber;
		} else {
			notifyIdGenerated = notifyFormat + sequenceNumber;
		}
		return notifyIdGenerated;
	}
	

	public ResponseEntity<Map<String, Object>> ViewNotifyTemplate() {
		Map<String,Object> response = new HashMap<String, Object>();
		List<NotifyTemplate> notifyTemplates = this.notifyTemplateRepo.getAllTemplate();
		if(notifyTemplates.isEmpty()) {
			response.put("data", notifyTemplates);
			response.put("status", false);
		}else {
			response.put("data", notifyTemplates);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getApprovedNotifyTemplate(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			NotifyTemplate notifyTemplate = this.notifyTemplateRepo.getNotifyPk(primaryKey);
			notifyTemplate.setActive(true);
			notifyTemplate.setUpdatedAt(LocalDateTime.now());
			this.notifyTemplateRepo.save(notifyTemplate);
			response.put("message", " Notify Template Approved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedNotifyTemplate(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			NotifyTemplate notifyTemplate = this.notifyTemplateRepo.getNotifyPk(primaryKey);
			notifyTemplate.setActive(false);
			notifyTemplate.setUpdatedAt(LocalDateTime.now());
			this.notifyTemplateRepo.save(notifyTemplate);
			response.put("message", "Notify Template DisApproved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> updateNotifyTemplate(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primarykey");
			String title = request.get("title");
			String messageText = request.get("messageText");

			NotifyTemplate notifyTemplate = this.notifyTemplateRepo.getNotifyPk(primaryKey);
			notifyTemplate.setTitle(title);
			notifyTemplate.setMessageText(messageText);
			notifyTemplate.setUpdatedAt(LocalDateTime.now());
			this.notifyTemplateRepo.save(notifyTemplate);
			response.put("message", "Notify Template Updated");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

}
