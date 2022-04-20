package app.controller.back;

import app.config.anotation.FrontController;
import app.persistence.entity.sys.*;
import app.service.sys.api.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;


import com.itextpdf.styledxmlparser.css.parse.syntax.CssParserStateController;

import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@FrontController
@PreAuthorize("hasRole('VIEWER')")
public class PdfController {

    private final RunService runService;
    private final ReportService reportService;
    private String baseUrl;


    @Autowired
    ServletContext servletContext;
    private final TemplateEngine templateEngine;

    @Autowired
    public PdfController(RunService runService, ReportService reportService, TemplateEngine templateEngine) {
        this.runService = runService;
        this.reportService = reportService;
        this.templateEngine = templateEngine;

    }

    @SneakyThrows
    @RequestMapping("/pdf_run")
    public ResponseEntity<?> pdf_run(Model model, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        Run run = runService.findRun(id);

        /* Create HTML using Thymeleaf template Engine
         * https://springhow.com/spring-boot-pdf-generation/
         * */

        this.baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("run", run);

        context.setVariable("baseUrl", this.baseUrl);
        //model.addAttribute("countermodel", 1);
        context.setVariable("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        context.setVariable("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));

        String html = templateEngine.process("front/simple_run", context);


        //----------------toc
        //String filename1 = (run.getSystem().getName() + "_" + run.getName() + "_" + run.getTime_start()).replaceAll(" ", "_");
        //String pdfname=(URLEncoder.encode(filename1, java.nio.charset.StandardCharsets.UTF_8.toString()) + ".pdf");


        Document htmlDoc = Jsoup.parse(html);

        // This is our Table of Contents aggregating element
        Element tocElement = htmlDoc.body().getElementById("TOC_SIMPLE_RUN");
        //tocElement.append("<b>Table of contents</b>");

        // We are going to build a complex CSS
        StringBuilder tocStyles = new StringBuilder().append("<style>");

        Elements tocElements = htmlDoc.select("[data-toc]");
        for (Element elem : tocElements) {
            // Here we create an anchor to be able to refer to this element when generating page numbers and links
            String idtoc = UUID.randomUUID().toString();
            elem.attr("id", idtoc);

            // CSS selector to show page numebr for a TOC entry
            tocStyles.append("*[data-toc-id=\"" + idtoc + "\"] .toc-page-ref::after { content: target-counter(#" + idtoc + ", page) }");

            // Generating TOC entry as a small table to align page numbers on the right
            Element tocEntry = tocElement.appendElement("table");
            tocEntry.attr("style", "width: 100%");
            Element tocEntryRow = tocEntry.appendElement("tr");
            tocEntryRow.attr("data-toc-id", idtoc);
            Element tocEntryTitle = tocEntryRow.appendElement("td");
            tocEntryTitle.appendText(elem.attr("data-toc"));
            Element tocEntryPageRef = tocEntryRow.appendElement("td");
            tocEntryPageRef.attr("style", "text-align: right");
            // <span> is a placeholder element where target page number will be inserted
            // It is wrapped by an <a> tag to create links pointing to the element in our document
            tocEntryPageRef.append("<a href=\"#" + idtoc + "\"><span class=\"toc-page-ref\"></span></a>");
        }


        tocStyles.append("</style>");

        htmlDoc.head().append(tocStyles.toString());

        html = htmlDoc.html();

        //===============toc
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
/*
        this.baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();*/
        //converterProperties.setBaseUri("http://localhost:8080");
        //converterProperties.setBaseUri(baseUrl);

        converterProperties.setCreateAcroForm(false);
        /* Call convert method */
        HtmlConverter.convertToPdf(html, target, converterProperties);


        //embedded files
        ByteArrayInputStream target_reader = new ByteArrayInputStream(target.toByteArray());
        ByteArrayOutputStream target_writer = new ByteArrayOutputStream();

        PdfReader pdfr = new PdfReader(target_reader);
        PdfWriter pdfw = new PdfWriter(target_writer);
        PdfDocument pdfDoc = new PdfDocument(pdfr, pdfw);

        //String FONT_FILENAME = "./src/main/resources/META-INF/resources/libs/font/arial.ttf";
        //PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.UTF8);
        //pdfDoc.addFont(font);

        for (Attach attach : run.getAttaches()) {
            String embeddedFileName = attach.getName();
            String embeddedFileDescription = convertCyrilic(attach.getTag()); //
            byte[] embeddedFileContentBytes = attach.getData();

            // the 7th argument is the AFRelationship key value.
            PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, embeddedFileContentBytes,
                    embeddedFileDescription, embeddedFileName, null, null, null);

            // This method adds file attachment at document level.
            pdfDoc.addFileAttachment("embedded_file_" + attach.getId(), spec);
        }

        pdfDoc.close();
        //embedded files



        /* extract output as bytes */
        //byte[] bytes = target.toByteArray();
        //embedded files
        byte[] bytes = target_writer.toByteArray();


        String filename2 = ("[Протокол НТ " + run.getSystem().getName() + "] " + run.getName() + " (" + run.getTime_start().toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")").replaceAll(" ", "-");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(filename2, java.nio.charset.StandardCharsets.UTF_8.toString()) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    @SneakyThrows
    @RequestMapping("/pdf_report")
    public ResponseEntity<?> pdf_report(Model model, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        Report report = this.reportService.getReportByReportId(id);

        this.baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        WebContext context = new WebContext(request, response, servletContext);


        context.setVariable("report", report);
        context.setVariable("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        context.setVariable("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        context.setVariable("baseUrl", this.baseUrl);

        String html = templateEngine.process("front/simple_report", context);

        Document htmlDoc = Jsoup.parse(html);

        //оглавление отчёта VVVVVVVV
        // This is our Table of Contents aggregating element
        Element tocElement = htmlDoc.body().getElementById("TOC_SIMPLE_REPORT");
        //tocElement.append("<b>Table of contents</b>");

        // We are going to build a complex CSS
        StringBuilder tocStyles = new StringBuilder().append("<style>");

        Elements tocElements = htmlDoc.select("[data-toc-report]");
        for (Element elem : tocElements) {
            // Here we create an anchor to be able to refer to this element when generating page numbers and links
            String idtoc = UUID.randomUUID().toString();
            elem.attr("id", idtoc);

            // CSS selector to show page numebr for a TOC entry
            tocStyles.append("*[data-toc-report-id=\"" + idtoc + "\"] .toc-page-ref::after { content: target-counter(#" + idtoc + ", page) }");

            // Generating TOC entry as a small table to align page numbers on the right
            Element tocEntry = tocElement.appendElement("table");
            tocEntry.attr("style", "width: 100%");
            Element tocEntryRow = tocEntry.appendElement("tr");
            tocEntryRow.attr("data-toc-report-id", idtoc);
            Element tocEntryTitle = tocEntryRow.appendElement("td");
            tocEntryTitle.appendText(elem.attr("data-toc-report"));
            Element tocEntryPageRef = tocEntryRow.appendElement("td");
            tocEntryPageRef.attr("style", "text-align: right");
            // <span> is a placeholder element where target page number will be inserted
            // It is wrapped by an <a> tag to create links pointing to the element in our document
            tocEntryPageRef.append("<a href=\"#" + idtoc + "\"><span class=\"toc-page-ref\"></span></a>");
        }
        tocStyles.append("</style>");
        htmlDoc.head().append(tocStyles.toString());
        //оглавление отчёта ^^^^^^^^^


        //список тестов VVVVV
        tocElement = htmlDoc.body().getElementById("TOC_RUN_LIST");
        tocStyles = new StringBuilder().append("<style>");
        tocElements = htmlDoc.select("[data-toc-run-list]");

        for (Element elem : tocElements) {
            // Here we create an anchor to be able to refer to this element when generating page numbers and links
            String idtoc = UUID.randomUUID().toString();
            elem.attr("id", idtoc);

            // CSS selector to show page numebr for a TOC entry
            tocStyles.append("*[data-toc-run-list-id=\"" + idtoc + "\"] .toc-page-ref::after { content: target-counter(#" + idtoc + ", page) }");

            // Generating TOC entry as a small table to align page numbers on the right
            Element tocEntry = tocElement.appendElement("table");
            tocEntry.attr("style", "width: 100%");
            Element tocEntryRow = tocEntry.appendElement("tr");
            tocEntryRow.attr("data-toc-run-list-id", idtoc);
            Element tocEntryTitle = tocEntryRow.appendElement("td");
            tocEntryTitle.appendText(elem.attr("data-toc-run-list"));
            Element tocEntryPageRef = tocEntryRow.appendElement("td");
            tocEntryPageRef.attr("style", "text-align: right");
            // <span> is a placeholder element where target page number will be inserted
            // It is wrapped by an <a> tag to create links pointing to the element in our document
            tocEntryPageRef.append("<a href=\"#" + idtoc + "\"><span class=\"toc-page-ref\"></span></a>");
        }
        tocStyles.append("</style>");
        htmlDoc.head().append(tocStyles.toString());
        //список запусков ^^^^^^^^^


        //оглавление запусков VVVVV
        /*run toc*/
        for (int i = 0; i < report.getRuns().size(); i++) {
            tocElement = htmlDoc.body().getElementById("TOC_SIMPLE_RUN_" + i);
            tocStyles = new StringBuilder().append("<style>");
            tocElements = htmlDoc.getElementsByClass("RUN_" + i).select("[data-toc-run]");

            for (Element elem : tocElements) {
                // Here we create an anchor to be able to refer to this element when generating page numbers and links
                String idtoc = UUID.randomUUID().toString();
                elem.attr("id", idtoc);

                // CSS selector to show page numebr for a TOC entry
                tocStyles.append("*[data-toc-run-id=\"" + idtoc + "\"] .toc-page-ref::after { content: target-counter(#" + idtoc + ", page) }");

                // Generating TOC entry as a small table to align page numbers on the right
                Element tocEntry = tocElement.appendElement("table");
                tocEntry.attr("style", "width: 100%");
                Element tocEntryRow = tocEntry.appendElement("tr");
                tocEntryRow.attr("data-toc-run-id", idtoc);
                Element tocEntryTitle = tocEntryRow.appendElement("td");
                tocEntryTitle.appendText(elem.attr("data-toc-run"));
                Element tocEntryPageRef = tocEntryRow.appendElement("td");
                tocEntryPageRef.attr("style", "text-align: right");
                // <span> is a placeholder element where target page number will be inserted
                // It is wrapped by an <a> tag to create links pointing to the element in our document
                tocEntryPageRef.append("<a href=\"#" + idtoc + "\"><span class=\"toc-page-ref\"></span></a>");
            }
            tocStyles.append("</style>");
            htmlDoc.head().append(tocStyles.toString());
        }
        //оглавление запусков ^^^^^^^^^


        html = htmlDoc.html();

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
/*
        this.baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
*/
        //converterProperties.setBaseUri(baseUrl);
        converterProperties.setCreateAcroForm(false);

        /* Call convert method */
        HtmlConverter.convertToPdf(html, target, converterProperties);


        //embedded files
        ByteArrayInputStream target_reader = new ByteArrayInputStream(target.toByteArray());
        ByteArrayOutputStream target_writer = new ByteArrayOutputStream();

        PdfReader pdfr = new PdfReader(target_reader);
        PdfWriter pdfw = new PdfWriter(target_writer);
        PdfDocument pdfDoc = new PdfDocument(pdfr, pdfw);

        //String FONT_FILENAME = "./src/main/resources/META-INF/resources/libs/font/arial.ttf";
        //PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.UTF8);
        //pdfDoc.addFont(font);

        for (Attach attach : report.getAttaches()) {
            String embeddedFileName = attach.getName();
            String embeddedFileDescription = convertCyrilic(attach.getTag()); //
            byte[] embeddedFileContentBytes = attach.getData();

            // the 7th argument is the AFRelationship key value.
            PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, embeddedFileContentBytes,
                    embeddedFileDescription, embeddedFileName, null, null, null);

            // This method adds file attachment at document level.
            pdfDoc.addFileAttachment("embedded_file_" + attach.getId(), spec);
        }


        for(Run run:report.getRuns()) {
            for (Attach attach : run.getAttaches()) {
                String embeddedFileName = attach.getName();
                String embeddedFileDescription = convertCyrilic(attach.getTag()); //
                byte[] embeddedFileContentBytes = attach.getData();

                // the 7th argument is the AFRelationship key value.
                PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, embeddedFileContentBytes,
                        embeddedFileDescription, embeddedFileName, null, null, null);

                // This method adds file attachment at document level.
                pdfDoc.addFileAttachment("embedded_file_" + attach.getId(), spec);
            }
        }
        pdfDoc.close();
        //embedded files





        /* extract output as bytes */
        //byte[] bytes = target.toByteArray();
        //embedded files
        byte[] bytes = target_writer.toByteArray();
        String filename2 = ("[Отчёт НТ " + report.getSystem().getName() + "] " + report.getName() + " (" + report.getDate_created() + ")").replaceAll(" ", "-");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(filename2, java.nio.charset.StandardCharsets.UTF_8.toString()) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    @SneakyThrows
    @RequestMapping("/pdf_system")
    public ResponseEntity<?> pdf_system(Model model, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        //List<Report> reports = this.reportService.findVisibleReportsBySystemId(id);
        List<Report> reports = this.reportService.findReportsBySystemIdAndVisibleIsTrueOrderByDate_createdAsc(id);
        String today = LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        int countruns = 0;
        for (Report report : reports) {
            for (Run run : report.getRuns()) {
                countruns++;
            }

        }

        WebContext context = new WebContext(request, response, servletContext);

        this.baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        context.setVariable("reports", reports);

        context.setVariable("df", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        context.setVariable("dft", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        context.setVariable("countruns", countruns);
        context.setVariable("today", today);
        context.setVariable("baseUrl", this.baseUrl);

        String html = templateEngine.process("front/simple_system", context);


        //----------------toc
        // TOC_SIMPLE_SYSTEM

        Document htmlDoc = Jsoup.parse(html);

        // This is our Table of Contents aggregating element
        Element tocElement = htmlDoc.body().getElementById("TOC_SIMPLE_SYSTEM");
        //tocElement.append("<b>Table of contents</b>");

        // We are going to build a complex CSS
        StringBuilder tocStyles = new StringBuilder().append("<style>");

        Elements tocElements = htmlDoc.select("[data-toc]");
        for (Element elem : tocElements) {
            // Here we create an anchor to be able to refer to this element when generating page numbers and links
            String idtoc = UUID.randomUUID().toString();
            elem.attr("id", idtoc);

            // CSS selector to show page numebr for a TOC entry
            tocStyles.append("*[data-toc-id=\"" + idtoc + "\"] .toc-page-ref::after { content: target-counter(#" + idtoc + ", page) }");

            // Generating TOC entry as a small table to align page numbers on the right
            Element tocEntry = tocElement.appendElement("table");
            tocEntry.attr("style", "width: 100%");
            Element tocEntryRow = tocEntry.appendElement("tr");
            tocEntryRow.attr("data-toc-id", idtoc);
            Element tocEntryTitle = tocEntryRow.appendElement("td");
            tocEntryTitle.appendText(elem.attr("data-toc"));
            Element tocEntryPageRef = tocEntryRow.appendElement("td");
            tocEntryPageRef.attr("style", "text-align: right");
            // <span> is a placeholder element where target page number will be inserted
            // It is wrapped by an <a> tag to create links pointing to the element in our document
            tocEntryPageRef.append("<a href=\"#" + idtoc + "\"><span class=\"toc-page-ref\"></span></a>");
        }


        tocStyles.append("</style>");

        htmlDoc.head().append(tocStyles.toString());

        html = htmlDoc.html();

        //===============toc


        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();

        //converterProperties.setBaseUri("http://localhost:8080");

        //converterProperties.setBaseUri(baseUrl);
        converterProperties.setCreateAcroForm(false);
        /* Call convert method */
        HtmlConverter.convertToPdf(html, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        String sysname = "";

        if (reports.size() > 0) {
            sysname = reports.get(0).getSystem().getName();
            //sysname="Smbio";
        }

        String filename2 = ("[НТ " + sysname + "] Выгрузка на " + today).replaceAll(" ", "_");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(filename2, java.nio.charset.StandardCharsets.UTF_8.toString()) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    public static String convertCyrilic(String message){
        char[] abcCyr =   {' ','а','б','в','г','д','ѓ','е', 'ж','з','ѕ','и','ј','к','л','љ','м','н','њ','о','п','р','с','т', 'ќ','у', 'ф','х','ц','ч','џ','ш', 'А','Б','В','Г','Д','Ѓ','Е', 'Ж','З','Ѕ','И','Ј','К','Л','Љ','М','Н','Њ','О','П','Р','С','Т', 'Ќ', 'У','Ф', 'Х','Ц','Ч','Џ','Ш','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','/','-'};
        String[] abcLat = {" ","a","b","v","g","d","]","e","zh","z","y","i","j","k","l","q","m","n","w","o","p","r","s","t","'","u","f","h", "c",";", "x","{","A","B","V","G","D","}","E","Zh","Z","Y","I","J","K","L","Q","M","N","W","O","P","R","S","T","KJ","U","F","H", "C",":", "X","{", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","/","-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }


}
