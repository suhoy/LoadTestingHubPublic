package app.controller.front;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.System;
import app.service.sys.api.RunService;
import app.service.sys.api.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;

@FrontController
public class SystemController {

    private final RunService runService;
    private final SystemService systemService;

    @Autowired
    public SystemController(RunService runService, SystemService systemService) {
        this.runService = runService;
        this.systemService = systemService;
    }

    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping(value = {"/system_view"})
    public String view(Model model, @RequestParam Long id, HttpServletRequest request) {

        System system = systemService.findSystem(id);
        //List<Run> runs = runService.findVisibleRuns(id);

        //model.addAttribute("runs", runs);
        model.addAttribute("system", system);

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return "front/system_view";
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @RequestMapping(value = {"/system_edit"})
    public String edit(Model model, @RequestParam Long id, HttpServletRequest request) {

        System system = systemService.findSystem(id);
        model.addAttribute("system", system);

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return "front/system_edit";
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @RequestMapping(value = {"/system/save"})
    public String save(Model model, @RequestParam Long id, HttpServletRequest request, @RequestParam String about, @RequestParam String runs_about, @RequestParam String reports_about, @RequestParam String name) {
        System system = systemService.findSystem(id);

        if (about.isEmpty()) {
            system.setAbout(null);
        } else {
            system.setAbout(about);
        }
        if (runs_about.isEmpty()) {
            system.setRuns_about(null);
        } else {
            system.setRuns_about(runs_about);
        }

        if (reports_about.isEmpty()) {
            system.setReports_about(null);
        } else {
            system.setReports_about(reports_about);
        }


        system.setName(name);
        this.systemService.save(system);

        model.addAttribute("system", system);
        model.addAttribute("df", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        return "front/system_edit";
    }

}
