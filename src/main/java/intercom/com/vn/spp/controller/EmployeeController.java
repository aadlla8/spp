package intercom.com.vn.spp.controller;

import org.springframework.web.bind.annotation.RestController;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.Employee;
import intercom.com.vn.spp.repository.EmployeeRepository;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin()
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not found for this id:: "+employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmploye(@PathVariable(value="id") Long employeeId, @Valid @RequestBody  Employee employeeDetails) 
        throws ResourceNotFoundException {
        Employee employee= employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not found for this id:: "+ employeeId));
        employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employeeRepository.save(employee);
        
        return ResponseEntity.ok(employee);
    }
    @DeleteMapping("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(@PathVariable(value="id") Long employeeId) throws ResourceNotFoundException{
                Employee employee= employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not found for this id:: "+ employeeId));
            employeeRepository.delete(employee);
            Map<String,Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);

            return response;
    }
}
