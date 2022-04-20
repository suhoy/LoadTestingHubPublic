package app.persistence.repository.auth;

import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import app.persistence.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    List<User> getDistinctByRolesIsIn(Set<Role> roles);
    User getUsersById(Long id);
    User getUsersByEmail(String email);
}
