package app.service.sys.api;

import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;

import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface StatTimeService {

    void save(Stat_Time stat_time);
    void delete(Stat_Time stat_time);

    Stat_Time findById(Long id);
}
