package intercom.com.vn.spp.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "jobs")
public class Job {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    /*Ma su co */
    private String scCode;
    @Column(name = "date_issued")
    /*Ngay phat sinh cong viec */
    private Date dateIssued;
    /* Ngay ket thuc cong viec */
    @Column(name="date_end")
    private Date dateEnd;
    /*Mo ta cong viec */
    private String description;

    @Column(length = 10,name="problem_status")
    /*Trang thai su co Down Check Up Move*/
    private String problemStatus;

    /* Dau moi khach hang */
    @Column(name="customer_contact")
    private String customerContact;
    /*Loai dich vu Metronet P2P Internet KenhBacNam, HonHop, Khac*/
    @Column(name="service_type")
    private String serviceType;
    /*Hinh thuc bao su co */
    @Column(name="inform_method")
    private String informMethod;

    @Column(name="root_cause")
    /*Nguyen nhan */
    private String rootCause;

    private String note;
    @Column(name="employee_code")
    private String employeeCode;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="done_date")
    private Date doneDate;
    @Column(name="come_back_office_date")
    private Date comebackOfficeDate;

    /*Mo ta cong viec cua network va TD */
    private String jobOfNetworkAndTD;
}
