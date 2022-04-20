package app.persistence.repository.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import app.persistence.entity.sys.System;

import java.util.List;

/**
 * @author Sukhorukov Sergei
 */
@Repository
public interface SystemRepository extends JpaRepository<System, Long> {

    public System getSystemById(Long id);

    public System getSystemByIdAndName(Long id,String name);

    @Query(value = "SELECT * FROM System ORDER BY System.name", nativeQuery = true)
    public List<System> getAllSystemsOrderByName();

}
