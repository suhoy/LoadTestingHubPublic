package app.controller.back.api;

import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;
import app.service.sys.api.*;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@PreAuthorize("hasRole('USER')")
public class StatApiController {

    private final RunService runService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;
    private Logger logger = LoggerFactory.getLogger("debugger");

    @Autowired
    public StatApiController(RunService runService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }

    @RequestMapping(value = "/api/add/stats", method = RequestMethod.POST)
    public String addStats(HttpServletRequest request, @RequestParam Long run_id, @RequestBody List<Period_Stat> period_stats) throws Exception {
        Run run = this.runService.findRun(run_id);

        for (Period_Stat period_stat : period_stats) {
            period_stat.setRun(run);

            for (Stat stat : period_stat.getStats()) {
                stat.setPeriod_stat(period_stat);

                for (Stat_Time stat_time : stat.getStat_time()) {
                    stat_time.setStat(stat);
                    //this.statTimeService.save(stat_time);
                }

                if (stat.getChild_list() != null) {
                    for (Stat children : stat.getChild_list()) {
                        children.setParent(stat);
                        children.setPeriod_stat(period_stat);
                        for (Stat_Time stat_time : children.getStat_time()) {
                            stat_time.setStat(children);
                            //this.statTimeService.save(stat_time);
                        }
                    }
                }
            }
            this.periodStatService.save(period_stat);
        }

        return okResult();
    }


    @RequestMapping(value = "/api/get/stats", method = RequestMethod.GET)
    public List<Period_Stat> getStats(HttpServletRequest request, @RequestParam("run_id") Long run_id) {
        Run run = this.runService.findRun(run_id);
        return run.getPeriod_stats();
    }

    @RequestMapping(value = "/api/delete/stats", method = RequestMethod.DELETE)
    public String deleteAllStats(HttpServletRequest request, @RequestParam("run_id") Long run_id) {
        List<Period_Stat> period_stats = this.periodStatService.findAllByRunId(run_id);
        for (Period_Stat period_stat : period_stats) {
            this.periodStatService.delete(period_stat);
        }
        return okResult();
    }

    /*
        @RequestMapping(value = "/api/get/stat", method = RequestMethod.GET)
        public Stat getStat(HttpServletRequest request, @RequestParam Long stat_id) throws Exception {
            Stat stat = this.statService.findStatByStatId(stat_id);
            return stat;
        }

        @RequestMapping(value = "/api/get/stats/all", method = RequestMethod.GET)
        public List<Stat> getAllStats(HttpServletRequest request) {
            List<Stat> stats = this.statService.findAllStats();
            return stats;
        }

        @RequestMapping(value = "/api/delete/stats", method = RequestMethod.DELETE)
        public String deleteStat(HttpServletRequest request, @RequestBody List<Stat> stats) throws JSONException {
            for (Stat stat : stats) {
                this.statService.delete(this.statService.findStatByStatId(stat.getId()));
            }
            return okResult();
        }


    */
    @SneakyThrows
    private String okResult() {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }
}