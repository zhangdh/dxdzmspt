
/**
 * <p>Title:  </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) xio.name 2006</p>
 * @author xio
 */
/**
 *
 * @param viewer Component
 * @param model XiorkFlowModel
 */
function XiorkFlowWrapper(viewer, model, stateMonitor, statuser) {
	//
    if (stateMonitor) {
        this.setStateMonitor(stateMonitor);
        this.getStateMonitor().addObserver(this);
        this.transitionMonitor = new TransitionMonitor();
    }

	//
    this.metaNodes = new Array();
    this.transitions = new Array();
    //权限
    this.roles = new Array();
    //wang 当前步骤20081228
    this.currentSteps=new Array();
    this.historySteps=new Array();
    
    this.selectedRoleConfigModels=new Array();
    this.selectedJoinConfigModels=new Array();
    this.selectedFunctionConfigModels=new Array();
    this.selectedFormConfigModels=new Array();
    this.selectedModel="";
    //流程类别
    this.selectedWkType="";
	//
    this.nodeMouseListener = new Array();
    this.transitionMouseListener = new Array();

    //
    if (model == null) {
        model = new XiorkFlowModel();
    }
    this.setModel(model);

    //
    if (viewer == null) {
        return;
    }
    this.setViewer(viewer);

    //
    this.statuser = statuser;
    this.setChanged(false);
}

//
XiorkFlowWrapper.prototype.setStateMonitor = function (stateMonitor) {
    this.stateMonitor = stateMonitor;
};
XiorkFlowWrapper.prototype.getStateMonitor = function () {
    return this.stateMonitor;
};

//
XiorkFlowWrapper.prototype.setChanged = function (b) {
    this.changed = b;
};
XiorkFlowWrapper.prototype.isChanged = function () {
    return this.changed;
};

//
XiorkFlowWrapper.prototype.setStatusInfo = function (text) {
    if (this.statuser) {
        this.statuser.setText(text);
    }
};

//
XiorkFlowWrapper.prototype.getTransitionMonitor = function () {
    return this.transitionMonitor;
};

//
XiorkFlowWrapper.prototype._initViewer = function () {
    this.getViewer().setPosition("relative");

    //
    if (this.getModel().isEditable()) {
        this.getViewer().addMouseListener(new WrapperMetaMouseListener(this));
        this.getViewer().addMouseListener(new WrapperSelectMouseListener(this));
        this.getViewer().addMouseListener(new MetaMoveMouseListener(this));
        this.getViewer().addMouseListener(new WrapperTransitionMouseListener(this));
        this.getViewer().addKeyListener(new MetaMoveKeyListener(this));
    }
};
XiorkFlowWrapper.prototype._updateViewer = function () {

    Toolkit.clearElement(this.getViewer());
    var model = this.getModel();
    var viewer = this.getViewer();
    var t = new Array();//transitions
    var node=new Array();
    //
    var currentArray=this.currentSteps;
    var stepArray= currentArray.concat(this.historySteps);
    var length=stepArray.length;
    for(var j=0;j<length;j++){
	    getLink(stepArray[j],t,this,node,this.historySteps);
    }
    
    var metaNodeModels = model.getMetaNodeModels();
    for (var i = 0; i < metaNodeModels.size(); i++) {
        var metaNodeModel = metaNodeModels.get(i);
      // alert(metaNodeModel.getFroms()[0].getID()+" "+metaNodeModel.getID());
        var metaNode = XiorkFlowWrapper.convertMetaNodeModelToMetaNode(metaNodeModel, this);
         for(var j=0;j<length;j++){
	        if(metaNodeModel.getID()==currentArray[j]){
	            metaNode.setBgolor("#66AD77");
	            metaNode.getUI().style.borderColor="red";
	           /* if(metaNodeModel.getTos()[0].getFromMetaNodeModel().type=='FORK_NODE'){
	             
	            }*/
	         }
         }
         node= node.concat(this.historySteps);
         for(var j=0;j<node.length;j++){
	        if(metaNodeModel.getID()==node[j]){
	           metaNode.setBgolor("#66AD77");
	           // metaNode.getUI().style.borderColor="red";
	         }
         }
        this.addMetaNode(metaNode);
    }
    var transitionModels = model.getTransitionModels();
    for (var i = 0; i < transitionModels.size(); i++) {
        var transitionModel = transitionModels.get(i);
       // alert(transitionModel.getFromMetaNodeModel());
        var transition = XiorkFlowWrapper.convertTransitionModelToTransition(transitionModel, this);
        for (var m = 0; m < t.length; m++) {
          if(transitionModel.getID()==t[m]){
              transition.getUI().strokecolor="red";
          }
        }
        this.addTransition(transition);
    }
};
/**
XiorkFlowWrapper.prototype.getLink=function(currentStep){
 t=new Array();
 var transitionModels = this.getModel().getTransitionModels();
    for (var i = 0; i < transitionModels.size(); i++) {
        var transitionModel = transitionModels.get(i);
        var id=transitionModel.getToMetaNodeModel().getID();
        if(id==currentStep){
           t.push(transitionModel.getID());
           this.getLink(transitionModel.getFromMetaNodeModel().getID());
        }
   }
   return t;
}
*/
function getLink(currentStep,t,obj,node,historyArray){
 var transitionModels = obj.getModel().getTransitionModels();
    for (var i = 0; i < transitionModels.size(); i++) {
        var transitionModel = transitionModels.get(i);
        var id=transitionModel.getToMetaNodeModel().getID();
       
        if(id==currentStep){
           t.push(transitionModel.getID());
           if(transitionModel.getToMetaNodeModel().getFroms()!=""){
            t.push(transitionModel.getToMetaNodeModel().getFroms()[0].getID());
           }
          
           node.push(transitionModel.getFromMetaNodeModel().getID());
           getLink(transitionModel.getFromMetaNodeModel().getID(),t,obj,node,historyArray);
        }
        
   }
}
//
XiorkFlowWrapper.prototype.setViewer = function (viewer) {
    if (!viewer) {
        return;
    }
    if (this.viewer == viewer) {
        return;
    }
    if (this.viewer) {
        this.viewer.listenerProxy.clear();//clear memory
    }
    this.viewer = viewer;
    this._initViewer();
};
XiorkFlowWrapper.prototype.getViewer = function () {
    return this.viewer;
};

//
XiorkFlowWrapper.prototype.setModel = function (model) {
    if (!model) {
        return;
    }
    if (this.model == model) {
        return;
    }
    if (this.transitionMonitor) {
        this.transitionMonitor.reset();
    }
    this.model = model;
    this.model.addObserver(this);
    this._updateViewer();
};
XiorkFlowWrapper.prototype.getModel = function () {
    return this.model;
};

//
XiorkFlowWrapper.prototype.addMetaNode = function (metaNode) {
    this.metaNodes.add(metaNode);
    this.getViewer().add(metaNode);
    if (this.getModel().isEditable()) {
        metaNode.addMouseListener(new MetaNodeMouseListener(metaNode.getModel(), this));
        metaNode.addMouseListener(new MetaNodeTextMouseListener(metaNode, this));
        metaNode.addKeyListener(new MetaNodeTextKeyListener(metaNode, this));
    }
};
XiorkFlowWrapper.prototype.removeMetaNode = function (metaNode) {
    metaNode.listenerProxy.clear();//clear memory
    this.metaNodes.remove(metaNode);
    this.getViewer().remove(metaNode);
};
XiorkFlowWrapper.prototype.addTransition = function (transition) {
    if (this.getModel().isEditable()) {
        transition.addMouseListener(new TransitionMouseListener(transition.getModel(), this));
        transition.addMouseListener(new TransitionTextMouseListener(transition, this));
        transition.addKeyListener(new TransitionTextKeyListener(transition, this));
    }
    this.transitions.add(transition);
    this.getViewer().add(transition);
    this.getViewer().add(transition.getLineText());
};
XiorkFlowWrapper.prototype.removeTransition = function (transition) {
    transition.listenerProxy.clear();//clear memory
    this.transitions.remove(transition);
    this.getViewer().remove(transition);
    this.getViewer().remove(transition.getLineText());
};
//权限
XiorkFlowWrapper.prototype.addRole = function (role) {
    this.roles.add(role);
};


//
XiorkFlowWrapper.convertMetaNodeModelToMetaNode = function (metaNodeModel, wrapper) {
    var type = metaNodeModel.type;
    var metaNode = null;
    switch (type) {
      case MetaNodeModel.TYPE_NODE:
        metaNode = new Node(metaNodeModel, wrapper);
        break;
      case MetaNodeModel.TYPE_FORK_NODE:
        metaNode = new ForkNode(metaNodeModel, wrapper);
        break;
      case MetaNodeModel.TYPE_JOIN_NODE:
        metaNode = new JoinNode(metaNodeModel, wrapper);
        break;
      case MetaNodeModel.TYPE_START_NODE:
        metaNode = new StartNode(metaNodeModel, wrapper);
        break;
      case MetaNodeModel.TYPE_END_NODE:
        metaNode = new EndNode(metaNodeModel, wrapper);
        break;
      default:
        break;
    }
    return metaNode;
};
XiorkFlowWrapper.convertTransitionModelToTransition = function (transitionModel, wrapper) {
    return new Transition(transitionModel, wrapper);
};

//
XiorkFlowWrapper.prototype.addNodeMouseListener = function () {
};
XiorkFlowWrapper.prototype.removeNodeMouseListener = function () {
};
XiorkFlowWrapper.prototype.addTransitionMouseListener = function () {
};
XiorkFlowWrapper.prototype.removeTransitionMouseListener = function () {
};

//
XiorkFlowWrapper.prototype.update = function (observable, arg) {
    if (arg instanceof Array) {
        this.setChanged(true);
        if (arg.size() == 2) {
            var property = arg[0];
            var model = arg[1];
            switch (property) {
              case XiorkFlowModel.META_NODE_MODEL_ADD:
                var node = XiorkFlowWrapper.convertMetaNodeModelToMetaNode(model, this);
                this.addMetaNode(node);
                break;
              case XiorkFlowModel.META_NODE_MODEL_REMOVE:
                model.suicide();
                break;
              case XiorkFlowModel.TRANSITION_MODEL_ADD:
                var transition = XiorkFlowWrapper.convertTransitionModelToTransition(model, this);
                this.addTransition(transition);
                break;
              case XiorkFlowModel.TRANSITION_MODEL_REMOVE:
                model.suicide();
                break;
              default:
                break;
            }
        }
        return;
    }
    if (arg == StateMonitor.OPERATION_STATE_CHANGED) {
        var state = this.getStateMonitor().getState();
        if (state == StateMonitor.SELECT) {
            this.getViewer().setCursor(Cursor.AUTO);
            this.getViewer().getUI().focus();
        } else {
            this.getViewer().setCursor(Cursor.CROSSHAIR);
        }
        return;
    }
};

