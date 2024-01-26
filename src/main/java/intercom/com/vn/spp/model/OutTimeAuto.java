package intercom.com.vn.spp.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OutTimeAuto {
    private LocalDateTime date;
    private String emCode;
    private LocalDateTime startDateTime;
    private LocalDateTime doneDatetime;
    private LocalDateTime combackOffice;
    private String trienkhai;
    private String otherJob;
    private String problem;
    private String status;
    private String result;
    private String note;
    private String processTime;
    private String isOutTime;
    private String outTime;
    private Double level;
    private Double money;

}
