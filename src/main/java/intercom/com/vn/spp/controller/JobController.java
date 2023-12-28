package intercom.com.vn.spp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.repository.JobRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/jobs")
    public List<Job> getAllEmployees() {
        return jobRepository.findAll();
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getEmployeeById(@PathVariable(value = "id") Long jobId)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        return ResponseEntity.ok().body(job);
    }

    @PostMapping("/jobs")
    public Job createEmployee(@Valid @RequestBody Job job) {
        return jobRepository.save(job);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateEmploye(@PathVariable(value = "id") Long jobId,
            @Valid @RequestBody Job employeeDetails)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        job.setScCode(employeeDetails.getScCode());
        job.setStartDate(employeeDetails.getStartDate());
        job.setDateEnd(employeeDetails.getDateEnd());
        job.setDoneDate(employeeDetails.getDoneDate());

        job.setNote(employeeDetails.getNote());
        job.setDescription(employeeDetails.getDescription());
        jobRepository.save(job);

        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/jobs/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long jobId)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        jobRepository.delete(job);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
