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
import intercom.com.vn.spp.model.Problem;
import intercom.com.vn.spp.repository.ProblemRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin()
@RequestMapping("/api/v1")
public class ProblemController {
    @Autowired
    private ProblemRepository problemRepository;

    @GetMapping("/problems")
    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<Problem> getById(@PathVariable(value = "id") Long problemId)
            throws ResourceNotFoundException {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Sự cố not found for this id:: " + problemId));
        return ResponseEntity.ok().body(problem);
    }

    @PostMapping("/problems")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Problem create(@Valid @RequestBody Problem problem, @AuthenticationPrincipal UserInfoDetails uInfo) {
        problem.setCreator(uInfo.getUsername());
        return problemRepository.save(problem);
    }

    @PutMapping("/problems/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Problem> update(@PathVariable(value = "id") Long problemId,
            @Valid @RequestBody Problem problemDetails)
            throws ResourceNotFoundException {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Sự cố not found for this id:: " + problemId));
        problem.setScCode(problemDetails.getScCode());
        problem.setStartDate(problemDetails.getStartDate());
        problem.setEndDate(problemDetails.getEndDate());
        problem.setDoneDate(problemDetails.getDoneDate());
        problem.setCustomerCode(problemDetails.getCustomerCode());
        problem.setCustomerContact(problemDetails.getCustomerContact());
        problem.setInformMethod(problemDetails.getInformMethod());
        problem.setNocAndTechWorks(problemDetails.getNocAndTechWorks());
        problem.setResultAndSolution(problemDetails.getResultAndSolution());
        problem.setUnitProcess(problemDetails.getUnitProcess());
        problem.setRootCause(problemDetails.getRootCause());
        problem.setInfo(problemDetails.getInfo());
        problem.setStatus(problemDetails.getStatus());
        problem.setServiceType(problemDetails.getServiceType());
        problem.setRegion(problemDetails.getRegion());
        problem.setTechnicalStart(problemDetails.getTechnicalStart());
        problem.setTechnicalDone(problemDetails.getTechnicalDone());

        problemRepository.save(problem);

        return ResponseEntity.ok(problem);
    }

    @DeleteMapping("/problems/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sự cố not found for this id:: " + id));
        problemRepository.delete(problem);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
