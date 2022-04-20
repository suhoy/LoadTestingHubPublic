package app.service.sys.api;

import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.System;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface SystemService {

    System findSystem(Long id);
    List<System> getAll();
    void save(System system);
    void delete(System system);
    System getSystemByIdAndName(Long id,String name);
}
