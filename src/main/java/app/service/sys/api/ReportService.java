package app.service.sys.api;

import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.api.RunServiceI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
public interface ReportService {
    List<Report> findAllReportsBySystemId(Long id);
    List<Report> findVisibleReportsBySystemId(Long id);
    List<Report> findReportsBySystemIdAndVisibleIsTrueOrderByDate_createdAsc(Long id);
    Report getReportByReportId(Long id);

    void save (Report report);
    void delete (Report report);

    List<Report> findVisibleReportsBySystemIdAndTime(LocalDate time_start, LocalDate time_finish, Long system_id);
    // h2 List<Report> getRunsByTimeAndSystemOrder(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order);

    //для поиска отчётов
    Long countReportsBySystem(Long system_id);
    Long countReportsBySystemAndTime(LocalDate time_start, LocalDate time_finish, Long system_id);
    Long countReportsBySystemAndTimeAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, String like);
    List<Report> findReportsBySystemAndTimeAndOrder(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order, int page, int length);
    List<Report> findReportsBySystemAndTimeAndOrderAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order, int page, int length, String like);

}
