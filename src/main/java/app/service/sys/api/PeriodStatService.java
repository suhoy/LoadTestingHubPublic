package app.service.sys.api;

import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Stat_Time;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface PeriodStatService {

    void save(Period_Stat stat_time);
    void delete(Period_Stat stat_time);
    Period_Stat findById(Long id);
    List<Period_Stat> findAllByRunId(Long id);

}
