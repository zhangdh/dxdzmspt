<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<html>
  <head>
    <title>节点配置</title>
  </head>
  <script charset="UTF-8" src="${ctx}/js/XiorkFlow/js/XiorkFlowWorkSpace.js" language="javascript"></script>
  <script>
  		var obj = window.dialogArguments;
  		var wrapper = obj.xiorkFlow.getWrapper();
		var toolbar = obj.xiorkFlow.getToolBar();
		var model = wrapper.getModel();
		var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
		var nodeID = selectedMetaNodeModels.get(0).getID();
		var nodeName=selectedMetaNodeModels.get(0).getText();//节点名称
		var nodeType=selectedMetaNodeModels.get(0).type;//节点类型
		var startNode="";
		//授权定义
		var  roleArray=new Array();
        XiorkFlowWorkSpace.build();
        var workflow="workflow";
        var roletype="roletype";
        var taskallocation="taskAllocation";
        var con="conditionType";
        var backNode="backNode";
        var autoNode="autoNode";
        //表单设置列表
        var formFiled;
        var doc3;
		//判断是否为首节点
		if(nodeType=="START_NODE"){
      		startNode="yes";
      	}

      	//页面加载
		window.onload=function(){
	         //授权
	         if(startNode=="yes"){
             	document.getElementById("listDiv").style.display="none";
             }else{
             	document.all.showGwConfig.style.display="";
             }
             var arry=model.selectedRoleConfigModels;
		     if(arry==""){
		        arry=wrapper.selectedRoleConfigModels;
		     }else{
			     var rt=true;
			     for(var i=0;i<arry.length;i++){
			     	  var xml=arry[i];
			          var  doc=XMLDocument.newDomcument(); 
				      doc.async="false"; 
					  doc.loadXML(xml);
					  var stepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
					  if(stepId==nodeID){
					    rt=false;
					  }
			     }
			     if(rt){
			       arry=wrapper.selectedRoleConfigModels;
			     }
		     }
		     for(var i=0;i<arry.length;i++){
	             var xml=arry[i];
		          var  doc=XMLDocument.newDomcument(); 
			      doc.async="false"; 
				  doc.loadXML(xml);
				  var stepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				  //判断步骤
				  if(stepId!=nodeID){
					continue;
				  }
				  var radio;
				  var task=doc.getElementsByTagName("workflow/taskAllocation")[0].getAttribute("value");//指定待办人
				  radio=document.getElementsByName("doType");
				  for(var j=0;j<radio.length;j++){
				     var radioObj=radio[j];
				     if(radioObj.value==task){
				        radioObj.checked=true;
				     }
				  }
				 
				  var con=doc.getElementsByTagName("workflow/conditionType")[0].getAttribute("value");//审核人数
				  radio=document.getElementsByName("condition");
				  for(var j=0;j<radio.length;j++){
				     var radioObj=radio[j];
				     if(radioObj.value==con){
				        radioObj.checked=true;
				     }
				     if(con=="0"){//多人审核
				       var  amountvalue=doc.getElementsByTagName("workflow/conditionType")[0].getAttribute("amount")
				       addInput();
				       document.getElementById("amount").value=amountvalue;
				     }
				  }
				  
				  var back=doc.getElementsByTagName("workflow/backNode")[0].getAttribute("value");//是否回退
				  radio=document.getElementsByName("back");
				  for(var j=0;j<radio.length;j++){
				     var radioObj=radio[j];
				     if(radioObj.value==back){
				        radioObj.checked=true;
				     }
				  }
				  
				  var auto=doc.getElementsByTagName("workflow/autoNode")[0].getAttribute("value");//自动节点
				   radio=document.getElementsByName("autoType");
				  for(var j=0;j<radio.length;j++){
				     var radioObj=radio[j];
				     if(radioObj.value==auto){
				        radioObj.checked=true;
				     }
				     if(auto=="1"){//自动节点
				       var  content=doc.getElementsByTagName("workflow/autoNode")[0].getAttribute("formula")
				       var  autoInput=document.getElementById("autoName");
				       add_Row2(autoInput);
				       var fo=content.split(",");
				       var configSelect=document.getElementById("configselect");
				       var optionsObj=configSelect.options;
				       for(var m=0;m<optionsObj.length;m++){
				         if(optionsObj[m].value==fo[0]){
				            optionsObj[m].selected=true;
				         }
				       }
	      			   var conselect=document.getElementById("conselect");
	      			   optionsObj=conselect.options;
				       for(var m=0;m<optionsObj.length;m++){
				         if(optionsObj[m].value==fo[1]){
				            optionsObj[m].selected=true;
				         }
				       }
	      			   
   		   			   document.getElementById("conValue").value=fo[2];
				       
				     }
				     
				  }
				  
				  var remind=doc.getElementsByTagName("workflow/remind")[0].getAttribute("value");//提醒
				   radio=document.getElementsByName("remind");
				  for(var j=0;j<radio.length;j++){
				     var radioObj=radio[j];
				     if(radioObj.value==remind){
				        radioObj.checked=true;
				     }
				     if(remind=="0"){//提醒
				       var  content=doc.getElementsByTagName("workflow/remind")[0].getAttribute("content");
				       var  contime=doc.getElementsByTagName("workflow/remind")[0].getAttribute("contime");
				       addremind();
				       document.getElementById("content").value=content;
				       document.getElementById("contime").value=contime;
				       
				     }
				  }
				  //权限 
				   var roles=doc.getElementsByTagName("workflow/roletype");//权限
				   var roleTypeCode="";
				   var roleValues="";
				   //得到所有选中项目
				    for(var j=0;j<roles.length;j++){
				       var role=roles[j];
				       var roleType=role.getAttribute("type");//类型(0部门1个人2岗位)
				   	   var roleValue=role.getAttribute("value");
				   	   if(roleType=="0"){
				   	   		roleTypeCode="bm";
				   	   }else if(roleType=="1"){
				   	   		roleTypeCode="yh";
				   	   }else if(roleType=="2"){
				   	   		roleTypeCode="gw";
				   	   }
				       roleValues=roleValues+roleTypeCode+roleValue+",";
				    }
				    document.getElementById("grantFrame").location="${ctx}/org/default.do?method=select_pub&show=yh,bm,gw&issingle=1&initItems="+roleValues;
				}
				
				/*
				节点权限增加
				*/
			//条件
			var arry1=model.selectedJoinConfigModels;
		      if(arry1==""){
		       	arry1=wrapper.selectedJoinConfigModels;
		      }else{
			     var rt=true;
			     for(var i=0;i<arry1.length;i++){
			     	  var xml=arry1[i];
			          var  doc=XMLDocument.newDomcument(); 
				      doc.async="false"; 
					  doc.loadXML(xml);
					  var stepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
					  if(stepId==nodeID){
					    rt=false;
					  }
			     }
			     if(rt){
			       arry1=wrapper.selectedFormConfigModels;
			     }
		     }
	         for(var i=0;i<arry1.length;i++){
	             var xml=arry1[i];
		          var  doc=XMLDocument.newDomcument(); 
			      doc.async="false"; 
				  doc.loadXML(xml);
				  var stepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				  //判断步骤
				    if(stepId==nodeID){
					  var joinconfig=doc.getElementsByTagName("workflow/jionconfig");
					  var value=joinconfig[0].getAttribute("value");
					  var convalue=document.getElementById("conValue");
					  convalue.value=value;
					}
		     }
		     			
		     
		   //功能按钮
		   		var arry2=model.selectedFunctionConfigModels;
				if(arry2==""){
					arry2=wrapper.selectedFunctionConfigModels;
				}
				for(var i=0;i<arry2.length;i++){
					var xml=arry2[i];
					var  doc=XMLDocument.newDomcument(); 
					doc.async="false"; 
					  doc.loadXML(xml);
					 // alert(xml);
					  var stepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
					  //判断步骤
					  if(stepId==nodeID){
						  var functionIds=doc.getElementsByTagName("workflow/functions");
						  
						  for(var t=0;t<functionIds.length;t++){
						    
						     var f=functionIds[t];
						     //类型值
						     var fId=f.getAttribute("value");
						     var objs=document.getElementsByName("functions");
						     //判断那类功能
							  	for(var n=0;n<objs.length;n++){
							  		 var obj=objs[n];
									  if(fId==obj.value){
									      obj.checked=true;
									      var cell=obj.parentNode.parentNode.childNodes[1];
									      var c  = cell.childNodes;
										  //遍历类别节点
								     	for(var n=0;n<c.length;n++){
										   var p = c[n];
										   if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
											  p.disabled=false;
											  var childes=f.childNodes;
											  for(var j=0;j<childes.length;j++){
													 var child=childes[j];
													 var value=child.getAttribute("value");
													 if(p.value==value){
													    p.checked=true;
													 }
											   }
											}
									    }
									  }
							  	}
						}
					}
				}
			
	     };
  		//显示表单设置
  		function showForm(obj){
				var name = wrapper.getModel().getName();
				var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
				if (selectedMetaNodeModels == null || selectedMetaNodeModels == "") {
					alert("没有选择任何节点");
					return;
				}
				//得到选中节点ID
				var nodeID = selectedMetaNodeModels.get(0).getID();
			    //判断模板id
			    if(wrapper.selectedModel!=null&&wrapper.selectedModel!=""){
			        $(function(){
				        $.ajax({type:"POST", url:"${ctx}/oswf/graphicdesigner/default.do?method=showFormConfig", data:"formId="+wrapper.selectedModel, dataType:"json", success:function (msg) {
							formFiled=msg.modelList;
							var connectStr="";
							//alert(formFiled[0][0]);
							if(formFiled.length!=0){
								connectStr=connectStr+"<table class=c_table_list align=center width=100% border=1>";
								for(var i=0;i<formFiled.length;i++){
									var id=formFiled[i][0];
		            	  			var value=formFiled[i][1];
		            	  			var remark=formFiled[i][2];
		            	  			
									connectStr=connectStr+"<tr align=center >";
									connectStr=connectStr+"<td>"+remark;
								    connectStr=connectStr+"<input type=checkbox onclick='disable(this);' checked name="+value+" id="+id+" value="+value+" /> ";
								    connectStr=connectStr+"</td>";
								    connectStr=connectStr+"<td>";       
								    connectStr=connectStr+"<input type=radio checked  name=roles"+value+" value=0 />可编辑 ";
								    connectStr=connectStr+"<input type=radio  name=roles"+value+" value=1 />可见 ";
								    connectStr=connectStr+"<input type=radio   name=roles"+value+" value=2 />不可见 ";
								    connectStr=connectStr+"<input type=checkbox  checked name=check"+value+" id=check"+id+" value=0 />非空 ";
								    connectStr=connectStr+"</td>";
									connectStr=connectStr+"</tr>";	
								}
								connectStr=connectStr+"</table>";
							}else{
								connectStr="<font color=red>没有可供选择的表单列</font>";
							}
							document.getElementById("showFormFiled").innerHTML=connectStr;
							//表单设置
							var m_formConfig=model.selectedFormConfigModels;
							if(m_formConfig.length!=0){
						      for(var i=0;i<m_formConfig.length;i++){
						          var xml=m_formConfig[i];
						          var  doc=XMLDocument.newDomcument(); 
								  doc.async="false"; 
								  doc.loadXML(xml);
								  var m_stepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
								  var w_formConfig=wrapper.selectedFormConfigModels;
								  for(var j=0;j<w_formConfig.length;j++){
								     var xml2=w_formConfig[j];
								  	 var  doc2=XMLDocument.newDomcument(); 
								 	 doc2.async="false"; 
								 	 doc2.loadXML(xml2);
								 	 var w_stepId=doc2.getElementsByTagName("step")[0].getAttribute("stepId");
								 	 if(m_stepId==w_stepId){
								 	    wrapper.selectedFormConfigModels.removeById(j);
								 	 }
								  }
						       }
						     }
						     model.selectedFormConfigModels=model.selectedFormConfigModels.concat(wrapper.selectedFormConfigModels);
					         var arry=model.selectedFormConfigModels;
					         for(var i=0;i<arry.length;i++){
					             var xml=arry[i];
						          var  doc=XMLDocument.newDomcument(); 
							      doc.async="false"; 
								  doc.loadXML(xml);
								  var stepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
								  //判断步骤
								  if(stepId==nodeID){
									  var tdIds=doc.getElementsByTagName("step/tdId");
									  for(var t=0;t<tdIds.length;t++){
									     var tdId=tdIds[t];
									     var tdIdvalue=tdId.getAttribute("value");
									     var role=tdId.childNodes[0];
									     var roleValue=role.getAttribute("value");
									     var tdObj=document.getElementsByName(tdIdvalue);
									     tdObj[0].checked=true;
									     var cell=tdObj[0].parentNode.parentNode.childNodes[1];
									     var c  = cell.childNodes;
									     for(var n=0;n<c.length;n++){
									        var p = c[n];
									        if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
									          p.disabled=false;
									          if(p.value==roleValue){
									           	p.checked=true;
									          }
									        }
										 }
									}
								}
				         	}
						}});
					});
				}else{
				    alert("请先设置模板");
					return;
				}
  		}
  		
  		//确定
  		function getSelect(){
			//授权
				var option;
				var doc = XMLDocument.newDomcument();
		      	//root
		      	
			  	var workflowNode = doc.createElement(workflow);
			  	workflowNode.setAttribute("nodeId",nodeID);
			  	workflowNode.setAttribute("nodeName",nodeName);
			  	workflowNode.setAttribute("nodeType",nodeType);
			   	doc.documentElement = workflowNode;
		   		//权限
		   		var grant=window.grantFrame.confirmSelect();
		   		
		   		if(grant==undefined || grant==""){
		   			return;
		   		}else{
		   			//按照角色-2,按照人员-1,按照部门-0
		   			grant=grant.substring(0,grant.length-1);
			   		var roleType=grant.substring(0,2);
			   		if(roleType=='gw'){
			   			option='2';
			   		}else if(roleType=='yh'){
			   			option='1';
			   		}else if(roleType='bm'){
			   			option='0';
			   		}
			   		var grantList=grant.split(",");
			   		for(i=0;i<grantList.length;i++){
			   			var roletypeNode = doc.createElement(roletype);
			   			roletypeNode.setAttribute("type",option);
			    		roletypeNode.setAttribute("value",grantList[i].substring(2));
			    		//选择岗位时进行设置 
			    		if(roleType=='gw'){
			    			var gwrole=document.getElementsByName("gwRole");
			    			for(i=0;i<gwrole.length;i++){
			    				if(gwrole[i].checked==true){
			    					roletypeNode.setAttribute("gwrole",gwrole[i].value);
			    					break;
			    				}
			    			}
			    		}
	   		   			workflowNode.appendChild(roletypeNode);
			   		}
		   		}
		   		//任务分配类型
		   		var taskNode= doc.createElement(taskallocation);
	   		   	var doType=document.getElementsByName("doType");
		       	for(var i=0;i<doType.length;i++){
		          	if(doType[i].checked==true){
		          		option=doType[i];
		          	}
		        }
	   		   	taskNode.setAttribute("value",option.value);
	   		   	workflowNode.appendChild(taskNode);
	   		   	
	   		   	//审核配置
	   		    var conNode= doc.createElement(con);
	   		    var condition=document.getElementsByName("condition");
		        for(var i=0;i<condition.length;i++){
		           if(condition[i].checked==true){
		           		option=condition[i];
		          }
		        }
		       conNode.setAttribute("value",option.value);
		       if(option.value=="0"){
		       //如果是多人审核，读取人员个数
		        var amount=document.getElementById("amount");
		         conNode.setAttribute("amount",amount.value);
		       }
	   		   workflowNode.appendChild(conNode);
	   		   
	   		   
	   		   //添加是否回退
   		   		var newNode= doc.createElement(backNode);
   		   		var backObj=document.getElementsByName("back");
	       		for(var i=0;i<backObj.length;i++){
	          		if(backObj[i].checked==true){
	          			option=backObj[i];
	          		}
	        	}
	        	newNode.setAttribute("value",option.value);
	        	workflowNode.appendChild(newNode);
		   		
		   		//节点，类型，0手工执行，1自动节点
		       var   isAutoNode= doc.createElement(autoNode);
	   		   var autoRadio=document.getElementsByName("autoType");
		       for(var i=0;i<autoRadio.length;i++){
		          if(autoRadio[i].checked==true){
		          	option=autoRadio[i];
		          }
		        }
		        isAutoNode.setAttribute("value",option.value);
		        if(option.value=="1"){
		       //自动节点，读取执行的条件
		       var obj=document.getElementById("configselect");
		       var conselect=document.getElementById("conselect");
	   		   var convalue=document.getElementById("conValue");
		        isAutoNode.setAttribute("formula",obj.value+","+conselect.value+","+convalue.value);
		       }
		       workflowNode.appendChild(isAutoNode);
		       
		       // 添加提醒信息
		       var   remindNode= doc.createElement("remind");
	   		   var remidRadio=document.getElementsByName("remind");
	   		     for(var i=0;i<remidRadio.length;i++){
		          if(remidRadio[i].checked==true){
		          	option=remidRadio[i];
		          }
		        }
	   		   remindNode.setAttribute("value",option.value);
	   		   if(option.value=="0"){
	   		      var content=document.getElementById("content");
	   		     remindNode.setAttribute("content",content.value);
	   		     var contime=document.getElementById("contime");
	   		     remindNode.setAttribute("contime",contime.value);
	   		   }
	   		    workflowNode.appendChild(remindNode);
	   		    var roleConfig=doc.xml;
	   		    //设置xiork
	   		    if(roleConfig!=null){
					//是否已经设置
					var roleConfigModels=model.selectedRoleConfigModels;
					var  doc=XMLDocument.newDomcument(); 
				    doc.async="false"; 
					doc.loadXML(roleConfig);
					var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
					for(var i=0;i<roleConfigModels.length;i++){
						 var xml=roleConfigModels[i];
						 var xmlDoc=XMLDocument.newDomcument(); 
						 xmlDoc.async="false"; 
						 xmlDoc.loadXML(xml);
						 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
						 if(stepId==currentStepId){
						    model.selectedRoleConfigModels.removeById(i);
						    break;
						 }
					}
					model.selectedRoleConfigModels.add(roleConfig);
			  	}
			 	//条件
			 	var convalue=document.getElementById("conValue");
			 	if(convalue.value!=""){
	         		 var con1="jionconfig";
		      		 var doc1 = XMLDocument.newDomcument();
		      		 //root
				  	 var workflowNode1 = doc1.createElement(workflow);
				  	 workflowNode1.setAttribute("nodeId",nodeID);
				   	 doc1.documentElement = workflowNode1;
				     //条件公式		 
		   		     var conNode= doc1.createElement(con1);
		   		     
		   		     conNode.setAttribute("value",convalue.value);
		   		     workflowNode.appendChild(conNode);
		   		     var joinConfig=doc1.xml;
		   		     if(joinConfig!=null){
					   //是否已经设置
							var joinConfigModels=model.selectedJoinConfigModels;
							var  doc=XMLDocument.newDomcument(); 
						    doc.async="false"; 
							doc.loadXML(joinConfig);
							var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
							for(var i=0;i<joinConfigModels.length;i++){
								 var xml=joinConfigModels[i];
								 var  xmlDoc=XMLDocument.newDomcument(); 
								 xmlDoc.async="false"; 
								 xmlDoc.loadXML(xml);
								 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
								 if(stepId==currentStepId){
								    model.selectedJoinConfigModels.removeById(i);
								    break;
								 }
							}
				   		model.selectedJoinConfigModels.add(joinConfig);
				   	}
			   	}
			   	
			 //功能
          		var functions="functions";
          		var functionNode="function";
          		var doc2 = XMLDocument.newDomcument();
				  var workflowNode2 = doc2.createElement(workflow);
				  workflowNode2.setAttribute("nodeId",nodeID);
				  doc2.documentElement = workflowNode2;
			      var type=document.getElementsByName("functions");
			      for(var i=0;i<type.length;i++){
			        if(type[i].checked){
			   		   var functionsNode= doc.createElement(functions);
			   		   functionsNode.setAttribute("value",type[i].value);
			   		   workflowNode2.appendChild(functionsNode);
			   		   //子节点
			   		   var cell=type[i].parentNode.parentNode.childNodes[1];
			   		   var cContent=cell.childNodes;
			   		   for(var j=0;j<cContent.length;j++){
			   		     var p = cContent[j];
			      		  if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
			      		      if(p.checked==true){
				      		      var fNode = doc.createElement(functionNode);
					  			  fNode.setAttribute("value",p.value);
					  			  functionsNode.appendChild(fNode);
					  		   }
			      		  }
			   		  }
			        }
			       }
			        var functionConfig=doc2.xml;
			        if(functionConfig!=null){
					 //是否已经设置
						var functionConfigModels=model.selectedFunctionConfigModels;
						var  doc=XMLDocument.newDomcument(); 
					    doc.async="false"; 
						doc.loadXML(functionConfig);
						var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
						for(var i=0;i<functionConfigModels.length;i++){
							 var xml=functionConfigModels[i];
							 var  xmlDoc=XMLDocument.newDomcument(); 
							 xmlDoc.async="false"; 
							 xmlDoc.loadXML(xml);
							 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
							 if(stepId==currentStepId){
							    model.selectedFunctionConfigModels.removeById(i);
							    break;
							 }
						}
						model.selectedFunctionConfigModels.add(functionConfig);
					}
			//表单设置
				 var stepNode="step";
		         var formNode="form";
		         var tdNode="tdId";
		         var checkNode="check";
		         var formId=wrapper.selectedModel;
		         doc3 = XMLDocument.newDomcument();
		         var setpElement = doc3.createElement(stepNode);
				 setpElement.setAttribute("stepId",nodeID);
				 doc3.documentElement = setpElement;
				 
				 var formIdElement=doc3.createElement(formNode);
				 formIdElement.setAttribute("formId",formId);
				 setpElement.appendChild(formIdElement);
				 if(formFiled!=undefined){
					 for(var i=0;i<formFiled.length;i++){
						var fid=formFiled[i][0];
					 	var table=document.getElementById(fid);
			          	var tdElement=doc3.createElement(tdNode);
					  	tdElement.setAttribute("value",table.value);
					  	setpElement.appendChild(tdElement);
			          	initElement(table,tdElement);
			         }
			         var formConfig=doc3.xml;
			         if(formConfig!=null){
						//是否已经设置
						var formConfigModels=model.selectedFormConfigModels;
						var  doc=XMLDocument.newDomcument(); 
					    doc.async="false"; 
						doc.loadXML(formConfig);
						var currentStepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
						for(var i=0;i<formConfigModels.length;i++){
							 var xml=formConfigModels[i];
							 var  xmlDoc=XMLDocument.newDomcument(); 
							 xmlDoc.async="false"; 
							 xmlDoc.loadXML(xml);
							 var stepId=xmlDoc.getElementsByTagName("step")[0].getAttribute("stepId");
							
							 if(stepId==currentStepId){
							    model.selectedFormConfigModels.removeById(i);
							    break;
							 }
						}
					    model.selectedFormConfigModels.add(formConfig);
					 }
				 }
				 window.close();
	   		    
		}
		
		var roleNode="role";
		//表单设置(插入元素)
		function initElement(obj,pElement){
	         var cell = obj.parentNode.parentNode.childNodes[1];
	   	 	 var c  = cell.childNodes;
	   	 	  var roleElement=doc3.createElement(roleNode);
		     for(var i=0;i<c.length;i++){
		        var p = c[i];
		        if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
		        	 
		               if(p.checked==true){
		                 if(p.type=='radio'){
							  roleElement.setAttribute("value",p.value);
							  pElement.appendChild(roleElement);
							  
						  }
		               }
		               if(p.type=='checkbox'){
		                      if(p.checked==true){
						      	roleElement.setAttribute("check",p.value);
							  	pElement.appendChild(roleElement);
							  }else{
							  	roleElement.setAttribute("check",'1');
							  	pElement.appendChild(roleElement);
							  }
						  }
	               	 
	             }
		     }
		     
	      }
		
	   //处理  多人通过审核 自动隐藏输入人数的输入框
	   var rt=0;
	   function addInput(){
	      if(rt!=0){
	        return;
	      }
	      rt=1;
	      var condition=document.getElementById("con");
	      condition.innerHTML=condition.innerHTML+"<input  type='text' id='amount' size='2'/> 人";
	   }
	   function delInput(){
	      rt=0;
	      var condition=document.getElementById("con");
	      condition.innerHTML="多人通过审核";
	   }
	   
	   //处理 提醒，自动隐藏和现实输入框 
	   var remind=0;
	   function addremind(){
	      if(remind!=0){
	        return;
	      }
	      remind=1;
	      var condition=document.getElementById("remindId");
	      condition.innerHTML=condition.innerHTML+"时间：<input  type='text' id='contime' size='1'/> 分钟后 </br>"
	      +"提醒内容：<input  type='text' id='content'/> ";
	   }
	   function delremind(){
	      remind=0;
	      var condition=document.getElementById("remindId");
	      condition.innerHTML="提醒";//"<input type='radio' onclick='addremind();' name='remind'  value='0' />";
	   }
	   
	   var bt=0;
	   //自动执行节点
	   function addRow(obj){
   			if(bt!=0){
	        	return;
	        }
	        bt=1;
	       
	        var superObj=obj.parentNode.parentNode.childNodes[3];
		    var opstr="<select size='1' id='configselect'	style='width: 150' onChange=''>";
			opstr+="</select></br>";
			superObj.innerHTML=opstr;
		    superObj.innerHTML=superObj.innerHTML+"<select size='1' id='conselect'	style='width: 150' onChange=''> "+
		     				"<option value='0'>></option> "+
		     				"<option value='1'><</option> "+
		     				"<option value='2'>=</option>"+
		     				"<option value='3'>!=</option>"+
		     				"</select></br>";
		     				
		    superObj.innerHTML=superObj.innerHTML+"<input type='text' size='2' id='conValue' />";
		    
    	}
   	   	function delRow(obj){
	     if(bt!=1){
	        return;
	      }
	      bt=0;
	      var parentNode=obj.parentNode.parentNode.childNodes[3];
	      parentNode.innerHTML="自动执行节点";
	   	}
  		//功能显示
  		function disable(obj){
		   	 var cell = obj.parentNode.parentNode.childNodes[1];
		   	 var c  = cell.childNodes;
		     for(var i=0;i<c.length;i++){
		        var p = c[i];
		        if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
		        	p.checked = obj.checked;
		        	if(obj.checked)
		        		p.disabled=false;
		        	else
		        		p.disabled=true;
		        	}
		     }
		}
  		
		function s_chg(td_now, mCols){
		  //授权
		  if(td_now.id == "s_td_2"){
		  	if(startNode!="yes"){
		  		document.all.showGwConfig.style.display="";
		  	}else{
		  		document.all.showGwConfig.style.display="none";
		  	}
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    document.all.s_td_3.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_4.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_5.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    
		    document.all.tabConfig.style.display="";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		  }
		  //条件
		  if(td_now.id == "s_td_3"){
		  	if(selectedMetaNodeModels.get(0).type!="JOIN_NODE"){
				alert("请选择汇聚节点设置汇聚条件！");
				return;
			}
		    document.all.s_td_1.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_3.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    document.all.s_td_4.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_5.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    
		    document.all.tabModel.style.display="none";
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		  }
		  //功能按钮
		  if(td_now.id == "s_td_4"){
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_3.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_4.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    document.all.s_td_5.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="";
		    document.all.tabForm.style.display="none";
		  }
		  //表单设置
		  if(td_now.id == "s_td_5"){
		  	if(wrapper.selectedModel==null || wrapper.selectedModel==""){
		  		alert("请先设置模板");
				return;
		  	}
		  	
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_3.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_4.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_5.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="";
		    showForm(obj);
		  }
		}
  </script>
  <body>
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100.gif" id="s_td_2" class="s_on" onClick="s_chg(this,2);" style="cursor:hand"><span class="style1">授权</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100_2.gif" id="s_td_3" class="s_on" onClick="s_chg(this,3);" style="cursor:hand"><span class="style1">条件</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100_2.gif" id="s_td_4" class="s_on" onClick="s_chg(this,4);" style="cursor:hand"><span class="style1">功能按钮</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100_2.gif" id="s_td_5" class="s_on" onClick="s_chg(this,5);" style="cursor:hand"><span class="style1">表单设置</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="90%" valign="bottom" align="left">&nbsp;</td>
	  </tr>
	</table>
	<div align="center">
		<table id="tabConfig" width="100%" >
			<tr align="center">
				<td>
				<div style="float: left; width: 100%; height: 100%;">
				<div id="listDiv" style="float: left; width: 100%; height: 50px;">
					<table border="1" id="tab" class="c_table_list">
						<tr>
							<td width="50px">
								<input type="radio" name="doType" value="0" />
							</td>
							<td >
								手工指定待办人
							</td>
							<td width="50px">
								<input type="radio" checked name="doType" value="1" />
							</td>
							<td>
								自动
							</td>
						</tr>
						<tr>
							<td  width="50px">
								<input type="radio" onclick="addInput();" name="condition"
									value="0" />
							</td>
							<td id="con">
								多人通过审核
							</td>
							<td width="50px" >
								<input type="radio" checked name="condition"
									onclick="delInput();" value="1" />
							</td>
							<td>
								单人通过审核
							</td>
							
						</tr>
						<tr>
							<td width="50px">
								<input type="radio" name="back" value="0" />
							</td>
							<td >
								回退
							</td>
							<td width="50px">
								<input type="radio" name="back" checked value="1" />
							</td>
							<td>
								不回退
							</td>
							
						</tr>
						<tr>
							<td width="50px">
								<input type="radio" name="remind" onclick="addremind();"
									value="0" />
							</td>
							<td id="remindId">
								提醒
							</td>
							<td width="50px">
								<input type="radio" name="remind" onclick="delremind();" checked
									value="1" />
							</td>
							<td >
								不提醒
							</td>
							
						</tr>
						<tr>
							<td  width="50px">
								<input type="radio" name="autoType" checked onclick="delRow(this);"
									value="0" />
							</td>
							<td>
								手工执行节点
							</td>
							<td width="50px" >
								<input type="radio" id="autoName" onclick="addRow(this);" name="autoType"
									value="1" />
							</td>
							<td>
								自动执行节点
							</td>
							
						</tr>

					</table>
				</div>
				<hr>
				<table id="showGwConfig" style="display:none" class="c_table_list">
					<tr>
						<td><input type="radio" name="gwRole" value="1" />发起者</td>
						<td><input type="radio" name="gwRole" value="2" />执行者</td>
					</tr>
				</table>
				<div id="buttomDiv" style="float: left; width: 100%; height: 300px;">
						<iframe id="grantFrame" height="400" src="${ctx}/org/default.do?method=select_pub&show=yh,bm,gw&issingle=1"></iframe>
				</div>
			</div>
				</td>
			</tr>
		</table>
		<table id="tabCondition" class="c_table_list" width="100%" style="display:none">
			<tr align="center">
				<td>
					<input type="text" id="conValue" />
				</td>
			</tr>
		</table>
		<table id="tabFunction" class="c_table_list" width="100%" style="display:none" border=1>
			<tr align="center">
				<td>
						发文
						<input type="checkbox" onclick="disable(this);" name="functions"
							value="0" />
					</td>
					<td>
						上传附件
						<input type="checkbox" disabled="true" name="doType" value="0" />
						修改
						<input type="checkbox" disabled="true" name="doType" value="1" />
					</td>
				</tr>
				<tr align="center">
					<td>
						会签
						<input type="checkbox" onclick="disable(this);" name="functions"
							value="1" />
					</td>
					<td>
						保存
						<input type="checkbox" disabled="true" name="condition" value="1" />
					</td>
				</tr>
				<tr align="center">
					<td>
						公共
						<input type="checkbox" onclick="disable(this);" name="functions"
							value="2" />
					</td>
					<td>
						模板下载
						<input type="checkbox" name="back" disabled="true" value="1" />
					</td>
			</tr>
		</table>
		<table id="tabForm" class="c_table_list" width="100%" style="display:none" >
			<tr>
				<td align=center>
					<div id="showFormFiled" />
				</td>
			</tr>
		</table>
		<div style=" float:left;width:100%;height:50px;margin-top:20px">
   		     	 <input type="button" onclick="getSelect();" value="确定" />
   		</div>
	</div>
  </body>
</html>
