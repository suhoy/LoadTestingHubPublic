package app.controller.back.api;

import app.config.anotation.FrontController;
import app.persistence.entity.auth.User;
import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.System;
import app.service.auth.api.UserService;
import app.service.sys.api.ReportService;
import app.service.sys.api.RunService;
import app.service.sys.api.SystemService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@PreAuthorize("hasRole('VIEWER')")
public class ReportApiController {

    private final RunService runService;
    private final UserService userService;
    private final SystemService systemService;
    private final ReportService reportService;

    @Autowired
    public ReportApiController(RunService runService, UserService userService, SystemService systemService, ReportService reportService) {
        this.runService = runService;
        this.userService = userService;
        this.systemService = systemService;
        this.reportService = reportService;
    }


    @SneakyThrows
    @RequestMapping(value = "/api/get/reports/json/calendar", method = RequestMethod.GET)
    public String getRunsByTime(HttpServletRequest request, @RequestParam Long system_id, @RequestParam("start") String start, @RequestParam("end") String end) {
        //форматируем дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate time_start = LocalDate.parse(start, formatter);
        LocalDate time_finish = LocalDate.parse(end, formatter);
        //поиск видимых
        List<Report> reports = this.reportService.findVisibleReportsBySystemIdAndTime(time_start, time_finish, system_id);

        JSONArray ja = new JSONArray();
        for (Report report : reports) {
            JSONObject jo = new JSONObject();
            if (report.getStatus().contains("Неуспе") || report.getStatus().contains("неуспе") || report.getStatus().contains("дефект") || report.getStatus().contains("Дефект")) {
                jo.put("color", "#dc3545");
            } else if (report.getStatus().contains("Успе") || report.getStatus().contains("успе")) {
                jo.put("color", "#28a745");
            } else {
                jo.put("color", "#ffc107");
            }
            jo.put("title", report.getName()+". Статус: "+report.getStatus()+", Тестов: "+report.getRuns().size());
            jo.put("url", "/report_view?id=" + report.getId());
            jo.put("start", report.getDate_created().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            jo.put("end", report.getDate_created().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ja.put(jo);
        }
        return ja.toString().replaceAll("\\\\", "");
    }

    @SneakyThrows
    @RequestMapping(value = "/api/get/reports/json/list", method = RequestMethod.GET)
    public String getRunsByTimeList(HttpServletRequest request, @RequestParam Long system_id, @RequestParam Long draw, @RequestParam("time_start") String time_start, @RequestParam("time_end") String time_finish, @RequestParam(value = "search[value]") String search, @RequestParam(value = "order[0][column]") int column, @RequestParam(value = "order[0][dir]") String order, @RequestParam("length") int length, @RequestParam("start") int start) {
        //форматируем дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ltime_start = LocalDate.parse(time_start, formatter);
        LocalDate ltime_finish = LocalDate.parse(time_finish, formatter);

        long filtered = 0;
        int page = start / length;
        String columnString = new String[]{"id", "name", "visible", "status", "date_created"}[column];

        long findBySystem = this.reportService.countReportsBySystem(system_id);

        List<Report> reports;
        if (length == -1) {
            length = Integer.MAX_VALUE;
            page = 0;
        }

        if (search.isEmpty()) {
            //считаем сколько всего
            filtered = this.reportService.countReportsBySystemAndTime(ltime_start, ltime_finish, system_id);
            //получаем первую страницу
            reports = this.reportService.findReportsBySystemAndTimeAndOrder(ltime_start, ltime_finish, system_id, columnString, order, page, length);
        } else {
            //считаем сколько всего с учетом like
            filtered = this.reportService.countReportsBySystemAndTimeAndLike(ltime_start, ltime_finish, system_id, search);
            //получаем первую страницу с учетом like
            reports = this.reportService.findReportsBySystemAndTimeAndOrderAndLike(ltime_start, ltime_finish, system_id, columnString, order, page, length, search);
        }

        //поиск всех с сортировкой
        //List<Report> reports = this.reportService.getRunsByTimeAndSystemOrder(ltime_start, ltime_finish, system_id, columnString, order);

        JSONArray ja = new JSONArray();
        for (Report report : reports) {
            //if (report.getName().contains(search) && (length == -1 || (added <= length))) {
                JSONObject jo = new JSONObject();
                jo.put("id", report.getId());
                jo.put("title", report.getName());
                jo.put("visible", report.isVisible());
                jo.put("status", report.getStatus());
                jo.put("date", report.getDate_created().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                jo.put("runs", report.getRuns().size());
                jo.put("url", "/report_view?id=" + report.getId());
                ja.put(jo);
                //added++;
            //} else {
            //    filtered++;
            //}
        }

        JSONObject jodata = new JSONObject();
        jodata.put("data", ja);
        jodata.put("draw", draw);
        jodata.put("recordsTotal", findBySystem);
        jodata.put("recordsFiltered", filtered);
        return jodata.toString().replaceAll("\\\\", "");
    }


    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }


}
