package app.persistence.repository.sys;

import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {

    public List<Info> getInfosByRunId(Long id);
    public Info getInfoById(Long id);

}
