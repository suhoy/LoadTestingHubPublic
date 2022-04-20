package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Period_Stat;
import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.Stat_Time;
import app.service.sys.api.PeriodStatService;
import app.service.sys.api.RunService;
import app.service.sys.api.StatService;
import app.service.sys.api.StatTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@FrontController
@PreAuthorize("hasRole('USER')")
public class Stat_TimeController {

    private final RunService runService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;

    @Autowired
    public Stat_TimeController(RunService runService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }
/*
    @RequestMapping(value = "/stat_time/add", method = RequestMethod.POST)
    public String add(Model model, RedirectAttributes redirectAttributes,
                      @RequestParam("run_id") Long run_id,
                      @RequestParam("stat_time_stat_id") Long stat_time_stat_id,
                      @RequestParam("stat_time_tag") String stat_time_tag
    ){
        Stat stat = this.statService.findStatByStatId(stat_time_stat_id);
        Stat_Time stat_time = new Stat_Time();
        stat_time.setStat(stat);
        stat_time.setTag(stat_time_tag);
        this.statTimeService.save(stat_time);

        redirectAttributes.addFlashAttribute("msg", "Время [" + stat_time.getId() + "] успешно добавлено.");
        return "redirect:/run_edit?id=" + run_id;
    }*/

    @RequestMapping(value = "/stat_time/delete", method = RequestMethod.POST)
    public String delete(Model model, RedirectAttributes redirectAttributes,
                      @RequestParam("run_id") Long run_id,
                      @RequestParam("stat_time_id") Long stat_time_id
    ){
        Stat_Time stat_time = this.statTimeService.findById(stat_time_id);
        String stat_time_name = stat_time.getStat().getScript() +" - "+ stat_time.getTag();
        this.statTimeService.delete(stat_time);

        redirectAttributes.addFlashAttribute("msg", "Время [" + stat_time_name + "] успешно удалено.");
        return "redirect:/run_edit?id=" + run_id;
    }

    @RequestMapping(value = "/stat_time/update", method = RequestMethod.POST)
    public String update(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam("run_id") Long run_id,
                         @RequestParam("stat_time_id") Long stat_time_id,
                         @RequestParam("stat_time_tag") String stat_time_tag,
                         //@RequestParam("stat_time_sla") double stat_time_sla,
                         @RequestParam("stat_time_pass_time") double stat_time_pass_time,
                         @RequestParam("stat_time_fail_time") double stat_time_fail_time
    ){
        Stat_Time stat_time = this.statTimeService.findById(stat_time_id);
        stat_time.setTag(stat_time_tag);
        //stat_time.setSla(stat_time_sla);
        stat_time.setPass_time(stat_time_pass_time);
        stat_time.setFail_time(stat_time_fail_time);

        String stat_time_name = stat_time.getStat().getScript() +" - "+ stat_time.getTag();
        this.statTimeService.save(stat_time);

        redirectAttributes.addFlashAttribute("msg", "Время [" + stat_time_name + "] успешно удалено.");
        return "redirect:/run_edit?id=" + run_id;
    }






}
