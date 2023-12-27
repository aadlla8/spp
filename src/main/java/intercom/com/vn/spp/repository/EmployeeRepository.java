package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import intercom.com.vn.spp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    
}
