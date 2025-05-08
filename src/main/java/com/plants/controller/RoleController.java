package com.plants.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.RoleService;

@RestController
@RequestMapping("/roleCont")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@GetMapping("/getViewRole")
	public ResponseEntity<Map<String, Object>> viewServiceName() {
		ResponseEntity<Map<String, Object>> response = this.roleService.getViewRole();
		return response;
	}
	
	@GetMapping("/getViewUsers")
	public ResponseEntity<Map<String, Object>> getViewUsers() {
		ResponseEntity<Map<String, Object>> response = this.roleService.getViewUsers();
		return response;
	}
	
	@GetMapping("/getActiveRole")
	public ResponseEntity<Map<String, Object>> getActiveRole() {
		ResponseEntity<Map<String, Object>> response = this.roleService.getActiveRole();
		return response;
	}
	
	@GetMapping("/getSubRoleWithRolePk/{rolePk}")
	public ResponseEntity<Map<String, Object>> getSubRoleWithRolePk(@PathVariable int rolePk) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getSubRoleWithRolePk(rolePk);
		return response;
	}
	
	@GetMapping("/getUserWithSubRolePk/{subRolepk}")
	public ResponseEntity<Map<String, Object>> getUserWithSubRolePk(@PathVariable int subRolepk) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getUserWithSubRolePk(subRolepk);
		return response;
	}
	
	@PostMapping("/addRoleName")
	public ResponseEntity<Map<String, Object>> addRoleName(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.addRoleName(request);
		return response;
	}
	
	@PostMapping("/addSubRole")
	public ResponseEntity<Map<String, Object>> addSubRole(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.addSubRole(request);
		return response;
	}
	
	@PostMapping("/addUserRegistration")
	public ResponseEntity<Map<String, Object>> addUserRegistration(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.addUserRegistration(request);
		return response;
	}
	
	@PostMapping("/getApprovedRole")
	public ResponseEntity<Map<String, Object>> getApprovedRole(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getApprovedRole(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedRole")
	public ResponseEntity<Map<String, Object>> getDisApprovedRole(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getDisApprovedRole(request);
		return response;
	}
	
	@PostMapping("/getApprovedSubRole")
	public ResponseEntity<Map<String, Object>> getApprovedSubRole(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getApprovedSubRole(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedSubRole")
	public ResponseEntity<Map<String, Object>> getDisApprovedSubRole(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getDisApprovedSubRole(request);
		return response;
	}
	
	@PostMapping("/getApprovedUser")
	public ResponseEntity<Map<String, Object>> getApprovedUser(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getApprovedUser(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedUser")
	public ResponseEntity<Map<String, Object>> getDisApprovedUser(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.roleService.getDisApprovedUser(request);
		return response;
	}
}
