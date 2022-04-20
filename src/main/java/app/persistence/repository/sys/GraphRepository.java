package app.persistence.repository.sys;

import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface GraphRepository extends JpaRepository<Graph, Long> {

    public List<Graph> getGraphsByRunIdOrderByTagAscAboutAsc(Long id);
    public Graph getGraphById(Long id);

    @Query(value = "SELECT DISTINCT Graph.tag FROM Graph WHERE Graph.run_id = ?1 ORDER BY Graph.tag", nativeQuery = true)
    public List<String> getDistinctTagsByRunIdOOrderByTagAsc(Long id);

}
