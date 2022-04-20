package app.service.sys;

import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Stat_Time;
import app.persistence.repository.sys.PeriodStatRepository;
import app.persistence.repository.sys.StatRepository;
import app.persistence.repository.sys.StatTimeRepository;
import app.service.sys.api.PeriodStatService;
import app.service.sys.api.StatTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class PeriodStatServiceImpl implements PeriodStatService {

    private PeriodStatRepository periodStatRepository;

    @Autowired
    public PeriodStatServiceImpl(PeriodStatRepository periodStatRepository) {
        this.periodStatRepository = periodStatRepository;
    }

    @Override
    public void save(Period_Stat period_stat) {
        this.periodStatRepository.save(period_stat);
    }

    @Override
    public void delete(Period_Stat period_stat) {
        this.periodStatRepository.delete(period_stat);
    }

    @Override
    public Period_Stat findById(Long id) {
        return this.periodStatRepository.getOne(id);
    }

    @Override
    public List<Period_Stat> findAllByRunId(Long id) {
        return this.periodStatRepository.findAllByRunId(id);
    }


}
