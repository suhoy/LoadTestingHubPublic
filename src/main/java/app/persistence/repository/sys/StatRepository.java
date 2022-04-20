package app.persistence.repository.sys;

import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    public Stat getStatById(Long id);

    @Query(value = "SELECT * FROM stat s Join period_stat ps on ps.id=s.period_stat_id  Where ps.run_id=?1 ORDER BY ps.tag", nativeQuery = true)
    public List<Stat> getStatsByRunId(Long id);

    @Query(value = "SELECT DISTINCT Stat.tag FROM Stat s Join period_stat ps on ps.id=s.period_stat_id WHERE ps.run_id = ?1 ORDER BY ps.tag", nativeQuery = true)
    public List<String> getDistinctTagsByRunId(Long id);

    /*
    @Query(value = "SELECT * FROM Stat s1 WHERE s1.id IN (SELECT inside.id FROM(SELECT id, time_start, time_finish FROM Stat WHERE RUN_ID = ?1 AND parent_id IS NULL GROUP BY time_start, time_finish, id ORDER BY time_start ASC) inside)", nativeQuery = true)
    List<Stat> findStatParents(Long id);


   @Query(value = "SELECT time_start, time_finish FROM Stat WHERE RUN_ID = ?1 GROUP BY time_start, time_finish ORDER BY time_start ASC", nativeQuery = true)
    List<LocalDateTime[]> findDistinctPeriods(Long id);*/

}
