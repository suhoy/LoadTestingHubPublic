package app.controller.back;

import app.persistence.entity.auth.User;
import app.persistence.entity.sys.Info;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;
import app.persistence.entity.sys.System;
import app.service.auth.api.UserService;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


//ПРИМЕР КОНТРОЛЛЕРА - ПОТОМ УДАЛИТЬ

@RestController
public class ApiController {

    private final RunService runService;
    private final StatService statService;
    private final GraphService graphService;
    private final InfoService infoService;
    private final AttachService attachService;
    private final SystemService systemService;
    private final UserService userService;


    @Autowired
    public ApiController(RunService runService, StatService statService, GraphService graphService, InfoService infoService, AttachService attachService, SystemService systemService, UserService userService) {
        this.runService = runService;
        this.statService = statService;
        this.graphService = graphService;
        this.infoService = infoService;
        this.attachService = attachService;
        this.systemService = systemService;
        this.userService = userService;
    }

}
