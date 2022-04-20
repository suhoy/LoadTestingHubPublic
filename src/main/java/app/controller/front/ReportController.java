package app.controller.front;

import app.config.anotation.FrontController;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.persistence.entity.sys.*;
import app.persistence.entity.sys.System;
import app.service.auth.api.UserService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@FrontController
public class ReportController {

    private final RunService runService;
    private final StatService statService;
    private final GraphService graphService;
    private final UserService userService;
    private final ReportService reportService;
    private final SystemService systemService;
    private final InfoService infoService;
    private final AttachService attachService;


    @Autowired
    public ReportController(RunService runService, StatService statService, GraphService graphService, UserService userService, ReportService reportService, SystemService systemService, InfoService infoService, AttachService attachService) {
        this.runService = runService;
        this.statService = statService;
        this.graphService = graphService;
        this.userService = userService;
        this.reportService = reportService;
        this.systemService = systemService;
        this.infoService = infoService;
        this.attachService = attachService;
    }

    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping("/report_view")
    public String view(Model model, @RequestParam Long id) {
        Report report = this.reportService.getReportByReportId(id);
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("report", report);
        return "front/report_view";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/run/remove")
    public String unattach(Model model, @RequestParam Long id, @RequestParam("run_id") Long run_id) {

        Report report = this.reportService.getReportByReportId(id);
        Run run = this.runService.findRun(run_id);

        report.getRuns().remove(run);
        this.reportService.save(report);

        run.setReport(null);
        this.runService.save(run);

        List<User> users = userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("report", report);
        model.addAttribute("users", users);
        //формат даты
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        model.addAttribute("msg", "Тест ["+run.getName()+"] успешно отвязан от теста.");
        return "front/report_edit";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/run/add")
    public String attach(Model model, @RequestParam Long id, @RequestParam("run_id") Long run_id) {

        Report report = this.reportService.getReportByReportId(id);
        Run run = this.runService.findRun(run_id);

        report.getRuns().add(run);
        this.reportService.save(report);

        run.setReport(report);
        this.runService.save(run);

        List<User> users = userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("report", report);
        model.addAttribute("users", users);
        //формат даты
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        model.addAttribute("msg", "Тест ["+run.getName()+"] успешно добавлен к тесту.");
        return "front/report_edit";
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report_edit")
    public String edit(Model model, @RequestParam Long id) {
        Report report = this.reportService.getReportByReportId(id);
        List<User> users = userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("report", report);
        model.addAttribute("users", users);
        //формат даты
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        if(!model.containsAttribute("msg")){
            model.addAttribute("msg", "Привет! Отчёт доступен на редактирование.");
        }
        return "front/report_edit";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/save")
    public String save(Model model, RedirectAttributes redirectAttributes, @RequestParam Long id, HttpServletRequest request, /*@RequestParam Long userID,*/ @RequestParam Boolean visible, @RequestParam String status, @RequestParam String name, @RequestParam("date_created") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_created) {
        Report report = this.reportService.getReportByReportId(id);
        //User user = userService.getUserById(userID);
        String email = request.getUserPrincipal().getName();
        User user = this.userService.findUsersByEmail(email);
        report.setVisible(visible);
        report.setStatus(status);
        report.setName(name);
        report.setDate_created(date_created);
        report.setUser(user);
        this.reportService.save(report);
        redirectAttributes.addFlashAttribute("msg", "Отчёт успешно сохранён.");
        return "redirect:/report_edit?id=" + id;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/add")
    public String add(Model model,RedirectAttributes redirectAttributes, HttpServletRequest request, @RequestParam("system_id") Long system_id, @RequestParam("report_name") String report_name) {
        System system = systemService.findSystem(system_id);

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        User user = this.userService.getUsersByEmail(name);

        Report report = new Report();
        report.setSystem(system);
        report.setName(report_name);
        report.setUser(user);
        report.setStatus("Создан");
        report.setDate_created(LocalDate.now());

        this.reportService.save(report);
        redirectAttributes.addFlashAttribute("msg", "Отчёт успешно создан.");
        return "redirect:/report_edit?id=" + report.getId();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/clone")
    public String clone(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam("report_id") Long report_id) {
        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        User user = this.userService.getUsersByEmail(name);
        Report originalRun = reportService.getReportByReportId(report_id);

        Report cloned = new Report();
        reportService.save(cloned);
        cloned.setSystem(originalRun.getSystem());
        cloned.setName("Клон " + originalRun.getName());
        cloned.setUser(user);
        cloned.setStatus("Склонирован");
        cloned.setDate_created(LocalDate.now());

        List<Info> clonedInfos = new ArrayList<>();
        for (Info info : originalRun.getInfos()) {

            Info newInfo = new Info();
            newInfo.setData(info.getData());
            newInfo.setTag(info.getTag());
            newInfo.setReport(cloned);
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
            newAttach.setReport(cloned);
            this.attachService.save(newAttach);
            clonedAttaches.add(newAttach);

        }
        cloned.setAttaches(clonedAttaches);

        redirectAttributes.addFlashAttribute("msg", "Отчёт успешно склонирован.");
        reportService.save(cloned);
        return "redirect:/report_edit?id=" + cloned.getId();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/report/delete")
    public String delete(Model model, @RequestParam Long id, @RequestParam("system.id") Long system_id, @RequestParam("question") String question) {
        Report report = this.reportService.getReportByReportId(id);
        for (Run run : report.getRuns()) {
            if (question.equals("УДАЛИТЬ")) {
                run.setReport(null);
                this.runService.save(run);
            } else if (question.equals("УДАЛИТЬ С ТЕСТАМИ")) {
                this.runService.delete(run.getId());
            }
        }
        this.reportService.delete(report);
        return "redirect:/system_view?id=" + system_id;
    }


}
