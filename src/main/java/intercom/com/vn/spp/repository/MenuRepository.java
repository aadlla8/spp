package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import intercom.com.vn.spp.model.Menu;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    
}
