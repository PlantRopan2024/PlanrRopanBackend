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
	            headerRow.createCell(0).setCellValue("First Name");
	            headerRow.createCell(1).setCellValue("Last Name");
	            headerRow.createCell(2).setCellValue("Mobile");
	            headerRow.createCell(3).setCellValue("Gender");
	            headerRow.createCell(4).setCellValue("Address");
	            headerRow.createCell(5).setCellValue("Aadhar Number");
	            headerRow.createCell(6).setCellValue("Account Holder Name");
	            headerRow.createCell(7).setCellValue("Account Number");
	            headerRow.createCell(8).setCellValue("Bank Name");
	            headerRow.createCell(9).setCellValue("IFSC Code");
	      
	            Row dataRow = sheet.createRow(1);
	            dataRow.createCell(0).setCellValue(agent.getFirstName());
	            dataRow.createCell(1).setCellValue(agent.getLastName());
	            dataRow.createCell(2).setCellValue(agent.getMobileNumber());
	            dataRow.createCell(3).setCellValue(agent.getGender());
	            dataRow.createCell(4).setCellValue(agent.getAddress());
	            dataRow.createCell(5).setCellValue(agent.getAadhaarNumber());
	            dataRow.createCell(6).setCellValue(agent.getAccHolderName());
	            dataRow.createCell(7).setCellValue(agent.getAccNumber());
	            dataRow.createCell(8).setCellValue(agent.getBankName());
	            dataRow.createCell(9).setCellValue(agent.getIfscCode());

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
		        headerRow.createCell(0).setCellValue("First Name");
		        headerRow.createCell(1).setCellValue("Last Name");
		        headerRow.createCell(2).setCellValue("Mobile");
		        headerRow.createCell(3).setCellValue("Gender");
		        headerRow.createCell(4).setCellValue("Address");
		        headerRow.createCell(5).setCellValue("Aadhar Number");
		        headerRow.createCell(6).setCellValue("Account Holder Name");
		        headerRow.createCell(7).setCellValue("Account Number");
		        headerRow.createCell(8).setCellValue("Bank Name");
		        headerRow.createCell(9).setCellValue("IFSC Code");

		        int rowIdx = 1;
		        for (AgentMain agent : listAgent) {
		            Row dataRow = sheet.createRow(rowIdx++);

		            dataRow.createCell(0).setCellValue(agent.getFirstName());
		            dataRow.createCell(1).setCellValue(agent.getLastName());
		            dataRow.createCell(2).setCellValue(agent.getMobileNumber());
		            dataRow.createCell(3).setCellValue(agent.getGender());
		            dataRow.createCell(4).setCellValue(agent.getAddress());
		            dataRow.createCell(5).setCellValue(agent.getAadhaarNumber());
		            dataRow.createCell(6).setCellValue(agent.getAccHolderName());
		            dataRow.createCell(7).setCellValue(agent.getAccNumber());
		            dataRow.createCell(8).setCellValue(agent.getBankName());
		            dataRow.createCell(9).setCellValue(agent.getIfscCode());
		        }

		        workbook.write(out);
		        return out.toByteArray();
		    } catch (IOException e) {
		        throw new RuntimeException("Failed to generate Excel", e);
		    }
		}

}
