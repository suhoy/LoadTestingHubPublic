package app.controller.back.api;

import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Run;
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
public class AttachApiController {

    private final RunService runService;
    private final AttachService attachService;



    @Autowired
    public AttachApiController(RunService runService, AttachService attachService) {
        this.runService = runService;
        this.attachService = attachService;

    }

    @RequestMapping(value = "/api/add/attach", method = RequestMethod.POST)
    public String addGraph(HttpServletRequest request, @RequestParam Long run_id, @RequestParam("data") MultipartFile data, @RequestParam String tag) throws Exception {
        Run run = this.runService.findRun(run_id);
        this.attachService.store(data, run, tag);
        return okResult();
    }

    @RequestMapping(value = "/api/get/attaches", method = RequestMethod.GET)
    public List<Attach> getAttaches(HttpServletRequest request, @RequestParam Long run_id) throws Exception {
        Run run = this.runService.findRun(run_id);
        return run.getAttaches();
    }

    @RequestMapping(value = "/api/delete/attaches/all", method = RequestMethod.DELETE)
    public String deleteAllAttachesByRunId(HttpServletRequest request, @RequestParam Long run_id) throws JSONException {
        List<Attach> attaches = this.attachService.findAttachesByRunId(run_id);
        for (Attach attach : attaches) {
            this.attachService.delete(attach);
        }
        return okResult();
    }

    @RequestMapping(value = "/api/delete/attaches", method = RequestMethod.DELETE)
    public String deleteAttaches(HttpServletRequest request, @RequestBody List<Attach> attaches) throws JSONException {
        for (Attach attach : attaches) {
            this.attachService.delete(this.attachService.findAttachByAttachId(attach.getId()));
        }
        return okResult();
    }

    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }


}