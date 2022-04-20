package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Info;
import app.persistence.entity.sys.Report;
import app.persistence.entity.sys.Run;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@FrontController
@PreAuthorize("hasRole('USER')")
public class InfoController {

    private final RunService runService;
    private final InfoService infoService;
    private final ReportService reportService;


    @Autowired
    public InfoController(RunService runService, InfoService infoService, ReportService reportService) {
        this.runService = runService;
        this.infoService = infoService;
        this.reportService = reportService;
    }



    @RequestMapping("/info/save")
    public String save(Model model, RedirectAttributes redirectAttributes, @RequestParam String type, @RequestParam Long id, @RequestParam String infoInput, @RequestParam Long infoId, @RequestParam String tag) {
        Info info = infoService.getInfoByInfoId(infoId);

        info.setData(infoInput);
        info.setTag(tag);
        this.infoService.save(info);
        String infoinfo = info.getTag();
        redirectAttributes.addFlashAttribute("msg", "Информация ["+infoinfo+"] успешно сохранена.");
        return "redirect:/" + type + "?id=" + id;
    }

    @RequestMapping("/info/add")
    public String add(Model model,RedirectAttributes redirectAttributes, @RequestParam String type, @RequestParam Long id, @RequestParam String infoInput, @RequestParam String tag) {
        Info info = new Info();
        String infoinfo = "";
        info.setData(infoInput);
        info.setTag(tag);
        if (type.equalsIgnoreCase("run_edit")) {
            Run run = this.runService.findRun(id);
            info.setRun(run);
            infoinfo = info.getTag();
        } else if (type.equalsIgnoreCase("report_edit")) {
            Report report = this.reportService.getReportByReportId(id);
            info.setReport(report);
            infoinfo = info.getTag();
        }
        this.infoService.save(info);
        redirectAttributes.addFlashAttribute("msg", "Информация ["+infoinfo+"] успешно добавлена.");
        return "redirect:/" + type + "?id=" + id;
    }

    @RequestMapping("/info/delete")
    public String delete(Model model,RedirectAttributes redirectAttributes, @RequestParam String type, @RequestParam Long id, @RequestParam Long infoId) {
        Info info = infoService.getInfoByInfoId(infoId);
        String infoinfo = info.getTag();
        this.infoService.delete(info);
        redirectAttributes.addFlashAttribute("msg", "Информация ["+infoinfo+"] успешно удалена.");
        return "redirect:/" + type + "?id=" + id;
    }

}
