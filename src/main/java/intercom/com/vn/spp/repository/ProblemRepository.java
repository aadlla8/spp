package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import intercom.com.vn.spp.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem,Long> {

    Problem findOneByScCode(String scCode);
    
    @Query(value="select max(id)+1 as id from problems p", nativeQuery = true)
    Integer getMaxId();
    
}
