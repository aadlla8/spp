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
    /* thời gian băts đầu */
    @Column(name = "start_date")
    private LocalDateTime startDate;
    /* thời gian hoàn thành xong */
    @Column(name = "done_date")
    private LocalDateTime doneDate;
    /* thời gian trở về văn phòng */
    @Column(name = "come_back_office_date")
    private LocalDateTime comebackOfficeDate;
    /*Ly do khong ve van phong */
    @Column(name="no_comeback_why")
    private String noComeBackWhy;
    @Nationalized
    @Column(name = "job_of_network_and_td", columnDefinition = "TEXT")
    /* Mo ta cong viec cua network va TD */
    private String jobOfNetworkAndTD;
    @Transient
    private long doneHours;
    @Transient
    private long doneMinutes;
    /* thời gian ngoài giờ */
    @Transient
    private String outTime;

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

    public String getOutTime() {
        if (this.getDoneDate() != null && this.getDoneDate().getHour() > 18) {

            long outH = this.getDoneDate().getHour() - 18;
            long outM = this.getDoneDate().getMinute();
            return String.format("%s:%s", outH, outM);
        } else
            return "";
    }

    @Transient
    private String functions;
    private String region;

    public Job() {
        this.setDateCreate(LocalDateTime.now());
    }
}
