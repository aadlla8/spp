package intercom.com.vn.spp.controller;

import org.springframework.web.bind.annotation.RestController;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.DailyReport;
import intercom.com.vn.spp.model.DailySatistic;
import intercom.com.vn.spp.model.Employee;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.model.Problem;
import intercom.com.vn.spp.repository.DailyReportRepository;
import intercom.com.vn.spp.repository.EmployeeRepository;
import intercom.com.vn.spp.repository.JobRepository;
import intercom.com.vn.spp.repository.ProblemRepository;
import jakarta.validation.Valid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/dailystatistic")
    public DailySatistic dailyStatistic(@RequestParam Optional<Date> date) {
        DailySatistic ds = new DailySatistic();

        var rt = ds.getDic();
        List<DailyReport> reportDaily = new ArrayList<>();

        List<Job> dailyJobs = null;
        if (!date.isEmpty()) {
            dailyJobs = jobRepository.findAllJobDaily(date.get());
        } else
            dailyJobs = jobRepository.findAllJobDaily();
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
                dr.setDeployment(j.getJobOfNetworkAndTD());
                dr.setOtherWork(j.getNote());
                dr.setProblem(j.getProblemInfo());
                if (prob != null)
                    dr.setResultAndApproach(prob.getResultAndSolution());
                dr.setNote(j.getNote());
                dr.setWorkProcessDateTime(j.getDoneHours() + ":" + j.getDoneMinutes());

                if (dr.getComebackofficeDatetime() == null) {
                    if (j.getNoComeBackWhy() != null &&
                            (j.getNoComeBackWhy().equalsIgnoreCase("VeNhaLuon")
                                    || j.getNoComeBackWhy().equalsIgnoreCase("Tiep"))) {
                        ds.getNotBackOffice().add(emCode);
                    } else {
                        if (rt.get("KT chưa về  VP: [" + em.getDepartment() + "]") != null) {
                            rt.get("KT chưa về  VP: [" + em.getDepartment() + "]").add(emCode);
                        } else {
                            var arr = new ArrayList<String>();
                            arr.add(emCode);
                            rt.put("KT chưa về  VP: [" + em.getDepartment() + "]", arr);
                        }
                    }

                } else {
                    ds.getBackOffice().add(emCode);

                    if (rt.get("KT đã về  VP: [" + em.getDepartment() + "]") != null) {
                        rt.get("KT đã về  VP: [" + em.getDepartment() + "]").add(emCode);
                    } else {
                        var arr = new ArrayList<String>();
                        arr.add(emCode);
                        rt.put("KT đã về  VP: [" + em.getDepartment() + "]", arr);
                    }
                }
                if (j.getJobType().equalsIgnoreCase("Nghi")) {
                    ds.getNotAtNoc().add(emCode);
                }
                reportDaily.add(dr);
            }
        }

        var allEm = emRepo.findAll();
        for (DailyReport rd : reportDaily) {
            allEm.removeIf(s -> s.getCode().equals(rd.getEmployeeCode()));
        }
        for (Employee e : allEm) {
            ds.getNoJob().add(e.getCode());

            if (rt.get("KT chưa giao việc: [" + e.getDepartment() + "]") != null) {
                rt.get("KT chưa giao việc: [" + e.getDepartment() + "]").add(e.getCode());
            } else {
                var arr = new ArrayList<String>();
                arr.add(e.getCode());
                rt.put("KT chưa giao việc: [" + e.getDepartment() + "]", arr);
            }
        }
        return ds;

    }

    @GetMapping("/dailyreports")
    public List<DailyReport> getAllEmployees(@RequestParam Optional<Date> date) {
        List<DailyReport> reportDaily = new ArrayList<>();
        List<Job> dailyJobs = null;
        List<String> empCodes = new ArrayList<>();

        if (!date.isEmpty()) {
            dailyJobs = jobRepository.findAllJobDaily(date.get());
        } else
            dailyJobs = jobRepository.findAllJobDaily();
        for (Job j : dailyJobs) {
            String[] employees = j.getEmployeeCode().split(",");
            for (String emCode : employees) {
                if (empCodes.contains(emCode)) {
                    reportDaily.forEach(dr -> {

                        if (dr.getEmployeeCode().equalsIgnoreCase(emCode)) {
                            if (j.getStartDate() != null)
                                dr.setStartDateTime(dr.getStartDateTime()+"<br>--------<br>"+j.getStartDate().getHour() + ":" + j.getStartDate().getMinute());
                            dr.setDeployment(dr.getDeployment()+"<br>--------<br>"+j.getJobOfNetworkAndTD());
                            dr.setOtherWork(dr.getOtherWork()+"<br>--------<br>"+j.getNote());
                            dr.setProblem(dr.getProblem()+"<br>--------<br>"+j.getProblemInfo());
                            if (j.getDoneDate() != null)
                                dr.setDoneDatetime(dr.getDoneDatetime()+"<br>--------<br>"+j.getDoneDate().getHour() + ":" + j.getDoneDate().getMinute());
                            Problem prob = probRepo.findOneByScCode(j.getScCode());
                            if (prob != null)
                                dr.setResultAndApproach(dr.getResultAndApproach()+"<br>--------<br>"+prob.getResultAndSolution());
                            dr.setNote(dr.getNote()+"<br>--------<br>"+j.getNote());
                            dr.setWorkProcessDateTime(dr.getWorkProcessDateTime()+"<br>--------<br>"+j.getDoneHours() + ":" + j.getDoneMinutes());
                            if (j.getComebackOfficeDate() != null)
                                dr.setComebackofficeDatetime(dr.getComebackofficeDatetime()+"<br>--------<br>"+
                                        j.getComebackOfficeDate().getHour() + ":"
                                                + j.getComebackOfficeDate().getMinute());
                        }
                    });
                } else {
                    empCodes.add(emCode);
                    DailyReport dr = new DailyReport();
                    Employee em = emRepo.findOneByCode(emCode);
                    Problem prob = probRepo.findOneByScCode(j.getScCode());
                    dr.setRegion(j.getRegion());
                    dr.setEmployeeCode(emCode);
                    if (em != null)
                        dr.setDepartment(em.getDepartment());
                    if (j.getStartDate() != null)
                        dr.setStartDateTime(j.getStartDate().getHour() + ":" + j.getStartDate().getMinute());
                    dr.setDeployment(j.getJobOfNetworkAndTD());
                    dr.setOtherWork(j.getNote());
                    dr.setProblem(j.getProblemInfo());
                    if (j.getDoneDate() != null)
                        dr.setDoneDatetime(j.getDoneDate().getHour() + ":" + j.getDoneDate().getMinute());
                    if (prob != null)
                        dr.setResultAndApproach(prob.getResultAndSolution());
                    dr.setNote(j.getNote());
                    dr.setWorkProcessDateTime(j.getDoneHours() + ":" + j.getDoneMinutes());
                    if (j.getComebackOfficeDate() != null)
                        dr.setComebackofficeDatetime(
                                j.getComebackOfficeDate().getHour() + ":" + j.getComebackOfficeDate().getMinute());
                    reportDaily.add(dr);
                }
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
                if (j.getStartDate() != null)
                    dr.setStartDateTime(j.getStartDate().getHour() + ":" + j.getStartDate().getMinute());
                dr.setDeployment(j.getJobOfNetworkAndTD());
                dr.setOtherWork(j.getNote());
                dr.setProblem(j.getProblemInfo());
                if (j.getDoneDate() != null)
                    dr.setDoneDatetime(j.getDoneDate().getHour() + ":" + j.getDoneDate().getMinute());
                if (prob != null)
                    dr.setResultAndApproach(prob.getResultAndSolution());
                dr.setNote(j.getNote());
                dr.setWorkProcessDateTime(j.getDoneHours() + ":" + j.getDoneMinutes());

                if (j.getComebackOfficeDate() != null)
                    dr.setComebackofficeDatetime(
                            j.getComebackOfficeDate().getHour() + ":" + j.getComebackOfficeDate().getMinute());
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
