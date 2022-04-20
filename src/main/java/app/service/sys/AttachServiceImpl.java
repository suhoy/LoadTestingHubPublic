package app.service.sys;

import app.persistence.entity.sys.Report;
import app.service.sys.api.AttachService;
import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Run;
import app.persistence.repository.sys.AttachRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author Sukhorukov Sergei
 */
@Service
public class AttachServiceImpl implements AttachService {

    private AttachRepository attachRepository;

    @Autowired
    public AttachServiceImpl(AttachRepository attachRepository) {
        this.attachRepository = attachRepository;
    }

    @Override
    public List<Attach> findAttachesByRunId(Long id) {
        return this.attachRepository.getAttachesByRunId(id);
    }

    @Override
    public Attach findAttachByAttachId(Long id) {
        return this.attachRepository.getAttachById(id);
    }

    @Override
    public void save(Attach attach) {
        this.attachRepository.save(attach);
    }

    @SneakyThrows
    @Override
    public Attach store(MultipartFile file, Run run, String tag) {
        Attach a =new Attach();
        a.setName(file.getOriginalFilename());
        a.setTag(tag);
        a.setType(file.getContentType());
        a.setData(file.getBytes());
        a.setRun(run);
        this.attachRepository.save(a);
        return a;
    }

    @SneakyThrows
    @Override
    public Attach store(MultipartFile file, Report report, String tag) {
        Attach a =new Attach();
        a.setName(file.getOriginalFilename());
        a.setTag(tag);
        a.setType(file.getContentType());
        a.setData(file.getBytes());
        a.setReport(report);
        this.attachRepository.save(a);
        return a;
    }

    @Override
    public void delete(Attach attach) {
        this.attachRepository.delete(attach);
    }

}
