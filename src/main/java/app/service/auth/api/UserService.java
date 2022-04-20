package app.service.auth.api;

import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;

import java.util.EnumSet;
import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface UserService {

    List<User> getAllByRoles(EnumSet<RoleType> roles);
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUsersByEmail(String email);
    User findUsersByEmail(String email);
    Role getRoleByRole(RoleType roleType);
    Role findRoleByRole(RoleType roleType);

    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void save(User user);
    void save(Role role);


}
