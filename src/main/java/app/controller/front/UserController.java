package app.controller.front;

import app.config.anotation.FrontController;
import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.service.auth.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@FrontController
public class UserController {


    //private static final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final UserService userService;

    @Value("${project.regmails}")
    private String[] regmails;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping("/register/user")
    public String register(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, @RequestParam String username, @RequestParam String fname, @RequestParam String lname, @RequestParam String password, @RequestParam String password2) {

        boolean goodmail = false;
        for (String regex : regmails) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(username);

            if (matcher.matches()) {
                goodmail = true;
            }
        }
        if (!goodmail) {
            model.addAttribute("error", "Недопустимый email");
            return "register";
        }

        User check = this.userService.findUsersByEmail(username);
        if (check != null) {
            model.addAttribute("error", "Почта уже зарегистрирована");
            return "register";
        }

        if (!password.equals(password2)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Пароль слишком короткий");
            return "register";
        }
        User user = new User();
        user.setId(null);
        user.setEmail(username);
        user.setFirstName(fname);
        user.setLastName(lname);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        List<Role> roles = this.userService.getAllRoles();
        if (roles.size() < RoleType.values().length) {
            for (RoleType type : RoleType.values()) {
                Role role = this.userService.findRoleByRole(type);
                if (role == null) {
                    role = new Role();
                    role.setId(null);
                    role.setRole(type);
                    this.userService.save(role);
                }
            }
        }
        try {
            //user.setRoles(new HashSet<>());
            user.setRoles(new HashSet<>());
            user.getRoles().add(this.userService.getRoleByRole(RoleType.ROLE_REGISTERED));
            //user.getRoles().add(this.userService.getRoleByRole(RoleType.ROLE_VIEWER));
            //user.getRoles().remove(this.userService.getRoleByRole(RoleType.ROLE_VIEWER));
            user.setActive(true);
            this.userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        try {
            request.login(username, password);
            //redirectAttributes.addFlashAttribute("succ_profile", "Аккаунт успешно создан! Для получения прав на просмотр - напишите администраторам сервиса.");
            //model.addAttribute("new_user", "Аккаунт успешно создан! Для получения прав на просмотр - напишите администраторам сервиса.");
            model.addAttribute("user", user);
            model.addAttribute("succ_profile", "Аккаунт успешно создан! Для получения прав на просмотр - напишите администраторам сервиса.");
            return "front/profile";
        } catch (ServletException e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @RequestMapping("/register")
    public String register(Model model) {
        return "register";
    }
}
