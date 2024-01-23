package intercom.com.vn.spp.controller;

import java.time.LocalDateTime;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.jwtutils.UserInfoDetails;
import intercom.com.vn.spp.model.Activity;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.repository.ActivityRepository;
import intercom.com.vn.spp.repository.JobRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class JobController {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ActivityRepository activityRepo;

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
    public Job create(@Valid @RequestBody Job job, @AuthenticationPrincipal UserInfoDetails uInfo) {
        job.setCreator(uInfo.getUsername());
        jobRepository.save(job);
        saveActivity(job, uInfo, "create_job","job");
        return job;
    }

    @PutMapping("/jobs/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
    public ResponseEntity<Job> update(@PathVariable(value = "id") Long jobId,
            @Valid @RequestBody Job jobDetail, @AuthenticationPrincipal UserInfoDetails uInfo)
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
        job.setComebackOfficeDate(jobDetail.getComebackOfficeDate());
        job.setCustomerContact(jobDetail.getCustomerContact());
        job.setNoComeBackWhy(jobDetail.getNoComeBackWhy());
        jobRepository.save(job);
        saveActivity(jobDetail, uInfo, "update_job","job");
        return ResponseEntity.ok(job);
    }

    private void saveActivity(Job jobDetail, UserInfoDetails uInfo, String act, String type) {
        try {
            Activity at = new Activity();
            at.setUsername(uInfo.getUsername());
            at.setAction(act);
            at.setObjectType(type);
            var om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String json = om.writer().withDefaultPrettyPrinter().writeValueAsString(jobDetail);
            at.setNewValue(json);
            at.setActionDate(LocalDateTime.now());
            activityRepo.save(at);

        } catch (JsonProcessingException ex) {
             
        }
    }

    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
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
