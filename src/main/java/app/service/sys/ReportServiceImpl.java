package app.service.sys;

import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.ReportRepository;
import app.service.sys.api.ReportService;
import app.service.sys.api.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {


    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    @Override
    public List<Report> findAllReportsBySystemId(Long id) {
        return this.reportRepository.findReportsBySystemId(id);
    }

    @Override
    public List<Report> findVisibleReportsBySystemId(Long id) {
        return this.reportRepository.findReportsBySystemId(id);
    }

    @Override
    public List<Report> findReportsBySystemIdAndVisibleIsTrueOrderByDate_createdAsc(Long id) {
        return this.reportRepository.findReportsBySystemIdAndVisibleIsTrueOrderByDate_createdAsc(id);
    }

    @Override
    public Report getReportByReportId(Long id) {
        return this.reportRepository.getReportById(id);
    }

    @Override
    public void save(Report report) {
        this.reportRepository.save(report);
    }

    @Override
    public void delete(Report report) {
        this.reportRepository.delete(report);
    }

    @Override
    public List<Report> findVisibleReportsBySystemIdAndTime(LocalDate time_start, LocalDate time_finish, Long system_id) {
        return this.reportRepository.findReportsBySystemIdAndVisibleIsTrueAndDate_createdIsBetween(time_start,time_finish,system_id);
    }

    /* h2
    @Override
    public List<Report> getRunsByTimeAndSystemOrder(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order) {
        if (order.equalsIgnoreCase("asc"))
            return this.reportRepository.findReportsByDate_createdBetweenAndSystemIdIsAndOrderASC(time_start, time_finish, system_id, column);
        else {
            return this.reportRepository.findReportsByDate_createdBetweenAndSystemIdIsAndOrderDESC(time_start, time_finish, system_id, column);
        }
    }*/

    //для поиска отчётов  vvvvvvvvvv
    @Override
    public Long countReportsBySystem(Long system_id) {
        return this.reportRepository.countReportsBySystem(system_id);
    }

    @Override
    public Long countReportsBySystemAndTime(LocalDate time_start, LocalDate time_finish, Long system_id) {
        return this.reportRepository.countReportsBySystemAndTime(time_start, time_finish, system_id);
    }

    @Override
    public Long countReportsBySystemAndTimeAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, String like) {
        return this.reportRepository.countReportsBySystemAndTimeAndLike(time_start, time_finish, system_id, "%" + like + "%");
    }

    @Override
    public List<Report> findReportsBySystemAndTimeAndOrder(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order, int page, int length) {
        if (order.equalsIgnoreCase("asc")) {
            return this.reportRepository.findReportsBySystemAndTimeAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column));
        } else {
            return this.reportRepository.findReportsBySystemAndTimeAndOrder(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column));
        }
    }

    @Override
    public List<Report> findReportsBySystemAndTimeAndOrderAndLike(LocalDate time_start, LocalDate time_finish, Long system_id, String column, String order, int page, int length, String like) {
        if (order.equalsIgnoreCase("asc")) {
            return this.reportRepository.findReportsBySystemAndTimeAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.ASC, column), "%" + like + "%");
        } else {
            return this.reportRepository.findReportsBySystemAndTimeAndOrderAndLike(time_start, time_finish, system_id, PageRequest.of(page, length, Sort.Direction.DESC, column), "%" + like + "%");
        }
    }
    //для поиска отчётов  ^^^^^^^^^^

}
