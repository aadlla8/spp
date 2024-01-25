package intercom.com.vn.spp.controller;

import org.springframework.web.bind.annotation.RestController;

import intercom.com.vn.spp.exception.ResourceNotFoundException;
import intercom.com.vn.spp.model.DailyReport;
import intercom.com.vn.spp.model.DailySatistic;
import intercom.com.vn.spp.model.Employee;
import intercom.com.vn.spp.model.EmployeeAggregate;
import intercom.com.vn.spp.model.Job;
import intercom.com.vn.spp.model.Problem;
import intercom.com.vn.spp.repository.DailyReportRepository;
import intercom.com.vn.spp.repository.EmployeeRepository;
import intercom.com.vn.spp.repository.JobRepository;
import intercom.com.vn.spp.repository.ProblemRepository;
import jakarta.validation.Valid;

import java.nio.DoubleBuffer;
import java.sql.Date;
import java.text.DecimalFormat;
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
                // kt nghi thi add vao danh sach nay
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
                                dr.setStartDateTime(dr.getStartDateTime() + "<br>--------<br>"
                                        + j.getStartDate().getHour() + ":" + j.getStartDate().getMinute());
                            dr.setDeployment(dr.getDeployment() + "<br>--------<br>" + j.getJobOfNetworkAndTD());
                            dr.setOtherWork(dr.getOtherWork() + "<br>--------<br>" + j.getNote());
                            dr.setProblem(dr.getProblem() + "<br>--------<br>" + j.getProblemInfo());
                            if (j.getDoneDate() != null)
                                dr.setDoneDatetime(dr.getDoneDatetime() + "<br>--------<br>" + j.getDoneDate().getHour()
                                        + ":" + j.getDoneDate().getMinute());
                            Problem prob = probRepo.findOneByScCode(j.getScCode());
                            if (prob != null)
                                dr.setResultAndApproach(
                                        dr.getResultAndApproach() + "<br>--------<br>" + prob.getResultAndSolution());
                            dr.setNote(dr.getNote() + "<br>--------<br>" + j.getNote());
                            dr.setWorkProcessDateTime(dr.getWorkProcessDateTime() + "<br>--------<br>"
                                    + j.getDoneHours() + ":" + j.getDoneMinutes());
                            if (j.getComebackOfficeDate() != null)
                                dr.setComebackofficeDatetime(dr.getComebackofficeDatetime() + "<br>--------<br>" +
                                        j.getComebackOfficeDate().getHour() + ":"
                                        + j.getComebackOfficeDate().getMinute());
                            else {
                                dr.setComebackofficeDatetime(
                                        dr.getComebackofficeDatetime() + "<br>--------<br>" + j.getNoComeBackWhy());
                            }
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
                    else {
                        dr.setComebackofficeDatetime(j.getNoComeBackWhy());
                    }
                    reportDaily.add(dr);
                }
            }
        }

        return reportDaily;
    }

    @GetMapping("/monthlyeports")
    public List<DailyReport> monthlyReport(@RequestParam Optional<Date> date) {
        List<DailyReport> reportDaily = new ArrayList<>();
        List<Job> dailyJobs = null;
        if (date.isEmpty()) {
            dailyJobs = jobRepository.findAllJobMonth();
        } else {
            dailyJobs = jobRepository.findAllJobMonth(date.get());
        }

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
                else {
                    dr.setComebackofficeDatetime(j.getNoComeBackWhy());
                }
                reportDaily.add(dr);
            }
        }
        return reportDaily;
    }

    private String addTime(String cur, String add) {
        var h = Integer.parseInt(cur.split(":")[0]);
        var m = Integer.parseInt(cur.split(":")[1]);
        var h2 = Integer.parseInt(add.split(":")[0]);
        var m2 = Integer.parseInt(add.split(":")[1]);
        h += h2;
        h += Math.floorDiv(m + m2, 60);
        m = Math.floorMod(m + m2, 60);
        return h + ":" + m;
    }

    @GetMapping("/employee-aggregate")
    public List<EmployeeAggregate> getMethodName(@RequestParam Optional<Date> date,
            @RequestParam Optional<Date> date1) {

        List<EmployeeAggregate> reports = new ArrayList<>();
        if (date.isEmpty() || date1.isEmpty())
            return reports;
        List<String> empCodes = new ArrayList<>();

        for (var j : jobRepository.findAllJobFromTo(date.get(), date1.get())) {
            if (!j.getEmployeeCode().isEmpty() && !j.getEmployeeCode().isBlank())
                for (String emCode : j.getEmployeeCode().split(",")) {
                    if (!j.getJobType().equals("Nghi")) {
                        Employee em = emRepo.findOneByCode(emCode);
                        if (empCodes.contains(emCode)) {
                            reports.forEach(ea -> {
                                if (ea.getEmCode().equals(emCode)) {
                                    ea.setTotalTime(addTime(ea.getTotalTime(), j.getDoneTime()));
                                    ea.setTotalProccessInTime(addTime(ea.getTotalProccessInTime(), j.getInTime()));
                                    ea.setTotalProccessOutTime(addTime(ea.getTotalProccessOutTime(), j.getOutTime()));
                                    if (j.getJobType().equals("TK"))
                                        ea.setSoLanTrienKhai(ea.getSoLanTrienKhai() + 1);
                                    if (j.getJobType().equals("SC")) {
                                        Problem prob = probRepo.findOneByScCode(j.getScCode());
                                        if (prob != null && prob.getProblemType() != null
                                                && !prob.getProblemType().isEmpty()
                                                && !prob.getProblemType().isBlank()) {
                                            if (prob.getProblemType().equals("Le")) {
                                                ea.setSolanSuCoLe(ea.getSolanSuCoLe() + 1);
                                                ea.setTotalSCLetProccessTime(
                                                        addTime(ea.getTotalSCLetProccessTime(), j.getDoneTime()));
                                            }

                                            else
                                                ea.setSoLanSuCoChum(ea.getSoLanSuCoChum() + 1);
                                        }
                                    }
                                    if (!j.getJobType().isEmpty() && j.getJobType().equals("Khac"))
                                        ea.setSolanCvKhac(ea.getSolanCvKhac() + 1);
                                }
                            });
                        } else {
                            empCodes.add(emCode);
                            EmployeeAggregate ea = new EmployeeAggregate();
                            ea.setEmCode(emCode);
                            ea.setDepartment(em.getDepartment());
                            ea.setTotalTime(j.getDoneTime());
                            ea.setTotalProccessInTime(j.getInTime());
                            ea.setTotalProccessOutTime(j.getOutTime());
                            if (j.getJobType().equals("TK"))
                                ea.setSoLanTrienKhai(1);
                            if (j.getJobType().equals("SC")) {
                                Problem prob = probRepo.findOneByScCode(j.getScCode());
                                if (prob != null && prob.getProblemType() != null && !prob.getProblemType().isEmpty()
                                        && !prob.getProblemType().isBlank()) {
                                    if (prob.getProblemType().equals("Le")) {
                                        ea.setSolanSuCoLe(1);
                                        ea.setTotalSCLetProccessTime(j.getDoneTime());
                                    }

                                    else
                                        ea.setSoLanSuCoChum(1);
                                }
                            }
                            if (!j.getJobType().isEmpty() && j.getJobType().equals("Khac"))
                                ea.setSolanCvKhac(1);

                            reports.add(ea);
                        }
                    }
                }
        }

        int totalTime = 0;
        int totalProccessInTime = 0;
        int totalProccessOutTime = 0;
        int tongSCLe = 0;
        int tongSCchum = 0;
        int tongSLTK = 0;
        int tongCVKhac = 0;
        for (var r : reports) {
            tongSCLe += r.getSolanSuCoLe();
            tongSCchum += r.getSoLanSuCoChum();
            tongSLTK += r.getSoLanTrienKhai();
            tongCVKhac += r.getSolanCvKhac();
            totalTime += Integer.parseInt(r.getTotalTime().split(":")[0]) * 60
                    + Integer.parseInt(r.getTotalTime().split(":")[1]);
            totalProccessInTime += Integer.parseInt(r.getTotalProccessInTime().split(":")[0]) * 60
                    + Integer.parseInt(r.getTotalProccessInTime().split(":")[1]);
            totalProccessOutTime += Integer.parseInt(r.getTotalProccessOutTime().split(":")[0]) * 60
                    + Integer.parseInt(r.getTotalProccessOutTime().split(":")[1]);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        for (var r : reports) {
            int rtotalTime = 0;
            if (r.getTotalTime() != null)
                rtotalTime = Integer.parseInt(r.getTotalTime().split(":")[0]) * 60
                        + Integer.parseInt(r.getTotalTime().split(":")[1]);

            int rtotalProccessInTime = 0;
            if (r.getTotalProccessInTime() != null)
                rtotalProccessInTime = Integer.parseInt(r.getTotalProccessInTime().split(":")[0]) * 60
                        + Integer.parseInt(r.getTotalProccessInTime().split(":")[1]);

            int rtotalProccessOutTime = 0;
            if (r.getTotalProccessOutTime() != null)
                rtotalProccessOutTime = Integer.parseInt(r.getTotalProccessOutTime().split(":")[0]) * 60
                        + Integer.parseInt(r.getTotalProccessOutTime().split(":")[1]);

            int rtotalSCLeProccessTime = 0;
            if (r.getTotalSCLetProccessTime() != null)
                rtotalSCLeProccessTime = Integer.parseInt(r.getTotalSCLetProccessTime().split(":")[0]) * 60
                        + Integer.parseInt(r.getTotalSCLetProccessTime().split(":")[1]);

            int rtotalSCLeProccessTimePerSCLe = 0;
            if (r.getSolanSuCoLe() > 0)
                rtotalSCLeProccessTimePerSCLe = (int) (Double.valueOf(rtotalSCLeProccessTime)
                        / Double.valueOf(r.getSolanSuCoLe()));

            r.setTyTrongTg(df.format((Double.valueOf(rtotalTime) * 100) / Double.valueOf(totalTime)) + "%");
            r.setTgtbXlScLe(Math.floorDiv(rtotalSCLeProccessTimePerSCLe, 60) + ":"
                    + Math.floorMod(rtotalSCLeProccessTimePerSCLe, 60));
            r.setTyTrongThucHien(df.format(Double
                    .valueOf(r.getSoLanSuCoChum() + r.getSoLanTrienKhai() + r.getSolanCvKhac() + r.getSolanSuCoLe())
                    / Double.valueOf(tongSLTK + tongSCLe + tongSCchum + tongCVKhac) * 100) + "%");
        }
        return reports;
    }

}
