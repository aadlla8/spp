package intercom.com.vn.spp.services;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import intercom.com.vn.spp.jwtutils.UserInfoDetails;
import intercom.com.vn.spp.model.UserInfo;
import intercom.com.vn.spp.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional; 

@Service
public class UserInfoService implements UserDetailsService { 

	@Autowired
	private UserInfoRepository repository;  

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

		Optional<UserInfo> userDetail = repository.findByName(username); 

		// Converting userDetail to UserDetails 
		return userDetail.map(UserInfoDetails::new) 
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
	} 
   
	public String addUser(UserInfo userInfo) { 
        PasswordEncoder encoder= new BCryptPasswordEncoder();
		userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
		repository.save(userInfo); 
		return "User Added Successfully"; 
	} 
	public String updateUser(UserInfo userInfo, Integer id) {
		UserInfo user = repository.getReferenceById(id);
		user.setRoles(userInfo.getRoles());
		user.setEmail(userInfo.getEmail());
		user.setName(userInfo.getName());
		if(!userInfo.getPassword().isEmpty()){
			PasswordEncoder encoder= new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(userInfo.getPassword()));
		}
		repository.save(user); 
		return "User Updated.";
	}
	public List<UserInfo> getAll(){
		return repository.findAll();
	}


} 
