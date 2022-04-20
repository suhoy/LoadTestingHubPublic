package app.persistence.repository.auth;

import app.persistence.entity.auth.Role;
import app.persistence.entity.auth.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //@Query(value = "SELECT DISTINCT Graph.tag FROM Role WHERE Graph.run_id = ?1 ORDER BY Graph.tag", nativeQuery = true)
    Set<Role> getRolesByRoleIsIn(Set<RoleType> roles);
    Role getRoleByRole(RoleType roleType);
    Role findRoleByRole(RoleType roleType);
}
