package app.service.sys.api;

import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.api.RunServiceI;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Sukhorukov Sergei
 */
public interface RunService {

    List<Run> findRunsByNameIsLike(Long system_id, String name);
    List<Run> findAllRunsBySystemId(Long id);
    List<Run> findVisibleRuns(Long id);
    List<Run> findAll();

    //2 step
    void delete(Long id);
    void save (Run run);
    Run findRun (Long id);


    //для api
    Run getRunByIdAndName(Long id,String name);

    //для api
    List<Run> getRunsByTime(LocalDateTime time_start, LocalDateTime time_finish);

    //List<Run> getRunsByTimeAndSystem(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);
    //List<Run> getRunsByTimeAndSystemOrderByIdAndWithNullReport(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order);
    //List<RunServiceI> countRunsGroupedById();


    // для календаря
    List<Run> getRunsByTimeAndSystemAndVisibleIsTrue(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);



    //для поиска запусков
    Long countRunsBySystem(Long system_id);
    Long countRunsBySystemAndTime(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);
    Long countRunsBySystemAndTimeAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like);
    List<Run> findRunsBySystemAndTimeAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length);
    List<Run> findRunsBySystemAndTimeAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length, String like);

    //для поиска запусков, которые можно привязать к отчёту
    Long countRunsBySystemAndReportNull(Long system_id);
    Long countRunsBySystemAndTimeAndReportNull(LocalDateTime time_start, LocalDateTime time_finish, Long system_id);
    Long countRunsBySystemAndTimeAndReportNullAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like);
    List<Run> findRunsBySystemAndTimeAndReportNullAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length);
    List<Run> findRunsBySystemAndTimeAndReportNullAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length, String like);





}
