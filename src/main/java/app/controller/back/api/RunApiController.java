package app.controller.back.api;

import app.service.auth.api.UserService;
import app.service.sys.api.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;
import app.persistence.entity.auth.User;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.System;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
public class RunApiController {

    private final RunService runService;
    private final UserService userService;
    private final SystemService systemService;

    @Autowired
    public RunApiController(RunService runService, UserService userService, SystemService systemService) {
        this.runService = runService;
        this.userService = userService;
        this.systemService = systemService;

    }

    @RequestMapping(value = "/api/add/run", method = RequestMethod.POST)
    public Run addRun(HttpServletRequest request, @RequestBody Run run, @RequestParam Long system_id) throws Exception {
        System system = this.systemService.findSystem(system_id);
        if (system == null) {
            throw new Exception("система не найдена");
        } else {
            Principal principal = request.getUserPrincipal();
            String name = principal.getName();
            User user = this.userService.getUsersByEmail(name);

            run.setUser(user);
            run.setSystem(system);

            run.setVisible(false);
            run.setStatus("Создан");
            this.runService.save(run);

            return run;
        }
    }

    @RequestMapping(value = "/api/delete/run", method = RequestMethod.DELETE)
    public String deleteRun(HttpServletRequest request, @RequestBody Run run) throws Exception {
        run = this.runService.getRunByIdAndName(run.getId(), run.getName());
        if (run == null) {
            throw new Exception("тест не найден");
        } else {
            this.runService.delete(run.getId());
            return okResult();
        }

    }

    @RequestMapping(value = "/api/get/runs", method = RequestMethod.GET)
    public List<Run> getRuns(HttpServletRequest request, @RequestParam Long system_id) {
        List<Run> runs = this.runService.findAllRunsBySystemId(system_id);
        return runs;
    }

    @RequestMapping(value = "/api/get/runs/by_name", method = RequestMethod.GET)
    public List<Run> getRunsByName(HttpServletRequest request, @RequestParam Long system_id, @RequestParam String name) {
        List<Run> runs = this.runService.findRunsByNameIsLike(system_id, name);
        return runs;
    }

    @RequestMapping(value = "/api/get/runs/by_time", method = RequestMethod.GET)
    public List<Run> getRunsByTime(HttpServletRequest request, @RequestParam Long system_id, @RequestParam("time_start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time_start, @RequestParam("time_finish") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time_finish) {
        List<Run> runs = this.runService.getRunsByTime(time_start, time_finish);
        return runs;
    }

    @SneakyThrows
    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping(value = "/api/get/runs/json/calendar", method = RequestMethod.GET)
    public String getRunsByTime(HttpServletRequest request, @RequestParam Long system_id, /*@RequestParam("time_start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime*/ @RequestParam("start") String start, /*@RequestParam("time_finish") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime*/ @RequestParam("end") String end) {
        //форматируем дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time_start = LocalDateTime.parse(start + " 00:00", formatter);
        LocalDateTime time_finish = LocalDateTime.parse(end + " 00:00", formatter);
        //поиск видимых
        List<Run> runs = this.runService.getRunsByTimeAndSystemAndVisibleIsTrue(time_start, time_finish, system_id);

        JSONArray ja = new JSONArray();
        for (Run run : runs) {
            JSONObject jo = new JSONObject();
            jo.put("title", run.getName() + " - " + run.getStatus());
            jo.put("url", "/run_view?id=" + run.getId());

            if (run.getStatus().contains("Неуспе") || run.getStatus().contains("неуспе") || run.getStatus().contains("дефект") || run.getStatus().contains("Дефект")) {
                jo.put("color", "#dc3545");
            } else if (run.getStatus().contains("Успе") || run.getStatus().contains("успе")) {
                jo.put("color", "#28a745");
            } else {
                jo.put("color", "#ffc107");
            }

            jo.put("start", run.getTime_start().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            jo.put("end", run.getTime_finish().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            ja.put(jo);
        }
        return ja.toString().replaceAll("\\\\", "");
    }

    @SneakyThrows
    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping(value = "/api/get/runs/json/list", method = RequestMethod.GET)
    public String getRunsByTimeList(HttpServletRequest request, @RequestParam Long system_id, @RequestParam Long draw, @RequestParam("time_start") String time_start, @RequestParam("time_end") String time_finish, @RequestParam(value = "search[value]") String search, @RequestParam(value = "order[0][column]") int column, @RequestParam(value = "order[0][dir]") String order, @RequestParam("length") int length, @RequestParam("start") int start) {
        //форматируем дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ltime_start = LocalDateTime.parse(time_start + " 00:00", formatter);
        LocalDateTime ltime_finish = LocalDateTime.parse(time_finish + " 00:00", formatter);



        long filtered = 0;
        int page = start / length;
        String columnString = new String[]{"id", "name", "visible", "status", "time_start", "time_finish"}[column];

        long findBySystem = this.runService.countRunsBySystem(system_id);

        List<Run> runs;
        if (length == -1) {
            length = Integer.MAX_VALUE;
            page = 0;
        }

        if (search.isEmpty()) {
            //считаем сколько всего
            filtered = this.runService.countRunsBySystemAndTime(ltime_start, ltime_finish, system_id);
            //получаем первую страницу
            runs = this.runService.findRunsBySystemAndTimeAndOrder(ltime_start, ltime_finish, system_id, columnString, order, page, length);
        } else {
            //считаем сколько всего с учетом like
            filtered = this.runService.countRunsBySystemAndTimeAndLike(ltime_start, ltime_finish, system_id, search);
            //получаем первую страницу с учетом like
            runs = this.runService.findRunsBySystemAndTimeAndOrderAndLike(ltime_start, ltime_finish, system_id, columnString, order, page, length, search);
        }

        JSONArray ja = new JSONArray();
        for (Run run : runs) {
            JSONObject jo = new JSONObject();
            jo.put("id", run.getId());
            jo.put("title", run.getName());
            jo.put("visible", run.isVisible());
            jo.put("status", run.getStatus());
            jo.put("start", run.getTime_start().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
            jo.put("end", run.getTime_finish().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
            jo.put("run_url", "/run_view?id=" + run.getId());
            jo.put("report_url", ((run.getReport() == null ? "" : (("/report_view?id=") + run.getReport().getId()))));
            ja.put(jo);
        }

        JSONObject jodata = new JSONObject();
        jodata.put("data", ja);
        jodata.put("draw", draw);
        jodata.put("recordsTotal", findBySystem);
        jodata.put("recordsFiltered", filtered);
        return jodata.toString().replaceAll("\\\\", "");

    }

    @SneakyThrows
    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping(value = "/api/get/runs/json/list/report", method = RequestMethod.GET)
    public String getRunsByTimeForReport(HttpServletRequest request, @RequestParam Long report_id, @RequestParam Long system_id, @RequestParam Long draw, @RequestParam("time_start") String time_start, @RequestParam("time_end") String time_finish, @RequestParam(value = "search[value]") String search, @RequestParam(value = "order[0][column]") int column, @RequestParam(value = "order[0][dir]") String order, @RequestParam("length") int length, @RequestParam("start") int start) {
        //форматируем дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ltime_start = LocalDateTime.parse(time_start + " 00:00", formatter);
        LocalDateTime ltime_finish = LocalDateTime.parse(time_finish + " 00:00", formatter);

        long filtered = 0;
        int page = start / length;
        String columnString = new String[]{"id", "title", "visible", "status", "start", "end"}[column];

        long findBySystem = this.runService.countRunsBySystemAndReportNull(system_id);

        List<Run> runs;
        if (length == -1) {
            length = Integer.MAX_VALUE;
            page = 0;
        }

        if (search.isEmpty()) {
            //считаем сколько всего
            filtered = this.runService.countRunsBySystemAndTimeAndReportNull(ltime_start, ltime_finish, system_id);
            //получаем первую страницу
            runs = this.runService.findRunsBySystemAndTimeAndReportNullAndOrder(ltime_start, ltime_finish, system_id, columnString, order, page, length);
        } else {
            //считаем сколько всего с учетом like
            filtered = this.runService.countRunsBySystemAndTimeAndReportNullAndLike(ltime_start, ltime_finish, system_id, search);
            //получаем первую страницу с учетом like
            runs = this.runService.findRunsBySystemAndTimeAndReportNullAndOrderAndLike(ltime_start, ltime_finish, system_id, columnString, order, page, length, search);

        }


        JSONArray ja = new JSONArray();
        for (Run run : runs) {
            JSONObject jo = new JSONObject();
            jo.put("id", run.getId());
            jo.put("title", run.getName());
            jo.put("visible", run.isVisible());
            jo.put("status", run.getStatus());
            jo.put("start", run.getTime_start().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
            jo.put("end", run.getTime_finish().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
            jo.put("run_url", "/run_view?id=" + run.getId());
            jo.put("report_add_run", ("/report/run/add?id=" + report_id + "&run_id=" + run.getId()));
            ja.put(jo);
        }

        JSONObject jodata = new JSONObject();
        jodata.put("data", ja);
        jodata.put("draw", draw);
        jodata.put("recordsTotal", findBySystem);
        jodata.put("recordsFiltered", filtered);
        return jodata.toString().replaceAll("\\\\", "");
    }


    @RequestMapping(value = "/api/get/runs/all", method = RequestMethod.GET)
    public List<Run> getAllRuns(HttpServletRequest request) {
        List<Run> runs = this.runService.findAll();
        return runs;
    }

    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }


}
