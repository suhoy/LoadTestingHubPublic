package app.persistence.repository.sys;

import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.api.RunServiceI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface RunRepository extends JpaRepository<Run, Long> {

    //документация https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    public List<Run> findRunsBySystemIdOrderByIdDesc(Long id);

    public Run getRunById(Long id);

    public List<Run> findRunsBySystemIdAndVisibleIsTrue(Long id);

    public Run getRunByIdAndName(Long id, String name);

    public List<Run> findRunsBySystemIdAndNameContainingIgnoreCase(Long system_id, String name);

    //@Query(value = "SELECT Run.system_id as system_id, COUNT(Run.id) as run_count FROM Run WHERE Run.visible = TRUE GROUP BY Run.system_id", nativeQuery = true)
    //public List<RunServiceI> countRunsGroupedById();

    //@Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3", nativeQuery = true)
    //public List<Run> findRunsByTime_startBetweenAndSystem(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);

    @Query(value = "SELECT * FROM Run WHERE Run.time_start BETWEEN ?1 and ?2", nativeQuery = true)
    public List<Run> findByTime_startBetween(LocalDateTime time_start, LocalDateTime time_finish);


    //для календаря
    @Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3 and Run.visible is TRUE", nativeQuery = true)
    public List<Run> findRunsByTime_startBetweenAndSystemAAndVisibleIsTrue(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);


    //реализация для h2, для поиска запусков
    //@Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3 ORDER BY ?#{#column} ASC ", nativeQuery = true)
    //public List<Run> findRunsByTime_startBetweenAndSystemAndOrderASC(LocalDateTime time_start,LocalDateTime time_finish, Long system_id, String column);
    //@Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3 ORDER BY ?#{#column} DESC ", nativeQuery = true)
    //public List<Run> findRunsByTime_startBetweenAndSystemAndOrderDESC(LocalDateTime time_start,LocalDateTime time_finish, Long system_id, String column);

    //реализация для h2, для поиска запусков, которые можно привязать к отчёту
    //@Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3 and Run.report_id is null ORDER BY ?#{#column} ASC ", nativeQuery = true)
    //public List<Run> findRunsByTime_startBetweenAndSystemAndOrderASC_andReportIsNull(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column);
    //@Query(value = "SELECT * FROM Run WHERE (Run.time_start BETWEEN ?1 and ?2) and Run.system_id = ?3 and Run.report_id is null ORDER BY ?#{#column} DESC ", nativeQuery = true)
    //public List<Run> findRunsByTime_startBetweenAndSystemAndOrderDESC_andReportIsNull(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column);


    //для поиска запусков

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?1", nativeQuery = true)
    public Long countRunsBySystem(Long system_id);

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?3 and Run.time_start BETWEEN ?1 and ?2", nativeQuery = true)
    public Long countRunsBySystemAndTime(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?3 and Run.time_start BETWEEN ?1 and ?2 and Run.name LIKE ?4", nativeQuery = true)
    public Long countRunsBySystemAndTimeAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like);

    @Query(value = "SELECT * FROM Run WHERE Run.system_id = ?3 and Run.time_start BETWEEN ?1 and ?2", nativeQuery = true)
    public List<Run> findRunsBySystemAndTimeAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, Pageable pageable);

    @Query(value = "SELECT * FROM Run WHERE Run.system_id = ?3 and Run.time_start BETWEEN ?1 and ?2 and Run.name LIKE ?4", nativeQuery = true)
    public List<Run> findRunsBySystemAndTimeAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, Pageable pageable, String like);



    //для поиска запусков, которые можно привязать к отчёту

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?1 and Run.report_id is null", nativeQuery = true)
    public Long countRunsBySystemAndReportNull(Long system_id);

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?3 and Run.report_id is null and Run.time_start BETWEEN ?1 and ?2", nativeQuery = true)
    public Long countRunsBySystemAndTimeAndReportNull(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);

    @Query(value = "SELECT count(*) FROM Run WHERE Run.system_id = ?3 and Run.report_id is null and Run.time_start BETWEEN ?1 and ?2  and Run.name LIKE ?4", nativeQuery = true)
    public Long countRunsBySystemAndTimeAndReportNullAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like);

    @Query(value = "SELECT * FROM Run WHERE Run.system_id = ?3 and Run.report_id is null and Run.time_start BETWEEN ?1 and ?2 ", nativeQuery = true)
    public List<Run> findRunsBySystemAndTimeAndReportNullAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, Pageable pageable);

    @Query(value = "SELECT * FROM Run WHERE Run.system_id = ?3 and Run.report_id is null and Run.time_start BETWEEN ?1 and ?2  and Run.name LIKE ?4 ", nativeQuery = true)
    public List<Run> findRunsBySystemAndTimeAndReportNullAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, Pageable pageable, String like);


}
