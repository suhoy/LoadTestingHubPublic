package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Attach;
import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.service.sys.api.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;


@FrontController
@PreAuthorize("hasRole('USER')")
public class AttachController {

    private final RunService runService;
    private final AttachService attachService;
    private final ReportService reportService;

    @Autowired
    public AttachController(RunService runService, AttachService attachService, ReportService reportService) {
        this.runService = runService;
        this.attachService = attachService;
        this.reportService = reportService;
    }


    @RequestMapping("/attach/delete")
    public String delete(@RequestParam Long attach_id, RedirectAttributes redirectAttributes, @RequestParam String type, @RequestParam("id") Long id) {
        Attach attach = this.attachService.findAttachByAttachId(attach_id);
        String attachinfo = attach.getTag() +" - " + attach.getName();
        this.attachService.delete(attach);
        redirectAttributes.addFlashAttribute("msg", "Вложение [" +attachinfo +"] успешно удалено.");
        return "redirect:/"+type+"?id="+id;
    }

    @RequestMapping("/attach/upload")
    public String upload(@RequestParam("data") MultipartFile data, RedirectAttributes redirectAttributes,@RequestParam String type, @RequestParam String tag, @RequestParam Long id) {
        String attachinfo="";
        if (type.equalsIgnoreCase("run_edit")) {
            Run run = this.runService.findRun(id);
            Attach attach = this.attachService.store(data, run, tag);
            attachinfo = attach.getTag() +" - " + attach.getName();
        } else if (type.equalsIgnoreCase("report_edit")) {
            Report report = this.reportService.getReportByReportId(id);
            Attach attach = this.attachService.store(data, report, tag);
            attachinfo = attach.getTag() +" - " + attach.getName();
        }
        redirectAttributes.addFlashAttribute("msg", "Вложение [" +attachinfo +"] успешно добавлено.");
        return "redirect:/"+type+"?id="+id;
    }

    @RequestMapping("/attach/update")
    public String update(RedirectAttributes redirectAttributes,@RequestParam String type, @RequestParam String tag, @RequestParam String name, @RequestParam("attach_id") Long attach_id, @RequestParam Long id) {

        Attach attach = attachService.findAttachByAttachId(attach_id);
        attach.setTag(tag);
        attach.setName(name);
        attachService.save(attach);

        String attachinfo = attach.getTag() +" - " + attach.getName();
        redirectAttributes.addFlashAttribute("msg", "Вложение [" +attachinfo +"] успешно обновленно.");
        return "redirect:/"+type+"?id="+id;
    }


    @SneakyThrows
    @PreAuthorize("hasRole('VIEWER')")
    @RequestMapping("/attach/download")
    public ResponseEntity<ByteArrayResource> download(Model model, @RequestParam Long id) {
        Attach attach = this.attachService.findAttachByAttachId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attach.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(attach.getName(), java.nio.charset.StandardCharsets.UTF_8.toString()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attach.getName() + "\"")
                .body(new ByteArrayResource(attach.getData()));
    }


}
