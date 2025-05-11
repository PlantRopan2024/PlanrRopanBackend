package com.plants.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.plants.entities.AgentMain;

@Service
public class ExcelService {
	
	 public byte[] generateExcel(AgentMain agent) {
	        try (Workbook workbook = new XSSFWorkbook(); 
	        	ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	            Sheet sheet = workbook.createSheet("Agent Data");

	            Row headerRow = sheet.createRow(0);
	            headerRow.createCell(0).setCellValue("Agent ID");
		        headerRow.createCell(1).setCellValue("First Name");
		        headerRow.createCell(2).setCellValue("Last Name");
		        headerRow.createCell(3).setCellValue("Mobile");
		        headerRow.createCell(4).setCellValue("Gender");
		        headerRow.createCell(5).setCellValue("Address");
		        headerRow.createCell(6).setCellValue("Aadhar Number");
		        headerRow.createCell(7).setCellValue("Account Holder Name");
		        headerRow.createCell(8).setCellValue("Account Number");
		        headerRow.createCell(9).setCellValue("Bank Name");
		        headerRow.createCell(10).setCellValue("IFSC Code");
		        headerRow.createCell(10).setCellValue("IFSC Code");
		        headerRow.createCell(11).setCellValue("Onboarding Date");
		        headerRow.createCell(12).setCellValue("Approval Date");

	      
	            Row dataRow = sheet.createRow(1);
	            dataRow.createCell(0).setCellValue(agent.getAgentId());
	            dataRow.createCell(1).setCellValue(agent.getFirstName());
	            dataRow.createCell(2).setCellValue(agent.getLastName());
	            dataRow.createCell(3).setCellValue(agent.getMobileNumber());
	            dataRow.createCell(4).setCellValue(agent.getGender());
	            dataRow.createCell(5).setCellValue(agent.getAddress());
	            dataRow.createCell(6).setCellValue(agent.getAadhaarNumber());
	            dataRow.createCell(7).setCellValue(agent.getAccHolderName());
	            dataRow.createCell(8).setCellValue(agent.getAccNumber());
	            dataRow.createCell(9).setCellValue(agent.getBankName());
	            dataRow.createCell(10).setCellValue(agent.getIfscCode());
	            dataRow.createCell(11).setCellValue(agent.getCreatedAt());
	            dataRow.createCell(12).setCellValue(agent.getApprovedAt());


	            workbook.write(out);
	            return out.toByteArray();
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to generate Excel", e);
	        }
	    }
	 
	 public byte[] generateExcel(List<AgentMain> listAgent, String action) {
		    try (Workbook workbook = new XSSFWorkbook(); 
		         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

		        Sheet sheet = workbook.createSheet("Agent Data");

		        // Header Row
		        Row headerRow = sheet.createRow(0);
		        headerRow.createCell(0).setCellValue("Agent ID");
		        headerRow.createCell(1).setCellValue("First Name");
		        headerRow.createCell(2).setCellValue("Last Name");
		        headerRow.createCell(3).setCellValue("Mobile");
		        headerRow.createCell(4).setCellValue("Gender");
		        headerRow.createCell(5).setCellValue("Address");
		        headerRow.createCell(6).setCellValue("Aadhar Number");
		        headerRow.createCell(7).setCellValue("Account Holder Name");
		        headerRow.createCell(8).setCellValue("Account Number");
		        headerRow.createCell(9).setCellValue("Bank Name");
		        headerRow.createCell(10).setCellValue("IFSC Code");
		        headerRow.createCell(11).setCellValue("Onboarding Date");
		        headerRow.createCell(12).setCellValue("Approval Date");

		        int rowIdx = 1;
		        for (AgentMain agent : listAgent) {
		            Row dataRow = sheet.createRow(rowIdx++);
		            
		            dataRow.createCell(0).setCellValue(agent.getAgentId());
		            dataRow.createCell(1).setCellValue(agent.getFirstName());
		            dataRow.createCell(2).setCellValue(agent.getLastName());
		            dataRow.createCell(3).setCellValue(agent.getMobileNumber());
		            dataRow.createCell(4).setCellValue(agent.getGender());
		            dataRow.createCell(5).setCellValue(agent.getAddress());
		            dataRow.createCell(6).setCellValue(agent.getAadhaarNumber());
		            dataRow.createCell(7).setCellValue(agent.getAccHolderName());
		            dataRow.createCell(8).setCellValue(agent.getAccNumber());
		            dataRow.createCell(9).setCellValue(agent.getBankName());
		            dataRow.createCell(10).setCellValue(agent.getIfscCode());
		            dataRow.createCell(11).setCellValue(agent.getCreatedAt());
		            dataRow.createCell(12).setCellValue(agent.getApprovedAt());
		        }

		        workbook.write(out);
		        return out.toByteArray();
		    } catch (IOException e) {
		        throw new RuntimeException("Failed to generate Excel", e);
		    }
		}

}
