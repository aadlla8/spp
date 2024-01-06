package intercom.com.vn.spp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import intercom.com.vn.spp.model.DailyReport;

public interface DailyReportRepository extends JpaRepository<DailyReport,Long> {
    
}
