package name.xio.xiorkflow.domain;

import java.io.File;
import java.util.List;

import name.xio.util.PathUtil;
import name.xio.xiorkflow.domain.logic.ProcessService;

import org.jdom.Document;
import org.jdom.Element;

import com.coffice.workflow.util. WorkFlowMethod;


public class ProcessResult {

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the mes.
     */
    public String getMes() {
        return mes;
    }

    /**
     * @param mes The mes to set.
     */
    public void setMes(String mes) {
        this.mes = mes;
    }

    /**
     * @return Returns the process.
     */
    public Process getProcess() {
        return process;
    }

    /**
     * @param process The process to set.
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public static Document convertXml(ProcessResult processResponse) {
        Element workflowProcessNode = new Element(
                ProcessResult.WORKFLOW_PROCESS_NODE_NAME);

        Element responseNode = new Element(ProcessResult.RESPONSE_NODE_NAME);
        workflowProcessNode.addContent(responseNode);

        int status = processResponse.getStatus();
        if (status > ProcessService.NULL) {
            responseNode.setAttribute(ProcessResult.STATUS_ATT_NAME, String
                    .valueOf(status));
        }

        String name = processResponse.getName();
        if ((name != null) && (!name.equals(""))) {
            responseNode.setAttribute(ProcessResult.NAME_ATT_NAME, name);
        }

        String mes = processResponse.getMes();
        if ((mes != null) && (!mes.equals(""))) {
            responseNode.setAttribute(ProcessResult.MES_ATT_NAME, mes);
        }

        return new Document(workflowProcessNode);
    }

    public static Document convertFilesToXml(List fileList) {
        Element workflowProcessNode = new Element(
                ProcessResult.WORKFLOW_PROCESS_NODE_NAME);

        Element responseNode = new Element(ProcessResult.RESPONSE_NODE_NAME);
        workflowProcessNode.addContent(responseNode);

        for (int i = 0; i < fileList.size(); i++) {
            Object obj = fileList.get(i);
            if (!(obj instanceof File)) {
                continue;
            }
            File file = (File) fileList.get(i);
            Element fileNode = new Element(ProcessResult.FILE_NODE_NAME);
            fileNode.setAttribute(ProcessResult.FILE_NAME_ATT_NAME, PathUtil
                    .getNameWithoutExtension(file.getName()));
            WorkFlowMethod method=new WorkFlowMethod();
            String workId= method.getworkId(file.getName().substring(0, file.getName().indexOf(".")));
            fileNode.setAttribute("workId", workId);
            responseNode.addContent(fileNode);
        }

        return new Document(workflowProcessNode);
    }

    public static String WORKFLOW_PROCESS_NODE_NAME = "WorkflowProcess";

    public static String RESPONSE_NODE_NAME = "Response";

    public static String STATUS_ATT_NAME = "status";

    public static String NAME_ATT_NAME = "name";

    public static String MES_ATT_NAME = "mes";

    public static String FILE_NODE_NAME = "File";

    public static String FILE_NAME_ATT_NAME = "name";

    public static String FILE_LASTTIME_ATT_NAME = "lasttime";

    private int status;

    private String name;

    private String mes;

    private Process process;
}
