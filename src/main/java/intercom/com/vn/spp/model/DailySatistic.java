package intercom.com.vn.spp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DailySatistic {
    /*Chua duoc giao cong viec */
    private List<String> noJob;
    /* da ve van phong */
    private List<String> backOffice;
    /* chua ve van phong */
    private List<String> notyetBackOffice;
    /* kt khong ve van phong, ve nha luon */
    private List<String> notBackOffice;
    /*Nghi phep, nghi truc NOC */
    private List<String> notAtNoc;
    public DailySatistic(){
        this.noJob = new ArrayList<>();
        this.backOffice = new ArrayList<>();
        this.notyetBackOffice = new ArrayList<>();
        this.notBackOffice = new ArrayList<>();
        this.notAtNoc = new ArrayList<>();
    }
}
