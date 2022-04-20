package app.service.sys;

import app.service.sys.api.InfoService;
import app.persistence.entity.sys.Info;
import app.persistence.repository.sys.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class InfoServiceImpl implements InfoService {

    private InfoRepository infoRepository;

    @Autowired
    public InfoServiceImpl(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    @Override
    public List<Info> findInfosByRunId(Long id) {
        return this.infoRepository.getInfosByRunId(id);
    }

    @Override
    public List<Info> findAllInfos() {
        return this.infoRepository.findAll();
    }

    @Override
    public Info getInfoByInfoId(Long id) {
        return this.infoRepository.getInfoById(id);
    }

    @Override
    public void save(Info info) {
        this.infoRepository.save(info);
    }

    @Override
    public void delete(Info info) {
        this.infoRepository.delete(info);
    }

}
