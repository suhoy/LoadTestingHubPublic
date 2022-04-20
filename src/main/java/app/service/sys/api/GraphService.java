package app.service.sys.api;

import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface GraphService {

    List<Graph> findGraphsByRunId(Long id);
    List<String> findDistinctTags(Long id);
    void delete (Graph graph);
    void save (Graph graph);
    Graph findGraphByGraphId(Long id);
    Graph store (MultipartFile file, Run run, String tag, String about);

}
