package app.controller.back.api;

import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import app.service.auth.api.UserService;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@PreAuthorize("hasRole('USER')")
public class GraphApiController {

    private final RunService runService;
    private final GraphService graphService;

    @Autowired
    public GraphApiController(RunService runService, GraphService graphService) {
        this.runService = runService;
        this.graphService = graphService;

    }

    @RequestMapping(value = "/api/add/graph", method = RequestMethod.POST)
    public String addGraph(HttpServletRequest request, @RequestParam Long run_id, @RequestParam("data") MultipartFile data, @RequestParam String tag, @RequestParam String about) throws Exception {
        Run run = this.runService.findRun(run_id);
        this.graphService.store(data, run, tag, about);
        return okResult();
    }

    @RequestMapping(value = "/api/get/graphs", method = RequestMethod.GET)
    public List<Graph> getGraph(HttpServletRequest request, @RequestParam Long run_id) throws Exception {
        Run run = this.runService.findRun(run_id);
        return run.getGraphs();
    }

    @RequestMapping(value = "/api/delete/graphs/all", method = RequestMethod.DELETE)
    public String deleteAllGraphByRunId(HttpServletRequest request, @RequestParam Long run_id) throws JSONException {
        List<Graph> graphs = this.graphService.findGraphsByRunId(run_id);
        for (Graph graph : graphs) {
            this.graphService.delete(graph);
        }
        return okResult();
    }

    @RequestMapping(value = "/api/delete/graphs", method = RequestMethod.DELETE)
    public String deleteGraphs(HttpServletRequest request, @RequestBody List<Graph> graphs) throws JSONException {
        for (Graph graph : graphs) {
            this.graphService.delete(this.graphService.findGraphByGraphId(graph.getId()));
        }
        return okResult();
    }

    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }


}