package app.controller.back.api;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import app.service.auth.api.UserService;
import app.service.sys.api.*;
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
import java.util.List;

@RestController
@PreAuthorize("hasRole('DEVELOPER')")
public class SystemApiController {

    private final SystemService systemService;

    @Autowired
    public SystemApiController(SystemService systemService) {
        this.systemService = systemService;
    }

    @RequestMapping(value = "/api/get/systems/all", method = RequestMethod.GET)
    public List<System> getSystemsByName(HttpServletRequest request) {
        return this.systemService.getAll();
    }


    @RequestMapping(value = "/api/delete/system", method = RequestMethod.DELETE)
    public String deleteSystem(HttpServletRequest request, @RequestBody System system) throws Exception {
        system = this.systemService.getSystemByIdAndName(system.getId(), system.getName());
        if (system == null) {
            throw new Exception("система не найдена");
        } else {
            this.systemService.delete(system);
            return okResult();
        }
    }

    @RequestMapping(value = "/api/add/system", method = RequestMethod.POST)
    public System addSystem(HttpServletRequest request, @RequestBody System system) {
        this.systemService.save(system);
        return system;
    }

    private String okResult() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("result", "ok");
        return jo.toString();
    }
}
