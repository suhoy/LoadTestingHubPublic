package app.service.sys.api;

import app.persistence.entity.sys.Info;
import app.persistence.entity.sys.Run;
import app.persistence.entity.sys.Stat;

import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface InfoService {

    List<Info> findInfosByRunId(Long id);
    List<Info> findAllInfos();
    Info getInfoByInfoId(Long id);
    void save (Info info);
    void delete (Info info);

}
