package app.persistence.repository.sys;

import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface StatTimeRepository extends JpaRepository<Stat_Time, Long> {

}
