package intercom.com.vn.spp.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import intercom.com.vn.spp.model.UserInfo;
import intercom.com.vn.spp.services.UserInfoService; 

@RestController
@CrossOrigin()
@RequestMapping("/api/v1") 
public class UserController { 

	@Autowired
	private UserInfoService service;  
	@GetMapping("/users")
	public List<UserInfo> getAll(){
		return service.getAll();
	}
	@PutMapping("/users/{id}") 
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public String update(@RequestBody UserInfo userInfo, Integer id) { 	 
		return service.updateUser(userInfo,id); 
	}  
} 
