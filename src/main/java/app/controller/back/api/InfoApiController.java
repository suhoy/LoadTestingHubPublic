package app.controller.back.api;

import app.persistence.entity.sys.Run;
import app.service.auth.api.UserService;
import app.service.sys.api.*;

import app.persistence.entity.sys.Info;
import app.persistence.entity.sys.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@PreAuthorize("hasRole('USER')")
public class InfoApiController {

    private final RunService runService;
    private final InfoService infoService;

    @Autowired
    public InfoApiController(RunService runService, InfoService infoService) {
        this.runService = runService;
        this.infoService = infoService;
    }

    @RequestMapping(value = "/api/get/infos/all", method = RequestMethod.GET)
    public List<Info> getAllInfos(HttpServletRequest request) {
        List<Info> infos = this.infoService.findAllInfos();
        return infos;
    }

    @RequestMapping(value = "/api/get/infos", method = RequestMethod.GET)
    public List<Info> getAllInfos(HttpServletRequest request, @RequestParam Long run_id) {
        List<Info> infos = this.infoService.findInfosByRunId(run_id);
        return infos;
    }

    @RequestMapping(value = "/api/delete/infos/all", method = RequestMethod.DELETE)
    public String deleteAllInfos(HttpServletRequest request, @RequestParam Long run_id) throws JSONException {
        List<Info> infos = this.infoService.findInfosByRunId(run_id);
        for (Info info : infos) {
            this.infoService.delete(info);
        }
        return okResult();
    }

    @RequestMapping(value = "/api/delete/infos", method = RequestMethod.DELETE)
    public String deleteInfos(HttpServletRequest request, @RequestBody List<Info> infos) throws JSONException {
        for (Info info : infos) {
            this.infoService.delete(this.infoService.getInfoByInfoId(info.getId()));
        }
        return okResult();
    }

    @RequestMapping(value = "/api/add/infos", method = RequestMethod.POST)
    public String addStats(HttpServletRequest request, @RequestParam Long run_id, @RequestBody List<Info> infos) throws Exception {
        Run run = this.runService.findRun(run_id);
        for (Info info : infos) {
            info.setRun(run);
            this.infoService.save(info);
        }
        return okResult();
    }

    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }

}
