package app.service.sys;

import app.service.sys.api.GraphService;
import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.GraphRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class GraphServiceImpl implements GraphService {

    private GraphRepository graphRepository;

    @Autowired
    public GraphServiceImpl(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    @Override
    public List<Graph> findGraphsByRunId(Long id) {
        return this.graphRepository.getGraphsByRunIdOrderByTagAscAboutAsc(id);
    }

    @Override
    public List<String> findDistinctTags(Long id)
    {
        return this.graphRepository.getDistinctTagsByRunIdOOrderByTagAsc(id);
    }

    @Override
    public void delete(Graph graph) {
        this.graphRepository.delete(graph);
    }

    @Override
    public void save(Graph graph) {
        this.graphRepository.save(graph);
    }

    @SneakyThrows
    @Override
    public Graph store(MultipartFile file, Run run, String tag, String about) {
        Graph newGraph = new Graph();
        newGraph.setName(file.getOriginalFilename());
        newGraph.setTag(tag);
        newGraph.setType(file.getContentType());
        newGraph.setData(file.getBytes());
        newGraph.setAbout(about);
        newGraph.setRun(run);

        this.graphRepository.save(newGraph);
        return newGraph;
    }

    @Override
    public Graph findGraphByGraphId(Long id) {
        return this.graphRepository.getGraphById(id);
    }
}
