package intercom.com.vn.spp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.Menu;
import intercom.com.vn.spp.repository.MenuRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class MenuController {

	@Autowired
	private MenuRepository repo;

	@GetMapping("/menus")
	public List<Menu> getAll() {
		return repo.findAll();
	}

	@GetMapping("/menus/{id}")
	public ResponseEntity<Menu> getEmployeeById(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		Menu employee = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + id));
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/menus")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
	public Menu create(@Valid @RequestBody Menu item) {
		return repo.save(item);
	}

	@PutMapping("/menus/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
	public ResponseEntity<Menu> updateEmploye(@PathVariable(value = "id") Long id,
			@Valid @RequestBody Menu detail)
			throws ResourceNotFoundException {
		Menu menu = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + id));
		menu.setName(detail.getName());
		menu.setCode(detail.getCode());
		menu.setNote(detail.getNote());
		menu.setCategory(detail.getCategory());
		repo.save(menu);
		return ResponseEntity.ok(menu);
	}

	@DeleteMapping("/menus/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		Menu menu = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id:: " + id));
		repo.delete(menu);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}

}
