package app.controller.back.api;

import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;
import app.service.sys.api.PeriodStatService;
import app.service.sys.api.RunService;
import app.service.sys.api.StatService;
import app.service.sys.api.StatTimeService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@PreAuthorize("hasRole('USER')")
public class Period_StatApiController {

    private final RunService runService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;
    private Logger logger = LoggerFactory.getLogger("debugger");

    @Autowired
    public Period_StatApiController(RunService runService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }

    @RequestMapping(value = "/api/period_stat/get", method = RequestMethod.GET)
    public List<Period_Stat> get(HttpServletRequest request, @RequestParam("run_id") Long run_id) {
        Run run = this.runService.findRun(run_id);
        return run.getPeriod_stats();
    }

    @RequestMapping(value = "/api/period_stat/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request, @RequestParam("run_id") Long run_id, @RequestBody List<Period_Stat> period_stats) {
        Run run = this.runService.findRun(run_id);
        for (Period_Stat period_stat : period_stats) {
            period_stat.setRun(run);
            this.periodStatService.save(period_stat);
        }
        return okResult();
    }

    @RequestMapping(value = "/api/period_stat/delete", method = RequestMethod.DELETE)
    public String add(HttpServletRequest request, @RequestParam("period_stat_id") Long period_stat_id) {
        Period_Stat period_stat = this.periodStatService.findById(period_stat_id);
        this.periodStatService.delete(period_stat);
        return okResult();
    }



    @SneakyThrows
    private String okResult() {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }
}