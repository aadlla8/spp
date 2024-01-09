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
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "employee_code", nullable = false, length = 30, unique = true)
    private String code;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email_address", nullable = false)
    private String emailId;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "phone_number1")
    private String phoneNumber1;
    @Column(name = "phone_number2")
    private String phoneNumber2;
    private String department;
    private String note;
    private String cccd;
    @Transient
    private String functions;

    private LocalDateTime dateCreate;

    public Employee() {
        this.setDateCreate(LocalDateTime.now());
    }
}
