package intercom.com.vn.spp.model;

import java.time.Duration;
import java.time.LocalDateTime;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /* Ma su co */
    @Nationalized
    @Column(name = "sc_code", length = 20)
    private String scCode;
    /* Người nhập */
    private String creator;
    /* ngày nhập liệu */
    @Column(name = "date_create")
    private LocalDateTime dateCreate;
    @Column(name = "date_issued")
    /* Ngay phat sinh cong viec */
    private LocalDateTime dateIssued;
    /* Ngay ket thuc cong viec */
    @Column(name = "date_end")
    private LocalDateTime dateEnd;
    /* Mo ta cong viec */
    @Column(columnDefinition = "TEXT")
    private String description;
    @Nationalized
    @Column(length = 10, name = "problem_status")
    /* Trang thai su co Down Check Up Move */
    private String problemStatus;

    /* Thong tin su co */
    @Nationalized
    @Column(name = "problem_info", columnDefinition = "TEXT")
    private String problemInfo;

    @Column(length = 30, name = "job_type")
    private String jobType;
    /* Dau moi khach hang */
    @Column(name = "customer_contact")
    @Nationalized
    private String customerContact;
    /* Loai dich vu Metronet P2P Internet KenhBacNam, HonHop, Khac */
    @Column(name = "service_type")
    @Nationalized
    private String serviceType;
    /* Hinh thuc bao su co */
    @Nationalized
    @Column(name = "inform_method", columnDefinition = "TEXT")
    private String informMethod;
    @Nationalized
    @Column(name = "root_cause", columnDefinition = "TEXT")
    /* Nguyen nhan */
    private String rootCause;
    @Nationalized
    private String note;
    @Column(name = "employee_code")
    private String employeeCode;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "done_date")
    private LocalDateTime doneDate;
    @Column(name = "come_back_office_date")
    private LocalDateTime comebackOfficeDate;
    @Nationalized
    @Column(name = "job_of_network_and_td", columnDefinition = "TEXT")
    /* Mo ta cong viec cua network va TD */
    private String jobOfNetworkAndTD;
    @Transient
    private long doneHours;
    @Transient
    private long doneMinutes;

    public long getDoneHours() {
        if (this.getStartDate() != null && this.getDoneDate() != null) {
            long totalMinutes = (Duration.between(this.getStartDate(), this.getDoneDate()).toMinutes());
            if (totalMinutes > 0) {
                return (Math.floorDiv(totalMinutes, 60));
            }
        }
        return 0;
    }

    public long getDoneMinutes() {
        if (this.getStartDate() != null && this.getDoneDate() != null) {
            long totalMinutes = (Duration.between(this.getStartDate(), this.getDoneDate()).toMinutes());
            if (totalMinutes > 0) {
                return (Math.floorMod(totalMinutes, 60));
            }
        }
        return 0;
    }

    @Transient
    private String functions;
    private String region;

    public Job() {
        this.setDateCreate(LocalDateTime.now());
    }
}
