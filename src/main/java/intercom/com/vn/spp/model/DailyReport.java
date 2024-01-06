package intercom.com.vn.spp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "daily_report")
@Data
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "department", nullable = false)
    private String department;
    @Column(name = "employee_code", nullable = false)
    private String employeeCode;
    @Column(name = "start_dateTime", nullable = false)
    private LocalDateTime startDateTime;
    @Column(name = "deployment", nullable = false)
    private String deployment;
    @Column(name = "other_work")
    private String otherWork;
    @Column(name = "problem")
    private String problem;
    @Column(name = "done_datetime")
    private LocalDateTime doneDatetime;
    @Column(name = "comebackoffice_datetime")
    private String comebackofficeDatetime;
    @Column(name = "result_and_approach")
    private String resultAndApproach;
    private String note;
    @Column(name="work_process_datetime")
    private LocalDateTime workProcessDateTime;
    private String status;
    @Transient
    private String functions;
    private LocalDateTime dateCreate;

    public DailyReport() {
        this.setDateCreate(LocalDateTime.now());
    }
}
