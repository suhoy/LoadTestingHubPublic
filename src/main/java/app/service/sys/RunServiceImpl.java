package app.service.sys;

import app.service.sys.api.RunService;
import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.RunRepository;
import app.persistence.repository.sys.api.RunServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Service
@Transactional
public class RunServiceImpl implements RunService {

    @Autowired
    private RunRepository runRepository;

    @Autowired
    public RunServiceImpl(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    @Override
    public List<Run> findAllRunsBySystemId(Long id) {
        return this.runRepository.findRunsBySystemIdOrderByIdDesc(id);
    }

    @Override
    public Run findRun(Long id) {
        return this.runRepository.getRunById(id);
    }

    @Override
    public List<Run> findVisibleRuns(Long id) {
        return this.runRepository.findRunsBySystemIdAndVisibleIsTrue(id);
    }

    @Override
    public List<Run> findAll() {
        return this.runRepository.findAll();
    }

    @Override
    public List<Run> findRunsByNameIsLike(Long system_id, String name) {
        return this.runRepository.findRunsBySystemIdAndNameContainingIgnoreCase(system_id, name);
    }

    @Override
    public void save(Run run) {
        this.runRepository.save(run);
    }

    //3 step
    @Override
    public void delete(Long id) {
        this.runRepository.deleteById(id);
    }

    /*@Override
    public List<RunServiceI> countRunsGroupedById() {
        return this.runRepository.countRunsGroupedById();
    }*/
    /*@Override
    public List<Run> getRunsByTimeAndSystem(LocalDateTime time_start, LocalDateTime time_finish, Long system_id) {
        return this.runRepository.findRunsByTime_startBetweenAndSystem(time_start, time_finish, system_id);
    }*/

    //для api
    @Override
    public Run getRunByIdAndName(Long id, String name) {
        return this.runRepository.getRunByIdAndName(id, name);
    }

    //для api
    @Override
    public List<Run> getRunsByTime(LocalDateTime time_start, LocalDateTime time_finish) {
        return this.runRepository.findByTime_startBetween(time_start, time_finish);
    }

    //для календаря
    @Override
    public List<Run> getRunsByTimeAndSystemAndVisibleIsTrue(LocalDateTime time_start, LocalDateTime time_finish, Long system_id) {
        return this.runRepository.findRunsByTime_startBetweenAndSystemAAndVisibleIsTrue(time_start, time_finish, system_id);
    }


    //для поиска запусков  vvvvvvvvvv
    @Override
    public Long countRunsBySystem(Long system_id) {
        return this.runRepository.countRunsBySystem(system_id);
    }

    @Override
    public Long countRunsBySystemAndTime(LocalDateTime time_start, LocalDateTime time_finish, Long system_id) {
        return this.runRepository.countRunsBySystemAndTime(time_start, time_finish, system_id);
    }

    @Override
    public Long countRunsBySystemAndTimeAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like) {
        return this.runRepository.countRunsBySystemAndTimeAndLike(time_start, time_finish, system_id, "%" + like + "%");
    }

    @Override
    public List<Run> findRunsBySystemAndTimeAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length) {
        if (order.equalsIgnoreCase("asc")) {
            return this.runRepository.findRunsBySystemAndTimeAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column));
            // h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderASC(time_start, time_finish, system_id, column);
        } else {
            return this.runRepository.findRunsBySystemAndTimeAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column));
            // h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderDESC(time_start, time_finish, system_id, column);
        }
    }

    @Override
    public List<Run> findRunsBySystemAndTimeAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length, String like) {
        if (order.equalsIgnoreCase("asc")) {
            return this.runRepository.findRunsBySystemAndTimeAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column), "%" + like + "%");
            // h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderASC(time_start, time_finish, system_id, column);
        } else {
            return this.runRepository.findRunsBySystemAndTimeAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column), "%" + like + "%");
            // h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderDESC(time_start, time_finish, system_id, column);
        }
    }
    //для поиска запусков  ^^^^^^^^^^



    //для поиска запусков, которые можно привязать к отчёту vvvvvvvvvv
    @Override
    public Long countRunsBySystemAndReportNull(Long system_id) {
        return this.runRepository.countRunsBySystemAndReportNull(system_id);
    }

    @Override
    public Long countRunsBySystemAndTimeAndReportNull(LocalDateTime time_start, LocalDateTime time_finish, Long system_id) {
        return this.runRepository.countRunsBySystemAndTimeAndReportNull(time_start, time_finish, system_id);
    }

    @Override
    public Long countRunsBySystemAndTimeAndReportNullAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String like) {
        return this.runRepository.countRunsBySystemAndTimeAndReportNullAndLike(time_start, time_finish, system_id, "%" + like + "%");
    }

    @Override
    public List<Run> findRunsBySystemAndTimeAndReportNullAndOrder(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length) {
        if (order.equalsIgnoreCase("asc")) {
            return this.runRepository.findRunsBySystemAndTimeAndReportNullAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column));
            //h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderASC_andReportIsNull(time_start, time_finish, system_id, column);
        } else {
            return this.runRepository.findRunsBySystemAndTimeAndReportNullAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column));
            //h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderDESC_andReportIsNull(time_start, time_finish, system_id, column);

        }
    }

    @Override
    public List<Run> findRunsBySystemAndTimeAndReportNullAndOrderAndLike(LocalDateTime time_start, LocalDateTime time_finish, Long system_id, String column, String order, int page, int length, String like) {
        if (order.equalsIgnoreCase("asc")) {
            return this.runRepository.findRunsBySystemAndTimeAndReportNullAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column), "%" + like + "%");
            //h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderASC_andReportIsNull(time_start, time_finish, system_id, column);
        } else {
            return this.runRepository.findRunsBySystemAndTimeAndReportNullAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column), "%" + like + "%");
            //h2 return this.runRepository.findRunsByTime_startBetweenAndSystemAndOrderDESC_andReportIsNull(time_start, time_finish, system_id, column);
        }
    }
    //для поиска запусков, которые можно привязать к отчёту  ^^^^^^^^^^

}
