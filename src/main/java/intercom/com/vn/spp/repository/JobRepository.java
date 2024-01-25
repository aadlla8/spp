package intercom.com.vn.spp.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import intercom.com.vn.spp.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
//
    @Query(value = "select * from jobs b where date(b.date_create) = curdate() order by date_create", nativeQuery = true)
    List<Job> findAllJobDaily();
    @Query(value = "select * from jobs b where date(b.date_create) = :date order by date_create", nativeQuery = true)
    List<Job> findAllJobDaily(Date date);
    @Query(value = "select * from jobs b where date_format(b.date_create,'%Y%m') = date_format(curdate(),'%Y%m') order by date_create", nativeQuery = true)
    List<Job> findAllJobMonth();
}
