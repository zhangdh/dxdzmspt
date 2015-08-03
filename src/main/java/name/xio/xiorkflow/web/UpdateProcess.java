package name.xio.xiorkflow.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.xio.xiorkflow.domain.Process;
import name.xio.xiorkflow.domain.ProcessResult;
import name.xio.xiorkflow.domain.logic.ProcessService;
import name.xio.xml.SimpleXMLWorkShop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class UpdateProcess implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String xml = request.getParameter("xml");

        log.info("update process:" + name);

        ProcessResult processResult = new ProcessResult();
        Document doc;
        try {
            doc = SimpleXMLWorkShop.str2Doc(xml);
        } catch (IOException e) {
            processResult.setStatus(ProcessService.IO_ERROR);
            processResult.setMes(e.getMessage());
            doc = null;
            log.warn("io error on onupdateprocess str2Doc:" + e.getMessage());
            log.warn(xml);
        } catch (JDOMException e) {
            processResult.setStatus(ProcessService.XML_PARSER_ERROR);
            processResult.setMes(e.getMessage());
            doc = null;
            log.warn("jdom error onupdateprocess str2Doc:" + e.getMessage());
            log.warn(xml);
        }

        if (doc != null) {
            Process process = new Process();
            process.setName(name);
            process.setDoc(doc);
            processResult = this.getProcessService().updateProcess(process);
        }

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        SimpleXMLWorkShop.outputXML(ProcessResult.convertXml(processResult),
                response.getOutputStream());

        //not redirect to other view,it processed on response
        return null;
    }

    /**
     * @return Returns the processService.
     */
    public ProcessService getProcessService() {
        return processService;
    }

    /**
     * @param processService The processService to set.
     */
    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    private Log log = LogFactory.getLog(UpdateProcess.class);

    private ProcessService processService;
}
