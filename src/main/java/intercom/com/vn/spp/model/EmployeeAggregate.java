package intercom.com.vn.spp.model;

import java.time.LocalDateTime;
 
import lombok.Data;

@Data
public class EmployeeAggregate {
    private String department;
    private String emCode;
    private String totalTime;
    private String totalProccessInTime;
    private String totalProccessOutTime;
    private String totalSCLetProccessTime;
    private int soLanTrienKhai;
    private int solanSuCoLe;
    private int soLanSuCoChum;
    private int solanCvKhac;
    private String tgtbXlScLe;
    /*ty trong thoi gian */
    private String tyTrongTg;
    private String tyTrongThucHien; 
    private LocalDateTime dateCreate; 
    public EmployeeAggregate() {
        this.setDateCreate(LocalDateTime.now());
    }
}
