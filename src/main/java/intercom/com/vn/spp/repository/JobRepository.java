package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import intercom.com.vn.spp.model.Job;

public interface JobRepository extends JpaRepository<Job,Long> {
    
}
