package com.plants.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.plants.Dao.CityRepo;
import com.plants.Dao.StateRepo;
import com.plants.entities.City;
import com.plants.entities.State;

@Service
public class StateService {
	
	@Autowired
	StateRepo stateRepo;
	
	@Autowired
	CityRepo cityRepo;
	
	public ResponseEntity<Map<String, Object>> addState(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			String stateName = request.get("stateName");
			
			State stateDB = this.stateRepo.getStateName(stateName);

			if(Objects.nonNull(stateDB)) {
				if(stateDB.getStateName().equals(stateName)) {
					response.put("message", "State Already Added "+stateName);
					response.put("status", false);
					return ResponseEntity.ok(response);
				}
			}
			State state = new State();
			state.setStateName(stateName);
			state.setActive(false);
			state.setCreatedAt(LocalDateTime.now());
			state.setUpdatedAt(LocalDateTime.now());
			this.stateRepo.save(state);
			response.put("message", " State Added");
			response.put("status", true);
		}catch(Exception e){
			e.printStackTrace();
			response.put("message", "Something Went Wrong!");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> ViewStateList() {
		Map<String,Object> response = new HashMap<String, Object>();
		List<State> listState = this.stateRepo.getAllState();
		if(listState.isEmpty()) {
			response.put("data", listState);
			response.put("status", false);
		}else {
			response.put("data", listState);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> updateState(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primarykey = request.get("primarykey");
			String stateName = request.get("stateName");

			State state = this.stateRepo.getStatePk(primarykey);
			state.setStateName(stateName);
			state.setUpdatedAt(LocalDateTime.now());
			this.stateRepo.save(state);
			response.put("message", "State Name Updated");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getApprovedState(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			State state = this.stateRepo.getStatePk(primaryKey);
			state.setActive(true);
			state.setUpdatedAt(LocalDateTime.now());
			this.stateRepo.save(state);
			response.put("message", " State Approved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedState(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			State state = this.stateRepo.getStatePk(primaryKey);
			state.setActive(false);
			state.setUpdatedAt(LocalDateTime.now());
			this.stateRepo.save(state);
			response.put("message", "State DisApproved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	public ResponseEntity<Map<String, Object>> stateByGetCity(int statePk) {
		Map<String, Object> response = new HashMap<>();
		
		State state = this.stateRepo.getStatePk(statePk);
			if(state.getCity().isEmpty()) {
				response.put("data", state.getCity());
				response.put("stateName", state.getStateName());
				response.put("message", "City are not avaliable for this State");
				response.put("status", false);
			}else {
				response.put("data", state.getCity());
				response.put("stateName", state.getStateName());
				response.put("status", true);
			}
		return ResponseEntity.ok(response);
	}
	
	
	public ResponseEntity<Map<String, Object>> addCity(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			String cityName = request.get("cityName");
			String statePk = request.get("statePk");
			
			State state = this.stateRepo.getStatePk(statePk);

			City cityDB = this.cityRepo.getCityName(cityName);

			if(Objects.nonNull(cityDB)) {
				if(cityDB.getCityName().equals(cityName)) {
					response.put("message", "City Already Added "+cityName);
					response.put("status", false);
					return ResponseEntity.ok(response);
				}
			}
			City city = new City();
			city.setCityName(cityName);
			city.setActive(false);
			city.setState(state);
			city.setCreatedAt(LocalDateTime.now());
			city.setUpdatedAt(LocalDateTime.now());
			this.cityRepo.save(city);
			response.put("message", " City Added");
			response.put("status", true);
		}catch(Exception e){
			e.printStackTrace();
			response.put("message", "Something Went Wrong!");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	public ResponseEntity<Map<String, Object>> updateCity(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primarykey = request.get("primarykey");
			String cityName = request.get("cityName");

			City city = this.cityRepo.getCityPk(primarykey);
			city.setCityName(cityName);
			city.setUpdatedAt(LocalDateTime.now());
			this.cityRepo.save(city);
			response.put("message", "City Name Updated");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getApprovedCity(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			
			City city = this.cityRepo.getCityPk(primaryKey);

			city.setActive(true);
			city.setUpdatedAt(LocalDateTime.now());
			this.cityRepo.save(city);
			response.put("message", " City Approved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedCity(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			
			City city = this.cityRepo.getCityPk(primaryKey);

			city.setActive(false);
			city.setUpdatedAt(LocalDateTime.now());
			this.cityRepo.save(city);
			response.put("message", " City Dis Approved");
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
