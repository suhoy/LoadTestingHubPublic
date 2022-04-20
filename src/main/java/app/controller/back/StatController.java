package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@FrontController
@PreAuthorize("hasRole('USER')")
public class StatController {

    private final RunService runService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;

    @Autowired
    public StatController(RunService runService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }

    @RequestMapping(value = "/stat/add", method = RequestMethod.POST)
    public String add(Model model, RedirectAttributes redirectAttributes,
                      @RequestParam("run_id") Long run_id,
                      @RequestParam("period_id") Long period_id
    ){
        Period_Stat period_stat = this.periodStatService.findById(period_id);
        Stat stat = new Stat();
        stat.setPeriod_stat(period_stat);
        this.statService.save(stat);
        redirectAttributes.addFlashAttribute("msg", "Скрипт [" + stat.getId() + "] успешно добавлен.");
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/stat/delete", method = RequestMethod.POST)
    public String delete(Model model, RedirectAttributes redirectAttributes,
                      @RequestParam("run_id") Long run_id,
                      @RequestParam("stat_id") Long stat_id
    ){
        Stat stat = this.statService.findStatByStatId(stat_id);
        String stat_name = stat.getScript();
        this.statService.delete(stat);
        redirectAttributes.addFlashAttribute("msg", "Скрипт [" + stat_name + "] успешно удалён.");
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/stat/update", method = RequestMethod.POST)
    public String update(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                         @RequestParam("stat_id") Long stat_id,
                         @RequestParam("stat_script") String stat_script,
                         @RequestParam("stat_profile") int stat_profile,
                         @RequestParam("stat_sla") double stat_sla,
                         @RequestParam("stat_pass_count") int stat_pass_count,
                         @RequestParam("stat_fail_count") int stat_fail_count
    ){
        Stat stat = this.statService.findStatByStatId(stat_id);
        stat.setScript(stat_script);
        stat.setProfile(stat_profile);
        stat.setPass_count(stat_pass_count);
        stat.setFail_count(stat_fail_count);
        stat.setSla(stat_sla);
        this.statService.save(stat);

        redirectAttributes.addFlashAttribute("msg", "Скрипт [" + stat.getScript() + "] успешно обновлён.");
        return "redirect:/run_edit?id=" + run_id;
    }


    @RequestMapping(value = "/stat/add/child", method = RequestMethod.POST)
    public String add_child(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                         @RequestParam("stat_id") Long stat_id
    ){
        Stat parent = this.statService.findStatByStatId(stat_id);
        Stat child = new Stat();

        child.setParent(parent);
        child.setPeriod_stat(parent.getPeriod_stat());
        child.setScript(parent.getScript()+"_child");

        this.statService.save(child);

        redirectAttributes.addFlashAttribute("msg", "Подскрипт  для скрипта[" + parent.getScript() + "] успешно обновлён.");
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/stat/add/time", method = RequestMethod.POST)
    public String add_time(Model model, RedirectAttributes redirectAttributes,
                            @RequestParam("run_id") Long run_id,
                            @RequestParam("stat_id") Long stat_id
    ){
        Stat stat = this.statService.findStatByStatId(stat_id);
        Stat_Time stat_time = new Stat_Time();
        stat_time.setStat(stat);
        this.statTimeService.save(stat_time);

        redirectAttributes.addFlashAttribute("msg", "Время для скрипта [" +  stat.getScript()  + "] успешно обновлено.");
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
