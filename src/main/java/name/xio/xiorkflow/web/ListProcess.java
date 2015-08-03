package name.xio.xiorkflow.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.xio.xiorkflow.domain.ProcessResult;
import name.xio.xiorkflow.domain.logic.ProcessService;
import name.xio.xml.SimpleXMLWorkShop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ListProcess implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        log.info("list process.");

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        List list = this.getProcessService().listProcess();

        SimpleXMLWorkShop.outputXML(ProcessResult.convertFilesToXml(list),
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

    private Log log = LogFactory.getLog(ListProcess.class);

    private ProcessService processService;
}
