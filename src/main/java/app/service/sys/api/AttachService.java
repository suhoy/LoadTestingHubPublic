package app.service.sys.api;

import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Info;
import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Sergei Sukhorukov
 */
public interface AttachService {

    List<Attach> findAttachesByRunId(Long id);
    Attach findAttachByAttachId(Long id);
    void save (Attach attach);
    void delete (Attach attach);
    Attach store (MultipartFile file, Run run, String tag);
    Attach store (MultipartFile file, Report report, String tag);

}
