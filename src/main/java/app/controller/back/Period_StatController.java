package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Run;
import app.service.sys.api.PeriodStatService;
import app.service.sys.api.RunService;
import app.service.sys.api.StatService;
import app.service.sys.api.StatTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;


@FrontController
@PreAuthorize("hasRole('USER')")
public class Period_StatController {

    private final RunService runService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;
    private Logger logger = LoggerFactory.getLogger("debugger");

    @Autowired
    public Period_StatController(RunService runService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }

    @RequestMapping(value = "/period_stat/update", method = RequestMethod.POST)
    public String update(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                         @RequestParam("period_id") Long period_id,
                         @RequestParam("period_about") String period_about,
                         @RequestParam("period_profile") double period_profile,
                         @RequestParam("period_tps") double period_tps,
                         @RequestParam("period_time_start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime period_time_start,
                         @RequestParam("period_time_finish") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime period_time_finish
                         ){
        Period_Stat period_stat = this.periodStatService.findById(period_id);

        period_stat.setAbout(period_about);
        period_stat.setProfile(period_profile);
        period_stat.setTps(period_tps);
        period_stat.setTime_start(period_time_start);
        period_stat.setTime_finish(period_time_finish);
        this.periodStatService.save(period_stat);

        redirectAttributes.addFlashAttribute("msg", "Период [" + period_stat.getId() + "] успешно обновлён.");
        return "redirect:/run_edit?id=" + run_id;

    }

    @RequestMapping(value = "/period_stat/delete", method = RequestMethod.POST)
    public String delete(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                         @RequestParam("period_id") Long period_id
    ){
        Period_Stat period_stat = this.periodStatService.findById(period_id);
        this.periodStatService.delete(period_stat);
        redirectAttributes.addFlashAttribute("msg", "Период [" + period_stat.getId() + "] успешно удалён.");
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/period_stat/add", method = RequestMethod.POST)
    public String add(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                      @RequestParam("period_about") String period_about
    ){
        Run run = this.runService.findRun(run_id);
        Period_Stat period_stat = new Period_Stat();
        period_stat.setRun(run);
        period_stat.setAbout(period_about);
        this.periodStatService.save(period_stat);
        redirectAttributes.addFlashAttribute("msg", "Период [" + period_stat.getId() + "] успешно добавлен.");
        return "redirect:/run_edit?id=" + run_id;
    }



/*
    @RequestMapping(value = "/stat/update", method = RequestMethod.POST)
    public String update(Model model, RedirectAttributes redirectAttributes, @RequestParam("stat_id") Long stat_id, @RequestParam("run_id") Long run_id,
                         @RequestParam("stat_tag") String stat_tag, @RequestParam("stat_script") String stat_script, @RequestParam("stat_sla") double stat_sla,
                         @RequestParam("stat_profile") int stat_profile, @RequestParam("stat_pass_count") int stat_pass_count,
                         @RequestParam("stat_fail_count") int stat_fail_count, @RequestParam("stat_pass_time") double stat_pass_time, @RequestParam("stat_fail_time") double stat_fail_time) {
        Stat stat = statService.findStatByStatId(stat_id);

        String statinfo = stat_tag + " - " + stat_script;
        redirectAttributes.addFlashAttribute("msg", "Статистика [" + statinfo + "] успешно обновлена.");
        return updateStat(model, redirectAttributes, run_id, stat_tag, stat_script, stat_sla, stat_profile, stat_pass_count, stat_fail_count, stat_pass_time, stat_fail_time, stat);
    }



    //ПЕРЕДЕЛАТЬ
    @RequestMapping(value = "/stat/add", method = RequestMethod.POST)
    public String add(Model model, RedirectAttributes redirectAttributes, @RequestParam("run_id") Long run_id,
                      @RequestParam("stat_tag") String stat_tag, @RequestParam("stat_script") String stat_script, @RequestParam("stat_sla") double stat_sla,
                      @RequestParam("stat_profile") int stat_profile, @RequestParam("stat_pass_count") int stat_pass_count,
                      @RequestParam("stat_fail_count") int stat_fail_count, @RequestParam("stat_pass_time") double stat_pass_time, @RequestParam("stat_fail_time") double stat_fail_time) {
        Run run = runService.findRun(run_id);
        Stat stat = new Stat();
        //stat.setRun(run);
        String statinfo = stat_tag + " - " + stat_script;
        redirectAttributes.addFlashAttribute("msg", "Статистика [" + statinfo + "] успешно добавлена.");
        return updateStat(model, redirectAttributes, run_id, stat_tag, stat_script, stat_sla, stat_profile, stat_pass_count, stat_fail_count, stat_pass_time, stat_fail_time, stat);
    }

    private String updateStat(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("run_id") Long run_id,
            @RequestParam("stat_tag") String stat_tag, @RequestParam("stat_script") String stat_script, @RequestParam("stat_sla") double stat_sla,
            @RequestParam("stat_profile") int stat_profile, @RequestParam("stat_pass_count") int stat_pass_count,
            @RequestParam("stat_fail_count") int stat_fail_count, @RequestParam("stat_pass_time") double stat_pass_time, @RequestParam("stat_fail_time") double stat_fail_time,
            Stat stat) {


        //stat.setScript(stat_script);
        //stat.setProfile(stat_profile);
        stat.setPass_count(stat_pass_count);
        stat.setFail_count(stat_fail_count);


        //------------------
                Stat_Time stat_time = stat.getStat_time().get(0);
                stat_time.setSla(stat_sla);
                stat_time.setTag(stat_tag);
                stat_time.setPass_time(stat_pass_time);
                stat_time.setFail_time(stat_fail_time);
                this.statTimeService.save(stat_time);
        //------------------

        this.statService.save(stat);
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/stat/delete", method = RequestMethod.POST)
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam("stat_id") Long stat_id, @RequestParam("run_id") Long run_id) {
        Stat stat = statService.findStatByStatId(stat_id);
        //String statinfo = stat.getScript();
        String statinfo = ""+stat.getId();
        this.statService.delete(stat);

        redirectAttributes.addFlashAttribute("msg", "Статистика [" + statinfo + "] успешно удалена.");
        return "redirect:/run_edit?id=" + run_id;
    }

    */

}
