package name.xio.xiorkflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.xio.xiorkflow.domain.Process;
import name.xio.xiorkflow.domain.ProcessResult;
import name.xio.xiorkflow.domain.logic.ProcessService;
import name.xio.xml.SimpleXMLWorkShop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DeleteProcess implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");

        log.info("delete process:" + name);

        Process process = new Process();
        process.setName(name);
        ProcessResult processResult = this.getProcessService()
                .deleteProcess(process);

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

    private Log log = LogFactory.getLog(DeleteProcess.class);

    private ProcessService processService;
}
