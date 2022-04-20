package app.controller.front;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.System;
import app.service.sys.api.ReportService;
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

/**
 * @author Samuel Butta
 */
@FrontController
@PreAuthorize("hasRole('USER')")
public class ReportsController {


    private final ReportService reportServiceService;
    private final SystemService systemService;

    @Autowired
    public ReportsController(ReportService reportServiceService, SystemService systemService) {
        this.reportServiceService = reportServiceService;
        this.systemService = systemService;
    }

    @RequestMapping(value = {"/reports"})
    public String view(Model model, @RequestParam Long id, HttpServletRequest request) {
        //List<Report> reports = this.reportServiceService.
        System system = systemService.findSystem(id);
        //model.addAttribute("runs", runs);
        model.addAttribute("system", system);
        //model.addAttribute("df", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return "front/reports";
    }
}
