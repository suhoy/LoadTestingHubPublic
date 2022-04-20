package app.service.sys;

import app.service.sys.api.SystemService;
import app.persistence.repository.sys.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.persistence.entity.sys.System;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * @author Sukhorukov Sergei
 */
@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    private SystemRepository systemRepository;

    @Autowired
    public SystemServiceImpl(SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    @Override

    public System findSystem(Long id) {
        return systemRepository.getSystemById(id);
    }

    @Override
    public List<System> getAll() {
        return systemRepository.getAllSystemsOrderByName();
    }

    @Override
    public void save(System system) {
        this.systemRepository.save(system);
    }

    @Override
    public System getSystemByIdAndName(Long id, String name) {
        return this.systemRepository.getSystemByIdAndName(id,name);
    }

    @Override
    public void delete(System system) {
        this.systemRepository.delete(system);
    }

}
