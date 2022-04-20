package app.service.sys.api;

import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.System;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface StatService {

    List<Stat> findStatsByRunId(Long id);
    List<Stat> findAllStats();
    Stat findStatByStatId(Long id);
    void delete (Stat stat);

    List<String> findDistinctTags(Long id);
    //List<LocalDateTime[]> findDistinctPeriods(Long id);

    void save (Stat stat);

    //List<Stat> findStatParents(Long id);

}
