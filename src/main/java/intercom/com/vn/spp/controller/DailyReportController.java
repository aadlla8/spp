package intercom.com.vn.spp.controller;

import org.springframework.web.bind.annotation.RestController;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.DailyReport;
import intercom.com.vn.spp.model.Employee;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.model.Problem;
import intercom.com.vn.spp.repository.DailyReportRepository;
import intercom.com.vn.spp.repository.EmployeeRepository;
import intercom.com.vn.spp.repository.JobRepository;
import intercom.com.vn.spp.repository.ProblemRepository;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class DailyReportController {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private DailyReportRepository repo;
    @Autowired
    private EmployeeRepository emRepo;
    @Autowired
    private ProblemRepository probRepo;

    @GetMapping("/dailyreports")
    public List<DailyReport> getAllEmployees() {
        List<DailyReport> reportDaily = new ArrayList<>();

        List<Job> dailyJobs = jobRepository.findAllJobDaily();
        for (Job j : dailyJobs) {
            String[] employees = j.getEmployeeCode().split(",");
            for (String emCode : employees) {
                DailyReport dr = new DailyReport();
                Employee em = emRepo.findOneByCode(emCode);
                Problem prob = probRepo.findOneByScCode(j.getScCode());
                dr.setRegion(j.getRegion());
                dr.setEmployeeCode(emCode);
                if (em != null)
                    dr.setDepartment(em.getDepartment());
                dr.setStartDateTime(j.getStartDate());
                dr.setDeployment(j.getJobOfNetworkAndTD());
                dr.setOtherWork(j.getNote());
                dr.setProblem(j.getProblemInfo());
                dr.setDoneDatetime(j.getDoneDate());
                if (prob != null)
                    dr.setResultAndApproach(prob.getResultAndSolution());
                dr.setNote(j.getNote());
                dr.setWorkProcessDateTime(j.getDoneHours() + ":" + j.getDoneMinutes());
                dr.setComebackofficeDatetime(j.getComebackOfficeDate());
                reportDaily.add(dr);
            }

        }
        return reportDaily;
    }
    @GetMapping("/monthlyeports")
    public List<DailyReport> monthlyReport() {
        List<DailyReport> reportDaily = new ArrayList<>();

        List<Job> dailyJobs = jobRepository.findAllJobMonth();
        for (Job j : dailyJobs) {
            String[] employees = j.getEmployeeCode().split(",");
            for (String emCode : employees) {
                DailyReport dr = new DailyReport();
                Employee em = emRepo.findOneByCode(emCode);
                Problem prob = probRepo.findOneByScCode(j.getScCode());
                dr.setRegion(j.getRegion());
                dr.setEmployeeCode(emCode);
                if (em != null)
                    dr.setDepartment(em.getDepartment());
                dr.setStartDateTime(j.getStartDate());
                dr.setDeployment(j.getJobOfNetworkAndTD());
                dr.setOtherWork(j.getNote());
                dr.setProblem(j.getProblemInfo());
                dr.setDoneDatetime(j.getDoneDate());
                if (prob != null)
                    dr.setResultAndApproach(prob.getResultAndSolution());
                dr.setNote(j.getNote());
                dr.setWorkProcessDateTime(j.getDoneHours() + ":" + j.getDoneMinutes());
                dr.setComebackofficeDatetime(j.getComebackOfficeDate());
                reportDaily.add(dr);
            }
        }
        return reportDaily;
    }

    @GetMapping("/dailyreports/{id}")
    public ResponseEntity<DailyReport> getEmployeeById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        DailyReport report = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + id));
        return ResponseEntity.ok().body(report);
    }

    @PostMapping("/dailyreports")
    public Boolean createEmployee(@Valid @RequestBody DailyReport report) {
        return true;
    }

    @PutMapping("/dailyreports/{id}")
    public ResponseEntity<Boolean> updateEmploye(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER2')")
    @DeleteMapping("/dailyreports/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
