package app.persistence.repository.sys;

import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface AttachRepository extends JpaRepository<Attach, Long> {

    public List<Attach> getAttachesByRunId(Long id);
    public Attach getAttachById(Long id);

}
