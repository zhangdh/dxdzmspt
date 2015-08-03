
/**
 * <p>Title:  </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) xio.name 2006</p>
 * @author xio
 */
function XiorkFlowViewPattern(ui) {
    this.base = Frame;
    this.base(ui);
    this.ui.style.overflow = "auto";
    
    //
    this.xiorkFlowToolBar = new XiorkFlowViewerToolBar();
    this.add(this.xiorkFlowToolBar);

    //
    this.xiorkFlowViewer = new XiorkFlowViewer();
    this.xiorkFlowViewer.setWidth("100%");
    this.xiorkFlowViewer.setHeight("100%");
    this.viewerRow = this.add(this.xiorkFlowViewer);

    //
    this.tableViewer = new XiorkFlowTableViewer();
    this.tableViewer.setWidth("100%");
    this.tableViewer.setHeight("100%");
    this.tableViewerRow = this.add(this.tableViewer);
    this.tableViewer.setDisplay("none");
    
     // wang test
    this.tableViewerTest = new XiorkFlowTableViewerTest();
    this.tableViewerTest.setWidth("100%");
    this.tableViewerTest.setHeight("100%");
    this.tableViewerRowTest = this.add(this.tableViewerTest);
    this.tableViewerTest.setDisplay("none");
    

    //
    this.statusPanel = new StatusLabel();
    //欢迎使用XiorkFlow工作流定制系统
   /// this.statusPanel.setText("\u6b22\u8fce\u4f7f\u7528XiorkFlow\u5de5\u4f5c\u6d41\u5b9a\u5236\u7cfb\u7edf");
    this.add(this.statusPanel);

	//
    this.xiorkFlowToolBar.getViewerPatternButtonGroup().addObserver(this);

    //
    var model = new XiorkFlowModel();
    model.setEditable(false);
    this.xiorkFlowWrapper = new XiorkFlowWrapper(this.xiorkFlowViewer, model, this.stateMonitor, this.statusPanel);
    this.tableViewer.setModel(this.xiorkFlowWrapper.getModel());
}
XiorkFlowViewPattern.prototype = new Frame();
XiorkFlowViewPattern.prototype.getToolBar = function () {
    return this.xiorkFlowToolBar;
};
XiorkFlowViewPattern.prototype.getStatusLabel = function () {
    return this.statusPanel;
};
XiorkFlowViewPattern.prototype.getWrapper = function () {
    return this.xiorkFlowWrapper;
};
XiorkFlowViewPattern.prototype.getTableViewer = function () {
    return this.tableViewer;
};
var count=0;
//
XiorkFlowViewPattern.prototype.update = function (observable, arg) {
    if (arg == ButtonGroup.PRESSED_CHANGED) {
        //
        
        if (observable == this.getToolBar().getViewerPatternButtonGroup()) {
            var pressedButtonModel = this.getToolBar().getViewerPatternButtonGroup().getPressedButtonModel();
            var modelName = pressedButtonModel.name;
            switch (modelName) {
              case XiorkFlowViewerToolBar.BUTTON_NAME_DESIGN:
                this.xiorkFlowViewer.setDisplay("");
                this.viewerRow.style.display = "";
                this.xiorkFlowViewer.setHeight("100%");
                this.viewerRow.height = "100%";
                this.tableViewer.setDisplay("none");
                this.tableViewerRow.style.display = "none";
                this.getStatusLabel().setText("\u8bbe\u8ba1\u6a21\u5f0f");
                break;
              case XiorkFlowViewerToolBar.BUTTON_NAME_TABLE:
                this.xiorkFlowViewer.setDisplay("none");
                this.viewerRow.style.display = "none";
                this.tableViewer.setDisplay("");
                this.tableViewerRow.style.display = "";
                this.tableViewer.setHeight("100%");
                this.tableViewerRow.height = "100%";
                this.getStatusLabel().setText("\u8868\u683c\u6d4f\u89c8\u6a21\u5f0f");
                break;
              case XiorkFlowViewerToolBar.BUTTON_NAME_MIX:
                this.xiorkFlowViewer.setDisplay("");
                this.viewerRow.style.display = "";
                this.xiorkFlowViewer.setHeight("100%");
                this.viewerRow.height = "100%";
                this.tableViewer.setDisplay("");
                this.tableViewerRow.style.display = "";
                this.tableViewer.setHeight("200px");
                this.tableViewerRow.height = "200px";
                this.getStatusLabel().setText("\u8bbe\u8ba1\u6a21\u5f0f\u3001\u8868\u683c\u6a21\u5f0f\u540c\u65f6\u663e\u793a");
                break;
                
              // wang 
              case XiorkFlowViewerToolBar.BUTTON_NAME_HISTORY:
              count++;
               // this.xiorkFlowViewer.setDisplay("none");
               // this.viewerRow.style.display = "none";
               // this.tableViewerTest.setDisplay("");
               // this.tableViewerRowTest.style.display = "";
               // this.tableViewerTest.setHeight("100%");
               // this.tableViewerRowTest.height = "100%";
               // this.getStatusLabel().setText("历史记录查看");
              // alert(id);
               // window.showModalDialog("../FindHistorySteps?id="+id,null);
                 //window.open("../FindHistorySteps?id="+id,null,"height=200,width=400,status=yes,toolbar=no,menubar=no,location=no");
			  if(count!=2&&count!=3){
			  	window.showModalDialog(sys_ctx+"/oswf/manage/default.do?method=showInstanceHis&id="+id, opener, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
             	}
              break;
              default:
                break;
            }
            return;
        }

        //
        return;
    }
};

//
function XiorkFlowViewerToolBar() {
    this.base = ToolBar;
    this.base();

    //
    this.addSeparator();

    //
    this.viewerPatternButtonGroup = new ButtonGroup();

    //design
    var designButton = new ToggleButton("", "\u8bbe\u8ba1", true);
    this.add(designButton);
    this.viewerPatternButtonGroup.add(designButton);
    designButton.getModel().name = XiorkFlowViewerToolBar.BUTTON_NAME_DESIGN;

    //table
    var tableButton = new ToggleButton("", "\u8868\u683c", true);
    this.add(tableButton);
    this.viewerPatternButtonGroup.add(tableButton);
    tableButton.getModel().name = XiorkFlowViewerToolBar.BUTTON_NAME_TABLE;

    //混合显示
    var mixButton = new ToggleButton("", "\u6df7\u5408\u663e\u793a", true);
    this.add(mixButton);
    this.viewerPatternButtonGroup.add(mixButton);
    mixButton.getModel().name = XiorkFlowViewerToolBar.BUTTON_NAME_MIX;
    
    if(id!=null){
	    //历史记录 wang
	    var historyButton = new ToggleButton("", "历史记录", true);
	    this.add(historyButton);
	    this.viewerPatternButtonGroup.add(historyButton);
	    historyButton.addActionListener(new SaveActionListener(this.xiorkFlow));
	    historyButton.getModel().name = XiorkFlowViewerToolBar.BUTTON_NAME_HISTORY;
    }
    
   
    
}
XiorkFlowViewerToolBar.prototype = new ToolBar();
XiorkFlowViewerToolBar.prototype.setButtonEnable = function (b) {
    var viewPatternbuttons = this.viewerPatternButtonGroup.getButtons();
    for (var i = 0; i < viewPatternbuttons.size(); i++) {
        viewPatternbuttons.get(i).getModel().setEnabled(b);
    }
};
XiorkFlowViewerToolBar.prototype.getViewerPatternButtonGroup = function () {
    return this.viewerPatternButtonGroup;
};

//
XiorkFlowViewerToolBar.BUTTON_NAME_DESIGN = "BUTTON_NAME_DESIGN";
XiorkFlowViewerToolBar.BUTTON_NAME_TABLE = "BUTTON_NAME_TABLE";
XiorkFlowViewerToolBar.BUTTON_NAME_MIX = "BUTTON_NAME_MIX";
XiorkFlowViewerToolBar.BUTTON_NAME_HISTORY = "BUTTON_NAME_HISTORY";


