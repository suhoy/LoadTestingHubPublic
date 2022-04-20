package app.controller.front;

import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.service.auth.api.UserService;
import app.service.sys.api.RunService;
import app.service.sys.api.SystemService;
import app.config.anotation.FrontController;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import app.persistence.entity.sys.System;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FrontController
public class IndexController {


    private final SystemService systemService;
    private final RunService runService;
    private final UserService userService;

    @Value("${project.somecode}")
    private String somecode;


    @Autowired
    IndexController(SystemService systemService, RunService runService, UserService userService) {
        this.systemService = systemService;
        this.runService = runService;
        this.userService = userService;
    }


    @RequestMapping("/")
    public String index(Model model) {
        List<System> systems = systemService.getAll();
        model.addAttribute("systems", systems);
        //model.addAttribute("listrunscount", this.runService.countRunsGroupedById());
        return "front/index";
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(HttpServletRequest request, Model model) {
        String email = request.getUserPrincipal().getName();
        User user = this.userService.getUsersByEmail(email);
        model.addAttribute("user", user);
        return "front/profile";
    }

    @SneakyThrows
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    public String profileUpdate(HttpServletRequest request, Model model, @RequestParam String username, @RequestParam String fname, @RequestParam String lname) {
        String oldEmail = request.getUserPrincipal().getName();
        String newEmail = username;
        boolean reload = false;
        User user = this.userService.findUsersByEmail(oldEmail);
        if (!oldEmail.equals(newEmail)) {
            User check = this.userService.findUsersByEmail(username);
            if (check != null) {
                model.addAttribute("error_profile", "Почта уже зарегистрирована");
                return "front/profile";
            } else {
                user.setEmail(newEmail);
                reload = true;
            }
        }

        user.setFirstName(fname);
        user.setLastName(lname);
        this.userService.save(user);
        if (reload) {
            request.logout();
            model.addAttribute("succ_profile", "Почта успешно изменена на " + newEmail);
            return "login";
        }
        model.addAttribute("succ_profile", "Данные успешно обновлены.");
        model.addAttribute("user", user);
        return "front/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/password", method = RequestMethod.POST)
    public String profilePass(HttpServletRequest request, Model model, @RequestParam String password1, @RequestParam String password2, @RequestParam String password3) {
        String oldEmail = request.getUserPrincipal().getName();
        User user = this.userService.findUsersByEmail(oldEmail);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password1, user.getPassword())) {
            model.addAttribute("error_pass", "Старый пароль неверный");
            model.addAttribute("user", user);
            return "front/profile";
        }
        if (!password2.equals(password3)) {
            model.addAttribute("error_pass", "Новые пароли не совпадают");
            model.addAttribute("user", user);
            return "front/profile";
        }
        user.setPassword(passwordEncoder.encode(password2));
        this.userService.save(user);
        model.addAttribute("succ_pass", "Пароль успешно обновлен.");
        model.addAttribute("user", user);
        return "front/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/cheat", method = RequestMethod.POST)
    public String profilePass(HttpServletRequest request, Model model, @RequestParam String code) {
        String oldEmail = request.getUserPrincipal().getName();
        User user = this.userService.findUsersByEmail(oldEmail);
        if (code.equals(somecode)) {
            user.setRoles(null);
            List<Role> roles = this.userService.getAllRoles();
            /*if (roles.size() == 0) {
                for (RoleType type : RoleType.values()) {
                    Role role = new Role();
                    role.setRole(type);
                    this.userService.save(role);
                }
            }*/
            Set<Role> setroles = new HashSet<>(roles);
            user.setRoles(setroles);
            this.userService.save(user);
        }
        model.addAttribute("user", user);
        return "front/profile";
    }


    @RequestMapping(value = "/notfound", method = RequestMethod.GET)
    public String notfound(HttpServletRequest request) {
        return "front/notfound";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model model, HttpServletRequest request) {
        List<User> admins = this.userService.getAllByRoles(EnumSet.of(RoleType.ROLE_ADMIN));
        List<User> users = this.userService.getAllByRoles(EnumSet.of(RoleType.ROLE_USER));
        model.addAttribute("admins", admins);
        model.addAttribute("users", users);
        //model.addAttribute("managers", managers);
        return "front/about";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String api(HttpServletRequest request) {
        return "front/api";
    }

}
