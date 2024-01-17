package intercom.com.vn.spp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import intercom.com.vn.spp.model.Activity;
import intercom.com.vn.spp.repository.ActivityRepository;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class ActivityController {
	@Autowired
    private ActivityRepository repo;

	@GetMapping("/activities")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
	public List<Activity> getAll() {
		return repo.findAll();
	}

	 
}
