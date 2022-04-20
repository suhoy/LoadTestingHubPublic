package app.controller.front;

import app.service.sys.api.RunService;
import app.service.sys.api.SystemService;
import app.config.anotation.FrontController;
import app.persistence.entity.sys.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import app.persistence.entity.sys.System;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Samuel Butta
 */
@FrontController
@PreAuthorize("hasRole('USER')")
public class RunsController {


    private final RunService runService;
    private final SystemService systemService;

    @Autowired
    public RunsController(RunService runService, SystemService systemService) {
        this.runService = runService;
        this.systemService = systemService;
    }

    @RequestMapping(value = {"/runs"})
    public String view(Model model, @RequestParam Long id, HttpServletRequest request) {
        //List<Run> runs = runService.findAllRunsBySystemId(id);
        System system = systemService.findSystem(id);
        //model.addAttribute("runs", runs);
        model.addAttribute("system", system);
        //model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return "front/runs";
    }
}
