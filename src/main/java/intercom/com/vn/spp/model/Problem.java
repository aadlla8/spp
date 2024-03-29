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
@Table(name = "problems")
@Data
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creator;
    private LocalDateTime createDate;
    @Nationalized
    @Column(name = "sc_code", length = 20, unique = true)
    private String scCode;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    /* Đơn vị xử lý sc*/
    @Column(name = "unit_proccess")
    @Nationalized
    private String unitProcess;
    /* Thoi gian hoàn thành KT*/
    @Column(name = "done_date")
    private LocalDateTime doneDate;
    /* Thoi gian kết thúc sc */
    @Column(name = "end_date")
    private LocalDateTime endDate;
    /*tg ky thuat xuat phat */
    @Column(name = "technical_start")
    private LocalDateTime technicalStart;
    /*thoi gian hoan thanh KT */
    @Column(name = "technical_done")
    private LocalDateTime technicalDone;
    /* Trạng thái */
    private String status;
    private String problemType;
    /* Thông tin sự cố */
    @Nationalized
    @Column(columnDefinition = "TEXT")
    private String info;
    /* Mã khách hàng */
    @Column(name = "customer_code")
    private String customerCode;
    /* đầu mối trao đổi khách hàng */
    @Nationalized
    @Column(name = "customer_contact")
    private String customerContact;
    @Column(name = "service_type")
    private String serviceType;
    @Nationalized
    @Column(name = "inform_method", columnDefinition = "TEXT")
    private String informMethod;
    @Nationalized
    @Column(name = "root_cause", columnDefinition = "TEXT")
    private String rootCause;
    @Nationalized
    @Column(name = "noc_and_tech_works", columnDefinition = "TEXT")
    private String nocAndTechWorks;
    @Column(name = "result_and_solution", columnDefinition = "TEXT")
    @Nationalized
    private String resultAndSolution;
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

    public Problem() {
        this.setCreateDate(LocalDateTime.now());
         
    }
}
