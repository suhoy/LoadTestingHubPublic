package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.Graph;
import app.persistence.entity.sys.Run;
import app.service.sys.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@FrontController
@PreAuthorize("hasRole('USER')")
public class GraphController {

    private final RunService runService;
    private final GraphService graphService;


    @Autowired
    public GraphController(RunService runService, GraphService graphService) {
        this.runService = runService;
        this.graphService = graphService;
    }

    @RequestMapping("/graph/delete")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam Long id, @RequestParam("run_id") Long run_id) {
        Run run = runService.findRun(run_id);
        Graph graph = this.graphService.findGraphByGraphId(id);
        String graphinfo = graph.getTag() +" - " + graph.getAbout();
        graphService.delete(graph);
        redirectAttributes.addFlashAttribute("msg", "График ["+graphinfo+"] успешно удалён.");
        return "redirect:/run_edit?id="+run_id;
    }

    @RequestMapping("/graph/update")
    public String update(Model model,RedirectAttributes redirectAttributes, @RequestParam Long id, @RequestParam("run_id") Long run_id, @RequestParam("about") String about, @RequestParam("tag") String tag) {
        Graph graph = this.graphService.findGraphByGraphId(id);
        String graphinfo = graph.getTag() +" - " + graph.getAbout();
        graph.setTag(tag);
        graph.setAbout(about);
        graphService.save(graph);
        redirectAttributes.addFlashAttribute("msg", "График ["+ graphinfo+ "] успешно обновлён.");
        return "redirect:/run_edit?id="+run_id;
    }


    @RequestMapping("/graph/upload")
    public String uploadFile(@RequestParam("data") MultipartFile data,RedirectAttributes redirectAttributes, @RequestParam String tag, @RequestParam String about, @RequestParam Long id) {
        Run run = runService.findRun(id);
        Graph graph = this.graphService.store(data, run, tag, about);
        String graphinfo = graph.getTag() +" - " + graph.getAbout();
        redirectAttributes.addFlashAttribute("msg", "График ["+ graphinfo + "] успешно загружен.");
        return "redirect:/run_edit?id="+id;
    }


}
