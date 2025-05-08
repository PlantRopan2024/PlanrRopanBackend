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

import com.plants.Dao.RoleRepo;
import com.plants.Dao.SubRoleRepo;
import com.plants.Dao.userDao;
import com.plants.entities.Role;
import com.plants.entities.SubRole;
import com.plants.entities.user;

@Service
public class RoleService {
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	userDao userdao;
	
	@Autowired
	SubRoleRepo subRoleRepo;
	
	public ResponseEntity<Map<String, Object>> getViewRole() {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Role> findRole = this.roleRepo.getAllRole();
		if(findRole.isEmpty()) {
			response.put("data", findRole);
			response.put("status", false);
		}else {
			response.put("data", findRole);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getViewUsers() {
		Map<String,Object> response = new HashMap<String, Object>();
		List<user> findUser = this.userdao.getUserData();
		if(findUser.isEmpty()) {
			response.put("data", findUser);
			response.put("status", false);
		}else {
			response.put("data", findUser);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getActiveRole() {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Role> findRole = this.roleRepo.getActiveRole();
		if(findRole.isEmpty()) {
			response.put("data", findRole);
			response.put("status", false);
		}else {
			response.put("data", findRole);
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getSubRoleWithRolePk(int rolePk) {
		Map<String,Object> response = new HashMap<String, Object>();
		Role findSubRole = this.roleRepo.getRolePk(rolePk);
		if(findSubRole.getSubRole().isEmpty()) {
			response.put("RoleName", findSubRole.getRoleName());
			response.put("data", findSubRole.getSubRole());
			response.put("status", false);
		}else {
			response.put("RoleName", findSubRole.getRoleName());
			response.put("data", findSubRole.getSubRole());
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getUserWithSubRolePk(int subRolePk) {
		Map<String,Object> response = new HashMap<String, Object>();
		SubRole findSubRole = this.subRoleRepo.getSubRolePk(subRolePk);
		if(findSubRole.getUserList().isEmpty()) {
			response.put("subRoleName", findSubRole.getSubRoleName());
			response.put("data", findSubRole.getUserList());
			response.put("status", false);
		}else {
			response.put("subRoleName", findSubRole.getSubRoleName());
			response.put("data", findSubRole.getUserList());
			response.put("status", true);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> addRoleName(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			String roleName = request.get("roleName");
			Role findRole = this.roleRepo.getRoleName(roleName);
			if(Objects.nonNull(findRole)) {
				response.put("message", "Role Name Already Added");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			Role role = new Role();
			role.setRoleName(roleName.toUpperCase());
			role.setActive(false);
			role.setCreatedAt(LocalDateTime.now());
			role.setUpdatedAt(LocalDateTime.now());
			this.roleRepo.save(role);
			response.put("message", "Role Added");
			response.put("status", true);
		}catch(Exception e){
			e.printStackTrace();
			response.put("message", "Something Went Wrong!");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> addSubRole(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			//String roleName = request.get("roleName");
			String subRoleName = request.get("subRoleName");
			String rolePk = request.get("rolePk");
			
			Role findRole = this.roleRepo.getRolePk(rolePk);
			
			SubRole subRolefind = this.subRoleRepo.getSubRoleName(subRoleName);
			if(Objects.nonNull(subRolefind)) {
				response.put("message", "Sub Role Name Already Added");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			SubRole subRole = new SubRole();
			subRole.setSubRoleName(subRoleName.toUpperCase());
			subRole.setActive(false);
			subRole.setCreatedAt(LocalDateTime.now());
			subRole.setUpdatedAt(LocalDateTime.now());
			subRole.setRole(findRole);
			this.subRoleRepo.save(subRole);
			response.put("message", "Sub Role Added");
			response.put("status", true);
		}catch(Exception e){
			e.printStackTrace();
			response.put("message", "Something Went Wrong!");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> addUserRegistration(@RequestBody Map<String, String> request) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        String subRoleName = request.get("subRoleName");
	        String username = request.get("username");
	        String employeename = request.get("employeename");
	        String mobileNumber = request.get("mobileNumber");
	        String email = request.get("email");
	        String password = request.get("password");

	        // Step 1: Find SubRole
	        SubRole subRole = subRoleRepo.getSubRoleName(subRoleName);

	        if (subRole == null) {
	            response.put("status", false);
	            response.put("message", "Sub Role not found");
	            return ResponseEntity.ok(response);
	        }

	        // Step 2: Check Role is present
	        Role role = subRole.getRole();
	        if (role == null) {
	            response.put("status", false);
	            response.put("message", "Sub Role is not linked to any Role");
	            return ResponseEntity.ok(response);
	        }

	        // Step 3: Check for duplicate username under this subrole
	        boolean userExists = subRole.getUserList().stream()
	            .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

	        if (userExists) {
	            response.put("status", false);
	            response.put("message", "Username already exists under this Sub Role");
	            return ResponseEntity.ok(response);
	        }

	        // Step 4: Save new user
	        user usr = new user();
	        usr.setUsername(username);
	        usr.setEmployeename(employeename);
	        usr.setMobileNumber(mobileNumber);
	        usr.setEmail(email);
	        usr.setPassword(password);
	        usr.setSubRole(subRole);
	        usr.setActive(false);
	        usr.setCreatedAt(LocalDateTime.now());
	        usr.setModifiedOn(LocalDateTime.now());

	        userdao.save(usr);

	        response.put("status", true);
	        response.put("message", "User successfully registered under sub role");

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "SomeThing Went Wrong ");
	    }

	    return ResponseEntity.ok(response);
	}


	
	public ResponseEntity<Map<String, Object>> getApprovedRole(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			Role findRole = this.roleRepo.getRolePk(primaryKey);
			findRole.setActive(true);
			findRole.setUpdatedAt(LocalDateTime.now());
			this.roleRepo.save(findRole);
			response.put("message", "Role Active");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedRole(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			Role findRole = this.roleRepo.getRolePk(primaryKey);
			findRole.setActive(false);
			findRole.setUpdatedAt(LocalDateTime.now());
			this.roleRepo.save(findRole);
			response.put("message", "Role DisActive");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getApprovedSubRole(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			SubRole findSubRole = this.subRoleRepo.getSubRolePk(primaryKey);
			findSubRole.setActive(true);
			findSubRole.setUpdatedAt(LocalDateTime.now());
			this.subRoleRepo.save(findSubRole);
			response.put("message", "Sub Role Active");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedSubRole(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			SubRole findSubRole = this.subRoleRepo.getSubRolePk(primaryKey);
			findSubRole.setActive(false);
			findSubRole.setUpdatedAt(LocalDateTime.now());
			this.subRoleRepo.save(findSubRole);
			response.put("message", "Sub Role Dis Approved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getApprovedUser(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			user findUser = this.userdao.getUserPk(primaryKey);
			findUser.setActive(true);
			findUser.setModifiedOn(LocalDateTime.now());
			this.userdao.save(findUser);
			response.put("message", "User Approved");
			response.put("status",true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status",false);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getDisApprovedUser(@RequestBody Map<String, String> request) {
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String primaryKey = request.get("primaryKey");
			user findUser = this.userdao.getUserPk(primaryKey);
			findUser.setActive(false);
			findUser.setModifiedOn(LocalDateTime.now());
			this.userdao.save(findUser);
			response.put("message", "User DisApproved");
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
