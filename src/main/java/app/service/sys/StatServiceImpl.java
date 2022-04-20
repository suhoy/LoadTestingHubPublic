package app.service.sys;

import app.service.sys.api.StatService;
import app.persistence.entity.sys.Stat;
import app.persistence.repository.sys.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class StatServiceImpl implements StatService {

    private StatRepository statRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Override
    public List<Stat> findStatsByRunId(Long id) {
        return this.statRepository.getStatsByRunId(id);
    }

    @Override
    public List<Stat> findAllStats() {
        return this.statRepository.findAll();
    }

    @Override
    public List<String> findDistinctTags(Long id) {
        return this.statRepository.getDistinctTagsByRunId(id);
    }
/*
    @Override
    public List<LocalDateTime[]> findDistinctPeriods(Long id) {
        return this.statRepository.findDistinctPeriods(id);
    }
*/
    @Override
    public void delete(Stat stat) {
        this.statRepository.delete(stat);
    }

    @Override
    public Stat findStatByStatId(Long id) {
        return this.statRepository.getStatById(id);
    }

    @Override
    public void save(Stat stat) {
        this.statRepository.save(stat);
    }
/*
    @Override
    public List<Stat> findStatParents(Long id) {
        return this.statRepository.findStatParents(id);
    }
*/
}
