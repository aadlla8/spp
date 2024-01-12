package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import intercom.com.vn.spp.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem,Long> {

    Problem findOneByScCode(String scCode);
    
}
