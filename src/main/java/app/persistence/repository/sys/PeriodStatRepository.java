package app.persistence.repository.sys;

import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Stat_Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface PeriodStatRepository extends JpaRepository<Period_Stat, Long> {

    List<Period_Stat> findAllByRunId(Long id);
}
