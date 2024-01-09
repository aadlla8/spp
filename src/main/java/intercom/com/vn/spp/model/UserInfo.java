package intercom.com.vn.spp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;
    @Column(name = "email", nullable = false, length = 50, unique = true)

    private String email;
    private String password;
    private String roles;
    private boolean active;
    @Column(name="create_date")
    private LocalDateTime createDate;
    public UserInfo(){
        this.setCreateDate(LocalDateTime.now());
        this.setActive(false);
    }

}