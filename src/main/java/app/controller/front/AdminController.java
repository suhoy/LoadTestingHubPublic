package app.controller.front;

import app.config.anotation.FrontController;
import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.service.auth.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@FrontController
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @Value("${project.passchange}")
    private String passchange;


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping("/admin")
    public String admin(Model model) {
        List<User> users = this.userService.getAllUsers();
        List<Role> roles = this.userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "front/admin";
    }

    @RequestMapping("/admin/add")
    public String add(RedirectAttributes redirectAttributes, @RequestParam Long user_id, @RequestParam Long role_id) {
        User user = this.userService.getUserById(user_id);
        Role role = this.userService.getRoleById(role_id);
        Set<Role> roles = user.getRoles();
        if (!roles.contains(role)) {
            roles.add(role);
            user.setRoles(roles);
            this.userService.save(user);
            String succ = "Пользователь " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " получил роль " + role.getRole().toString();
            redirectAttributes.addFlashAttribute("succ", succ);
            return "redirect:/admin";
        } else {
            String err = "У Пользователя " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " уже есть роль " + role.getRole().toString();
            redirectAttributes.addFlashAttribute("err", err);
            return "redirect:/admin";
        }
    }

    @RequestMapping("/admin/remove")
    public String remove(Model model, RedirectAttributes redirectAttributes, @RequestParam Long user_id, @RequestParam Long role_id) {
        User user = this.userService.getUserById(user_id);
        Role role = this.userService.getRoleById(role_id);
        Set<Role> roles = user.getRoles();
        if (roles.contains(role)) {
            roles.remove(role);
            user.setRoles(roles);
            this.userService.save(user);
            String succ = "У Пользователя " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " отобрали роль " + role.getRole().toString();
            redirectAttributes.addFlashAttribute("succ", succ);
            return "redirect:/admin";
        } else {
            String err = "У Пользователя " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " нет роли " + role.getRole().toString();
            redirectAttributes.addFlashAttribute("err", err);
            return "redirect:/admin";
        }
    }

    @RequestMapping("/admin/reset/password")
    public String reset_password(Model model, RedirectAttributes redirectAttributes, @RequestParam Long user_id) {
        Random rand  = new Random();
        int r = rand.nextInt(100);
        String newpass=passchange+r;

        User user = this.userService.getUserById(user_id);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newpass);
        user.setPassword(encodedPassword);

        this.userService.save(user);
        String succ = "У Пользователя " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " обновили пароль на " + newpass;
        redirectAttributes.addFlashAttribute("succ", succ);
        return "redirect:/admin";

    }

    @RequestMapping("/admin/change/active")
    public String change_active(Model model, RedirectAttributes redirectAttributes, @RequestParam Long user_id) {

        User user = this.userService.getUserById(user_id);
        user.setActive(!user.isActive());

        this.userService.save(user);
        String succ = "У Пользователя " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " изменили active на " + user.isActive();
        redirectAttributes.addFlashAttribute("succ", succ);
        return "redirect:/admin";

    }
}
