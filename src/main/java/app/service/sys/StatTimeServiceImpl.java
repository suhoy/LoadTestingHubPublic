package app.service.sys;

import app.persistence.entity.sys.Stat_Time;
import app.persistence.repository.sys.StatTimeRepository;
import app.service.sys.api.StatTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class StatTimeServiceImpl implements StatTimeService {

    private StatTimeRepository statTimeRepository;

    @Autowired
    public StatTimeServiceImpl(StatTimeRepository statTimeRepository) {
        this.statTimeRepository = statTimeRepository;
    }

    @Override
    public void save(Stat_Time stat_time) {
        this.statTimeRepository.save(stat_time);
    }

    @Override
    public void delete(Stat_Time stat_time) {
        this.statTimeRepository.delete(stat_time);
    }

    @Override
    public Stat_Time findById(Long id) {
        return this.statTimeRepository.getOne(id);
    }


}
