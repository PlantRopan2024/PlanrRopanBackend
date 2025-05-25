package com.plants.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.IncludingServiceRepo;
import com.plants.Dao.PlansRepo;
import com.plants.Dao.ServiceIncludedMainRepo;
import com.plants.Dao.serviceNameDao;
import com.plants.Dao.userDao;
import com.plants.config.Utils;
import com.plants.entities.IncludingService;
import com.plants.entities.Plans;
import com.plants.entities.PlansDto;
import com.plants.entities.ServiceIncludedMain;
import com.plants.entities.ServiceNameDto;
import com.plants.entities.serviceName;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class PlansServices {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	serviceNameDao serviceNameDao;

	@Autowired
	ServiceIncludedMainRepo serviceIncludedMainRepo;

	@Autowired
	userDao userdao;

	@Autowired
	IncludingServiceRepo includingServiceRepo;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	PlansRepo plansRepo;

	public ResponseEntity<Map<String, Object>> viewService(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		List<serviceName> list = this.serviceNameDao.getallViewService();
		if (list.isEmpty()) {
			response.put("data", list);
			response.put("message", "Service Name Not Avaliable");
			response.put("status", false);
		} else {
			List<Map<String, Object>> filteredList = new ArrayList<>();
			for (serviceName servName : list) {
				Map<String, Object> serviceData = new HashMap<>();
				serviceData.put("primaryKey", servName.getPrimaryKey());
				serviceData.put("active", servName.isActive());
				serviceData.put("serviceImage",
						Utils.findImgPath(request, uploadDir, servName.getName(), servName.getServiceImage()));
				serviceData.put("name", servName.getName());
				serviceData.put("serviceImageName", servName.getServiceImage());
				filteredList.add(serviceData);
			}
			response.put("data", filteredList);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> serviceByGetPlans(int servicePk) {
		Map<String, Object> response = new HashMap<>();
		serviceName serviceObject = this.serviceNameDao.getServiceId(servicePk);
		if (serviceObject.getPlans().isEmpty()) {
			response.put("data", serviceObject.getPlans());
			response.put("serviceName", serviceObject.getName());
			response.put("message", "Plans are not avaliable for this services");
			response.put("status", false);
		} else {
			response.put("serviceName", serviceObject.getName());
			response.put("data", serviceObject.getPlans());
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> viewPlansByPk(int planPk) {
		Map<String, Object> response = new HashMap<>();
		Plans getPlan = this.customerDao.getPlansByPk(planPk);
		if (Objects.isNull(getPlan)) {
			response.put("data", getPlan);
			response.put("status", false);
		} else {
			response.put("data", getPlan);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> addService(String serviceJson, MultipartFile serviceImage) {
		Map<String, Object> response = new HashMap<>();

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			ServiceNameDto serviceNameDto = objectMapper.readValue(serviceJson, ServiceNameDto.class);

			if (serviceImage.isEmpty()) {
				response.put("message", "Please select the Service image.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}

			if (serviceNameDto.getServiceName() == null || serviceNameDto.getServiceName().isEmpty()) {
				response.put("message", "Provide Service Name");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			serviceName servName = new serviceName();
			servName.setName(serviceNameDto.getServiceName());
			servName.setActive(false);
			servName.setCreatedAt(LocalDateTime.now());
			servName.setUpdatedAt(LocalDateTime.now());

			String subDirectoryFloderName = serviceNameDto.getServiceName();
			String fileName = serviceNameDto.getServiceName() + System.currentTimeMillis() + "_"
					+ serviceImage.getOriginalFilename();
			String serviceImageUrl = Utils.saveImgFile(serviceImage, uploadDir, subDirectoryFloderName, fileName);

			servName.setServiceImage(fileName);
			servName.setServiceImagePath(serviceImageUrl);
			servName.setServiceImage_type(serviceImage.getContentType());

			this.serviceNameDao.save(servName);
			response.put("message", "Service Name Added");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> updateServiceName(String serviceJson, MultipartFile serviceImage) {
		Map<String, Object> response = new HashMap<>();

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			ServiceNameDto serviceNameDto = objectMapper.readValue(serviceJson, ServiceNameDto.class);
			System.out.println(" service pk - " + serviceNameDto.getServicePk());
			System.out.println(" service name - " + serviceNameDto.getServiceName());

			if (serviceImage.isEmpty()) {
				response.put("message", "Please select the Service image.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}

			if (serviceNameDto.getServiceName() == null || serviceNameDto.getServiceName().isEmpty()) {
				response.put("message", "Provide Service Name");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}

			serviceName serviceObject = this.serviceNameDao.getServicePk(serviceNameDto.getServicePk());

			serviceObject.setName(serviceNameDto.getServiceName());
			serviceObject.setUpdatedAt(LocalDateTime.now());

			String subDirectoryFloderName = serviceNameDto.getServiceName();
			String fileName = serviceNameDto.getServiceName() + System.currentTimeMillis() + "_"
					+ serviceImage.getOriginalFilename();
			String serviceImageUrl = Utils.saveImgFile(serviceImage, uploadDir, subDirectoryFloderName, fileName);

			serviceObject.setServiceImage(fileName);
			serviceObject.setServiceImagePath(serviceImageUrl);
			serviceObject.setServiceImage_type(serviceImage.getContentType());

			this.serviceNameDao.save(serviceObject);
			response.put("message", "Service Name Updated");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> viewIncludServiceMain() {
		Map<String, Object> response = new HashMap<>();
		List<ServiceIncludedMain> list = this.serviceIncludedMainRepo.getallViewIncludingService();
		if (list.isEmpty()) {
			response.put("data", list);
			response.put("message", "Including Service Name Not Avaliable");
			response.put("status", false);
		} else {
			List<Map<String, Object>> filteredList = new ArrayList<>();
			for (ServiceIncludedMain servName : list) {
				Map<String, Object> serviceData = new HashMap<>();
				serviceData.put("primaryKey", servName.getPrimaryKey());
				serviceData.put("active", servName.isActive());
				serviceData.put("headerName", servName.getHeaderName());
				serviceData.put("headerNameDetails", servName.getNameDetails());
				filteredList.add(serviceData);
			}
			response.put("data", filteredList);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getActiveIncludingService() {
		Map<String, Object> response = new HashMap<>();
		List<ServiceIncludedMain> list = this.serviceIncludedMainRepo.getactiveIncludingServiceMain();
		if (list.isEmpty()) {
			response.put("data", list);
			response.put("message", "Including Service Name Not Avaliable");
			response.put("status", false);
		} else {
			List<Map<String, Object>> filteredList = new ArrayList<>();
			for (ServiceIncludedMain servName : list) {
				Map<String, Object> serviceData = new HashMap<>();
				serviceData.put("primaryKey", servName.getPrimaryKey());
				serviceData.put("headerName", servName.getHeaderName());
				filteredList.add(serviceData);
			}
			response.put("data", filteredList);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> addIncludingService(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();

		try {
			String headerName = request.get("headerName");
			String detailsName = request.get("nameDetails");

			if (headerName == null || headerName.isEmpty()) {
				response.put("message", "Provide Including Service Name");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			ServiceIncludedMain serviceIncludedMain = new ServiceIncludedMain();
			serviceIncludedMain.setHeaderName(headerName);
			serviceIncludedMain.setNameDetails(detailsName);
			serviceIncludedMain.setActive(false);
			serviceIncludedMain.setCreatedAt(LocalDateTime.now());
			serviceIncludedMain.setUpdatedAt(LocalDateTime.now());

			this.serviceIncludedMainRepo.save(serviceIncludedMain);
			response.put("message", "Including Service Name Added");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> includingServiceActive(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			ServiceIncludedMain serviceIncludedMain = this.serviceIncludedMainRepo
					.getPkIncludingServiceMain(primaryKey);
			serviceIncludedMain.setActive(true);
			serviceIncludedMain.setUpdatedAt(LocalDateTime.now());

			this.serviceIncludedMainRepo.save(serviceIncludedMain);
			response.put("message", "Including Service Approved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> includingServiceDisActive(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			ServiceIncludedMain serviceIncludedMain = this.serviceIncludedMainRepo
					.getPkIncludingServiceMain(primaryKey);
			serviceIncludedMain.setActive(false);
			serviceIncludedMain.setUpdatedAt(LocalDateTime.now());

			this.serviceIncludedMainRepo.save(serviceIncludedMain);
			response.put("message", "Including Service Dis Approved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	@Transactional
	public ResponseEntity<Map<String, Object>> addPlans(String plansJson, MultipartFile planImage) {
		Map<String, Object> response = new HashMap<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			PlansDto plans = objectMapper.readValue(plansJson, PlansDto.class);

			if (planImage.isEmpty()) {
				response.put("message", "Please select the Plan image.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}

			serviceName serviceList = this.serviceNameDao.getServicePk(plans.getServiceNamePk());

			Plans savePlan = new Plans();
			savePlan.setPlansName(plans.getPlansName());
			savePlan.setPlanPacks(plans.getPlanPacks());
			savePlan.setPlansRs(plans.getPlansRs());
			savePlan.setPlanType(plans.getPlanType());
			savePlan.setTimeDuration(plans.getTimeDuration());
			savePlan.setUptoPots(plans.getUptoPots());
			savePlan.setActive(false);
			savePlan.setCreatedAt(LocalDateTime.now());
			savePlan.setServicesName(serviceList);

			// set image Name
			String subDirectoryFloderName = serviceList.getName();
			String fileName = plans.getPlansName() + System.currentTimeMillis() + "_" + planImage.getOriginalFilename();
			String planImageUrl = Utils.saveImgFile(planImage, uploadDir, subDirectoryFloderName, fileName);

			savePlan.setPlanImage(fileName);
			savePlan.setPlanImagePath(planImageUrl);
			savePlan.setPlanImage_type(planImage.getContentType());

			Plans savedPlan = this.userdao.save(savePlan);
			// Save including services
			List<IncludingService> includingServices = plans.getIncludingServicesList().stream().map(serviceDto -> {
				IncludingService includingService = new IncludingService();
				ServiceIncludedMain serviceMain = serviceIncludedMainRepo
						.getPkIncludingServiceMain(serviceDto.getPrimaryKey());
				includingService.setHeaderName(serviceMain.getHeaderName());
				includingService.setNameDetails(serviceMain.getNameDetails());
				includingService.setPlans(savedPlan);
				return includingService;
			}).collect(Collectors.toList());
			this.includingServiceRepo.saveAll(includingServices);
			response.put("message", "Plan added successfully");
			response.put("status", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
	}

	@Transactional
	public ResponseEntity<Map<String, Object>> updatePlans(String plansJson, MultipartFile planImage) {
		Map<String, Object> response = new HashMap<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			PlansDto plans = objectMapper.readValue(plansJson, PlansDto.class);

			System.out.println(" plans -- " + plans);
			if (planImage.isEmpty()) {
				response.put("message", "Please select the Plan image.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}

			Plans getPlan = this.customerDao.getPlansId(plans.getPlanKey());

			serviceName serviceList = this.serviceNameDao.getServicePk(plans.getServiceNamePk());

			getPlan.setPlansName(plans.getPlansName());
			getPlan.setPlanPacks(plans.getPlanPacks());
			getPlan.setPlansRs(plans.getPlansRs());
			getPlan.setPlanType(plans.getPlanType());
			getPlan.setTimeDuration(plans.getTimeDuration());
			getPlan.setUptoPots(plans.getUptoPots());
			getPlan.setServicesName(serviceList);

			// set image Name
			String subDirectoryFloderName = serviceList.getName();
			String fileName = plans.getPlansName() + System.currentTimeMillis() + "_" + planImage.getOriginalFilename();
			String planImageUrl = Utils.saveImgFile(planImage, uploadDir, subDirectoryFloderName, fileName);

			getPlan.setPlanImage(fileName);
			getPlan.setPlanImagePath(planImageUrl);
			getPlan.setPlanImage_type(planImage.getContentType());

			Plans savedPlan = this.userdao.save(getPlan);

			// delete servce including update

			// Delete existing IncludingService records for the plan
			List<IncludingService> existingServices = this.includingServiceRepo.getDataIncluding(plans.getPlanKey());
			if (existingServices != null && !existingServices.isEmpty()) {
				this.includingServiceRepo.deleteAll(existingServices);
			}
			// Save including services
			List<IncludingService> includingServices = plans.getIncludingServicesList().stream().map(serviceDto -> {
				IncludingService includingService = new IncludingService();
				ServiceIncludedMain serviceMain = serviceIncludedMainRepo.getPkIncludingServiceMain(serviceDto.getPrimaryKey());
				includingService.setHeaderName(serviceMain.getHeaderName());
				includingService.setNameDetails(serviceMain.getNameDetails());
				includingService.setPlans(savedPlan);
				return includingService;
			}).collect(Collectors.toList());
			this.includingServiceRepo.saveAll(includingServices);
			response.put("message", "Plan updated successfully");
			response.put("status", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
	}

	public ResponseEntity<Map<String, Object>> getApprovedPlans(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			Plans activePlans = this.plansRepo.getPlansId(primaryKey);
			activePlans.setActive(true);
			activePlans.setUpdatedAt(LocalDateTime.now());

			this.plansRepo.save(activePlans);
			response.put("message", "Plan are Approved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getDisApprovedPlans(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			Plans activePlans = this.plansRepo.getPlansId(primaryKey);
			activePlans.setActive(false);
			activePlans.setUpdatedAt(LocalDateTime.now());
			this.plansRepo.save(activePlans);
			response.put("message", "Plan are Dis Approved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getApprovedServiceName(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			serviceName serviceObject = this.serviceNameDao.getServicePk(primaryKey);
			serviceObject.setActive(true);
			serviceObject.setUpdatedAt(LocalDateTime.now());

			this.serviceNameDao.save(serviceObject);
			response.put("message", "Service Name are Approved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getDisApprovedServiceName(@RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String primaryKey = request.get("primaryKey");
			serviceName serviceObject = this.serviceNameDao.getServicePk(primaryKey);
			serviceObject.setActive(false);
			serviceObject.setUpdatedAt(LocalDateTime.now());

			this.serviceNameDao.save(serviceObject);
			response.put("message", "Service Name are DisApproved");
			response.put("status", true);
		} catch (Exception e) {
			response.put("message", "Something Went Wrong");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
}
