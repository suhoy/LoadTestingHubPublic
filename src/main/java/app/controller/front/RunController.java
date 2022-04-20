package app.controller.front;

import app.persistence.entity.sys.System;
import app.service.auth.api.UserService;
import app.config.anotation.FrontController;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.persistence.entity.sys.*;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
//import org.apache.commons.io.IOUtils;

/**
 * @author https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/
 */
@FrontController
public class RunController {

    //private static final Logger LOG = LoggerFactory.getLogger(RunController.class);
    private final RunService runService;
    private final UserService userService;
    private final SystemService systemService;
    private final GraphService graphService;
    private final InfoService infoService;
    private final AttachService attachService;
    private final StatService statService;
    private final StatTimeService statTimeService;
    private final PeriodStatService periodStatService;


    @Autowired
    public RunController(RunService runService, UserService userService, SystemService systemService, GraphService graphService, InfoService infoService, AttachService attachService, StatService statService, StatTimeService statTimeService, PeriodStatService periodStatService) {
        this.runService = runService;
        this.userService = userService;
        this.systemService = systemService;
        this.graphService = graphService;
        this.infoService = infoService;
        this.attachService = attachService;
        this.statService = statService;
        this.statTimeService = statTimeService;
        this.periodStatService = periodStatService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/run/save")
    public String save(Model model, RedirectAttributes redirectAttributes, @RequestParam Long id, HttpServletRequest request,/*@RequestParam Long userID,*/@RequestParam Boolean visible, @RequestParam String status, @RequestParam String name, @RequestParam("time_start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time_start, @RequestParam("time_finish") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time_finish/*, @RequestParam MultipartFile attach*/) {
        Run run = runService.findRun(id);
        //User user = userService.getUserById(userID);
        String email = request.getUserPrincipal().getName();
        User user = this.userService.findUsersByEmail(email);
        run.setVisible(visible);
        run.setStatus(status);
        run.setName(name);
        run.setTime_start(time_start);
        run.setTime_finish(time_finish);
        run.setUser(user);
        this.runService.save(run);
        redirectAttributes.addFlashAttribute("msg", "Тест успешно сохранён.");
        return "redirect:/run_edit?id=" + id;
    }

/*
    @PreAuthorize("hasRole('DEVELOPER')")
    @RequestMapping("/run/edit")
    public String edit(Model model, @RequestParam Long id) {
        Run run = runService.findRun(id);
        return "redirect:/run?id=" + id;
    }
*/

    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping("/run_view")
    public String runView(Model model, @RequestParam Long id) {
        Run run = runService.findRun(id);
        //List<Stat> stats = statService.findStatParents(id);
        //List<LocalDateTime[]> stat_periods = statService.findDistinctPeriods(id);
        List<User> users = userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("run", run);
        model.addAttribute("users", users);
        //model.addAttribute("stats", stats);
        //model.addAttribute("stat_periods", stat_periods);
        //формат даты
        model.addAttribute("df", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        model.addAttribute("tsf", new SimpleDateFormat("HH:mm dd.MM.yyyy"));

        return "front/run_view";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/run_edit")
    public String runEdit(Model model, @RequestParam Long id) {
        Run run = runService.findRun(id);
        List<User> users = userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("run", run);
        model.addAttribute("users", users);
        if (!model.containsAttribute("msg")) {
            model.addAttribute("msg", "Привет! Тест доступен на редактирование.");
        }

        //формат даты
        model.addAttribute("df", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        return "front/run_edit";
    }


    //4 step
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/run/delete")
    public String delete(Model model, @RequestParam Long id, @RequestParam("system.id") Long system_id) {
        runService.delete(id);
        return "redirect:/system_view?id=" + system_id;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/run/add")
    public String add(Model model, HttpServletRequest request, @RequestParam("system_id") Long system_id, @RequestParam("run_name") String run_name) {
        System system = this.systemService.findSystem(system_id);
        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        User user = this.userService.getUsersByEmail(name);
        Run run = new Run();
        run.setName(run_name);
        run.setUser(user);
        run.setSystem(system);
        run.setStatus("Создан");
        run.setTime_start(LocalDateTime.now());
        run.setTime_finish(LocalDateTime.now());
        runService.save(run);
        return "redirect:/run_edit?id=" + run.getId();
    }


    //https://javarevisited.blogspot.com/2014/03/how-to-clone-collection-in-java-deep-copy-vs-shallow.html
    //https://stackoverflow.com/questions/1692871/found-shared-references-to-a-collection-org-hibernate-hibernateexception
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/run/clone")
    public String clone(Model model, HttpServletRequest request, @RequestParam("run_id") Long run_id) {
        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        User user = this.userService.getUsersByEmail(name);
        Run originalRun = runService.findRun(run_id);

        Run cloned = new Run();
        runService.save(cloned);
        cloned.setSystem(originalRun.getSystem());
        cloned.setName("Клон " + originalRun.getName());
        cloned.setUser(user);
        cloned.setStatus("Склонирован");
        cloned.setTime_finish(originalRun.getTime_finish());
        cloned.setTime_start(originalRun.getTime_start());
        List<Graph> clonedGraphs = new ArrayList<>();

        for (Graph graph : originalRun.getGraphs()) {
            Graph newGraph = new Graph();
            newGraph.setName(graph.getName());
            newGraph.setTag(graph.getTag());
            newGraph.setType(graph.getType());
            newGraph.setData(graph.getData());
            newGraph.setAbout(graph.getAbout());
            newGraph.setRun(cloned);
            graphService.save(newGraph);
            clonedGraphs.add(newGraph);
        }
        cloned.setGraphs(clonedGraphs);


        //cloned.setPeriod_stats(new ArrayList<Period_Stat>());
        //List<Stat> clonedStats = new ArrayList<>();
        List<Period_Stat> clonedPeriod_Stats = new ArrayList<>();
        for (Period_Stat period_stat : originalRun.getPeriod_stats()) {
            Period_Stat newPeriod = new Period_Stat();
            List<Stat> clonnedStats = new ArrayList<Stat>();
            //newPeriod.setStats(new ArrayList<Stat>());

            newPeriod.setRun(cloned);
            newPeriod.setAbout(period_stat.getAbout());
            newPeriod.setProfile(period_stat.getProfile());
            newPeriod.setTime_finish(period_stat.getTime_finish());
            newPeriod.setTime_start(period_stat.getTime_start());
            newPeriod.setTps(period_stat.getTps());

            this.periodStatService.save(newPeriod);

            for (Stat stat : period_stat.getOnlyParents()) {
                Stat newStat = new Stat();
                List<Stat_Time> clonnedStat_Times = new ArrayList<Stat_Time>();

                newStat.setPeriod_stat(newPeriod);
                newStat.setScript(stat.getScript());
                newStat.setProfile(stat.getProfile());
                newStat.setPass_count(stat.getPass_count());
                newStat.setFail_count(stat.getFail_count());
                newStat.setSla(stat.getSla());
                this.statService.save(newStat);

                for (Stat_Time stat_time : stat.getStat_time()) {
                    Stat_Time new_stat_time = new Stat_Time();
                    new_stat_time.setStat(newStat);
                    new_stat_time.setTag(stat_time.getTag());
                    new_stat_time.setPass_time(stat_time.getPass_time());
                    new_stat_time.setFail_time(stat_time.getFail_time());

                    this.statTimeService.save(new_stat_time);
                    clonnedStat_Times.add(new_stat_time);

                }

                if (!stat.getChild_list().isEmpty()) {
                    List<Stat> child_list = new ArrayList<>();
                    for (Stat child : stat.getChild_list()) {
                        Stat newStatchild = new Stat();
                        List<Stat_Time> clonnedStat_Times_child = new ArrayList<Stat_Time>();

                        newStatchild.setPeriod_stat(newPeriod);
                        newStatchild.setParent(newStat);
                        newStatchild.setScript(child.getScript());
                        newStatchild.setProfile(child.getProfile());
                        newStatchild.setPass_count(child.getPass_count());
                        newStatchild.setFail_count(child.getFail_count());
                        newStatchild.setSla(child.getSla());
                        this.statService.save(newStatchild);

                        for (Stat_Time stat_time : child.getStat_time()) {
                            Stat_Time new_stat_time = new Stat_Time();
                            new_stat_time.setStat(newStatchild);
                            new_stat_time.setTag(stat_time.getTag());
                            new_stat_time.setPass_time(stat_time.getPass_time());
                            new_stat_time.setFail_time(stat_time.getFail_time());

                            this.statTimeService.save(new_stat_time);
                            clonnedStat_Times_child.add(new_stat_time);

                        }

                        newStatchild.setPeriod_stat(newPeriod);
                        newStatchild.setStat_time(clonnedStat_Times_child);
                        this.statService.save(newStatchild);
                        //clonnedStats.add(newStatchild);
                        child_list.add(newStatchild);
                    }
                    newStat.setChild_list(child_list);
                }


                newStat.setPeriod_stat(newPeriod);
                newStat.setStat_time(clonnedStat_Times);
                this.statService.save(newStat);
                clonnedStats.add(newStat);

            }

            newPeriod.setStats(clonnedStats);
            this.periodStatService.save(newPeriod);
            clonedPeriod_Stats.add(newPeriod);

        }
        cloned.setPeriod_stats(clonedPeriod_Stats);


        List<Info> clonedInfos = new ArrayList<>();
        for (Info info : originalRun.getInfos()) {

            Info newInfo = new Info();
            newInfo.setData(info.getData());
            newInfo.setTag(info.getTag());
            newInfo.setRun(cloned);
            this.infoService.save(newInfo);
            clonedInfos.add(newInfo);
        }
        cloned.setInfos(clonedInfos);


        List<Attach> clonedAttaches = new ArrayList<>();
        for (Attach attach : originalRun.getAttaches()) {
            Attach newAttach = new Attach();
            newAttach.setName(attach.getName());
            newAttach.setTag(attach.getTag());
            newAttach.setType(attach.getType());
            newAttach.setData(attach.getData());
            newAttach.setRun(cloned);
            this.attachService.save(newAttach);
            clonedAttaches.add(newAttach);

        }
        cloned.setAttaches(clonedAttaches);

        runService.save(cloned);
        return "redirect:/run_edit?id=" + cloned.getId();
    }


}
