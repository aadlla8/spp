package intercom.com.vn.spp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.jwtutils.UserInfoDetails;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.repository.JobRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class JobController {
    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/jobs")
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getById(@PathVariable(value = "id") Long jobId)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        return ResponseEntity.ok().body(job);
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Job create(@Valid @RequestBody Job job, @AuthenticationPrincipal UserInfoDetails uInfo) {
        job.setCreator(uInfo.getUsername());
        return jobRepository.save(job);
    }

    @PutMapping("/jobs/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Job> update(@PathVariable(value = "id") Long jobId,
            @Valid @RequestBody Job jobDetail)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        job.setJobType(jobDetail.getJobType());
        job.setScCode(jobDetail.getScCode());
        job.setStartDate(jobDetail.getStartDate());
        job.setDateEnd(jobDetail.getDateEnd());
        job.setDoneDate(jobDetail.getDoneDate());
        job.setProblemStatus(jobDetail.getProblemStatus());
        job.setNote(jobDetail.getNote());
        job.setDescription(jobDetail.getDescription());
        job.setServiceType(jobDetail.getServiceType());
        job.setInformMethod(jobDetail.getInformMethod());
        job.setJobOfNetworkAndTD(jobDetail.getJobOfNetworkAndTD());
        job.setEmployeeCode(jobDetail.getEmployeeCode());
        job.setDateIssued(jobDetail.getDateIssued());
        job.setRootCause(jobDetail.getRootCause());
        job.setRegion(jobDetail.getRegion());
        job.setProblemInfo(jobDetail.getProblemInfo());
        jobRepository.save(job);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long jobId)
            throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for this id:: " + jobId));
        jobRepository.delete(job);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
