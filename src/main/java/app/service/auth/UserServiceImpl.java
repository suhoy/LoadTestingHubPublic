package app.service.auth;

import app.service.auth.api.UserService;
import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import app.persistence.repository.auth.RoleRepository;
import app.persistence.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAllByRoles(EnumSet<RoleType> roles) {

        Set<Role> rls = this.roleRepository.getRolesByRoleIsIn(roles);
        return userRepository.getDistinctByRolesIsIn(rls);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUsersById(id);
    }

    @Override
    public User getUsersByEmail(String email) {
        return userRepository.getUsersByEmail(email);
    }

    @Override
    public User findUsersByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Role getRoleByRole(RoleType roleType) {
        return roleRepository.getRoleByRole(roleType);
    }

    @Override
    public Role findRoleByRole(RoleType roleType) {
        return roleRepository.findRoleByRole(roleType);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }


}
