package intercom.com.vn.spp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import intercom.com.vn.spp.model.Job;

public interface JobRepository extends JpaRepository<Job,Long> {
    
    @Query(value="select * from jobs b where date(b.date_create) = curdate()", nativeQuery = true)
    List<Job> findAllJobDaily();
}
