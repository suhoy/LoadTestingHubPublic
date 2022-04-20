package app.persistence.repository.sys;


import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    //While “find” might lead to no result at all, “get” will always return something – otherwise the JPA repository throws an exception

    public Report getReportById(Long id);
    public List<Report> findReportsBySystemIdAndVisibleIsTrue(Long id);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?1 and Report.visible is TRUE ORDER BY Report.date_created ASC", nativeQuery = true)
    public List<Report> findReportsBySystemIdAndVisibleIsTrueOrderByDate_createdAsc(Long id);

    public List<Report> findReportsBySystemId(Long id);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2 and Report.visible is TRUE", nativeQuery = true)
    public List<Report> findReportsBySystemIdAndVisibleIsTrueAndDate_createdIsBetween(LocalDate time_start, LocalDate time_finish, Long system_id);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2 ORDER BY ?#{#column} ASC", nativeQuery = true)
    public List<Report> findReportsByDate_createdBetweenAndSystemIdIsAndOrderASC(LocalDate time_start, LocalDate time_finish, Long system_id, String column);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2 ORDER BY ?#{#column} DESC", nativeQuery = true)
    public List<Report> findReportsByDate_createdBetweenAndSystemIdIsAndOrderDESC(LocalDate time_start, LocalDate time_finish, Long system_id, String column);


    //для поиска отчётов

    @Query(value = "SELECT count(*) FROM Report WHERE Report.system_id = ?1", nativeQuery = true)
    public Long countReportsBySystem(Long system_id);

    @Query(value = "SELECT count(*) FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2", nativeQuery = true)
    public Long countReportsBySystemAndTime(LocalDate time_start, LocalDate time_finish, Long system_id);

    @Query(value = "SELECT count(*) FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2 and Report.name LIKE ?4", nativeQuery = true)
    public Long countReportsBySystemAndTimeAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, String like);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2", nativeQuery = true)
    public List<Report> findReportsBySystemAndTimeAndOrder(LocalDate time_start, LocalDate time_finish, Long system_id, Pageable pageable);

    @Query(value = "SELECT * FROM Report WHERE Report.system_id = ?3 and Report.date_created BETWEEN ?1 and ?2 and Report.name LIKE ?4", nativeQuery = true)
    public List<Report> findReportsBySystemAndTimeAndOrderAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, Pageable pageable, String like);

}
