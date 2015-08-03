<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<html>
  <head>
    <title>节点配置</title>
  </head>
  <script>		
  	    var obj = window.dialogArguments;
		var startNode="";
		var workflow="workflow";
        var roletype="roletype";
        var taskallocation="taskAllocation";
        var con="conditionType";
        var backNode="backNode";
        var autoNode="autoNode";
        var formFiled="";
        var nodeID=obj.ID;//节点ID
        var nodeType="";
        var formId;
        var roleId;
        var nodeName=obj.Text;//节点名称
        var stepType=obj.ProcType;//节点类型
        var formConfig="";
        var joinConfig="";
        var functionConfig="";
        var roleConfig="";
        var formFuncConfig="";//表单函数配置参数
        var subConfig="";//子流程配置
        var useSub="0";//使用子流程标记(0、不使用1、使用)
  		//设置表单ID
  		if(obj._FLOW.formId!=null){
  		  formId=obj._FLOW.formId;
  		}
  		if(stepType=="BeginProc"){
  			nodeType="START_NODE";//首节点
  		}
  		//显示表单设置
  		function showForm(){
			    //判断模板id
			    if(formId!="undefined"){
			        $(function(){
				        $.ajax({type:"POST", url:"${ctx}/oswf/graphicdesigner/default.do?method=showFormConfig", data:"formId="+formId,dataType:"json",  success:function (msg) {
							formFiled=msg.modelList;
							var connectStr="";
							if(formFiled.length!=0){
								connectStr=connectStr+"<table class=c_table_list align=center width=100% border=1>";
								for(var i=0;i<formFiled.length;i++){
									var id=formFiled[i][0];
		            	  			var value=formFiled[i][1];
		            	  			var remark=formFiled[i][2];
									connectStr=connectStr+"<tr  >";
									connectStr=connectStr+"<td align=center>"+remark;
								    connectStr=connectStr+"<input type=checkbox onclick='disable(this);' checked name="+value+" id="+id+" value="+value+" /> ";
								    connectStr=connectStr+"</td>";
								    connectStr=connectStr+"<td align=left>";       
								    connectStr=connectStr+"<input type=radio checked  name=roles"+value+" value=0 />可编辑 ";
								    connectStr=connectStr+"<input type=radio  name=roles"+value+" value=1 />可见 ";
								    connectStr=connectStr+"<input type=radio   name=roles"+value+" value=2 />不可见 ";
								    connectStr=connectStr+"<input type=checkbox   name=check"+value+" id=check"+id+" value=0 />非空 ";
								    connectStr=connectStr+"</td>";
									connectStr=connectStr+"</tr>";	
								}
								connectStr=connectStr+"</table>";
							}else{
								connectStr="<font color=red>没有可供选择的表单列</font>";
							}
							document.getElementById("showFormFiled").innerHTML=connectStr;
							
							var arry=obj._FLOW.formConfigs;
					        for(var i=0;i<arry.length;i++){
					            var xml=arry[i];
					            if(xml==""){
					              continue;
					            }
					          var  doc=new ActiveXObject("Microsoft.XMLDOM"); 
						      doc.async="false"; 
							  doc.loadXML(xml);
							  var stepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
							  //判断步骤
							  if(stepId==nodeID){
								  var tdIds=doc.getElementsByTagName("step/tdId");
								  try{
									  for(var t=0;t<tdIds.length;t++){
									     var tdId=tdIds[t];
									     var tdIdvalue=tdId.getAttribute("value");
									     var role=tdId.childNodes[0];
									     var roleValue=role.getAttribute("value");
									     var checkValue=role.getAttribute("check");
									     var tdObj=document.getElementsByName(tdIdvalue);
									     tdObj[0].checked=true;
									     var cell=tdObj[0].parentNode.parentNode.childNodes[1];
									     var c  = cell.childNodes;
									     for(var n=0;n<c.length;n++){
									        var p = c[n];
									        if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
									          p.disabled=false;
									          if(p.type=='checkbox'){
									          	p.checked=false;
									          	if(p.value==checkValue){
									          		p.checked=true;
									          	}
									          }else{
										          if(p.value==roleValue){
										           p.checked=true;
										          }
									          }
									        }
										 }
									}
								}catch(e){
								
								}
								break;
								}	
					       	 }
							 
						}});
					});
				}else{
				    alert("请先设置模板");
					return;
				}
  		}
  		
  		//显示功能
  		function showFunctions(){
  			 $(function(){
		        $.ajax({type:"POST", url:"${ctx}/oswf/design/default.do?method=showFunctionConfig", data:"", dataType:"json", success:function (msg) {
		        	var funcFirst=msg.funcfirst;
		        	var funcSecond=msg.funcsecond;
					var connectStr="";
					if(funcFirst.length!=0){
						connectStr=connectStr+"<table class=c_table_list align=center width=100% border=1>";
						for(var i=0;i<funcFirst.length;i++){
							var id=funcFirst[i].id;//id
            	  			var name=funcFirst[i].func_name;//name
            	  			var parent=funcFirst[i].func_parent;//parent
            	  			var ischeck=funcFirst[i].ischeck;//ischeck
            	  			connectStr=connectStr+"<tr  >";
							connectStr=connectStr+"<td align=center>"+name;
						    connectStr=connectStr+"<input type=checkbox onclick='disable1(this);' name=functions value="+id+" /> ";
						    connectStr=connectStr+"</td>";
						    connectStr=connectStr+"<td align=left>";
						    for(var j=0;j<funcSecond.length;j++){
						    	if(id==funcSecond[j].func_parent){
						    		if(funcSecond[j].ischeck=="1"){
						    			connectStr=connectStr+"<input type=radio   name=function"+i+"  value="+funcSecond[j].id+" disabled />"+funcSecond[j].func_name;
						    		}else{
						    			connectStr=connectStr+"<input type=checkbox  name=function"+funcSecond[j].id+" value="+funcSecond[j].id+" disabled />"+funcSecond[j].func_name;
						    		}
						    	}
						    }     
						    connectStr=connectStr+"</td>";
							connectStr=connectStr+"</tr>";
						}
						connectStr=connectStr+"</table>";
					}else{
						connectStr="<font color=red>没有可供选择的功能</font>";
					}
					document.getElementById("showFunctions").innerHTML=connectStr;
					var functionConfigs=obj._FLOW.functionConfigs;
				     for(var i=0;i<functionConfigs.length;i++){
					    xmlDoc1=new ActiveXObject("Microsoft.XMLDOM");
			 		    xmlDoc1.loadXML(functionConfigs[i]);
					    var nId=$(xmlDoc1).find("workflow").attr("nodeId");
					    if(nId==nodeID){
						    $("#tabFunction").find("input").each(function(){
						     		var name=$(this).attr("name");
						     		var value=$(this).attr("value");
						     		var s=this;
						     		 s.disabled=false;
						     		//类别
						     		if(name=='functions'){
							     		$(xmlDoc1).find("functions").each(function (){
									        if(value==$(this).attr("value")){
									           s.checked="checked";
									           
									        }
									    });
								    }
						     		if(name!='functions'){
							     		$(xmlDoc1).find("function").each(function (){
									        if(value==$(this).attr("value")){
									          s.checked="checked";
									        }
									    });
								    }
						    });
					 	}
					}
		        }});
			});
  		}
  		
  		//显示流程类型列表
  		function showWktypeList(){
	  		sys_ajaxPostDirect("/oswf/manage/default.do?method=showWktypeList","",function(json){
				bind(json);
				showSub();
			});
  		}
  		//自动加载子流程配置
  		function showSub(){
  			var subConfigs=obj._FLOW.subConfigs;
		    if(subConfigs!=""){
			    for(var i=0;i<subConfigs.length;i++){
			        if(subConfigs[i]!=undefined && subConfigs[i]!=""){
					    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			 		    xmlDoc.loadXML(subConfigs[i]);
					    var nId=$(xmlDoc).find("workflow").attr("nodeId");
					    if(nId!=null&&nId==nodeID){ 
					       var tb=$("#tab_Sub");
					       $(tb).find("input").each(function(){
					       		//节点设置为子流程
					        	if(this.name=='subbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > subbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}
					        	//引用父流程表单
					        	if(this.name=='formbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > formbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程正文
					        	if(this.name=='zwbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > zwbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程正文
					        	if(this.name=='fjbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > fjbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程留言
					        	if(this.name=='lybz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > lybz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程留言
					        	if(this.name=='lcjsbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > lcjsbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					       });
					       //流程类型
			       			var value=$(xmlDoc).find("workflow > docid").attr("value");
			       			$("#doc_id").val(value);
					    }
					}
				}
			}
  		}
  		
  		//确定
  		function getSelect(){
				//授权
				var option;
				var doc = new ActiveXObject('MSXML2.DOMDocument');
		      	//root
			  	var workflowNode = doc.createElement(workflow);
			  	workflowNode.setAttribute("nodeId",nodeID);
			  	workflowNode.setAttribute("nodeName",nodeName);
			  	workflowNode.setAttribute("nodeType",nodeType);
			   	doc.documentElement = workflowNode;
			   	var gwFlag=0;
			   	if(stepType=="NormalProc" || stepType=="BeginProc"){
			   		if(stepType=="NormalProc"){
				   	var gwrole=document.getElementsByName("gwRole");
				   	for(i=0;i<gwrole.length;i++){
	    				if(gwrole[i].checked==true){
	    					gwFlag=gwrole[i].value;
	    					break;
	    				}
	    			}
				   	if(gwFlag!=0){
				   			var roletypeNode = doc.createElement(roletype);
				   			roletypeNode.setAttribute("type","2");
				    		roletypeNode.setAttribute("value","");
			    			roletypeNode.setAttribute("gwrole",gwFlag);
		   		   			workflowNode.appendChild(roletypeNode);
				   	}else{
					   	var grant=window.grantFrame.confirmSelect();
				   		if(grant==undefined || grant==""){
				   			return;
				   		}else{
				   			//按照岗位-2,按照人员-1,按照部门-0,按照角色-3
				   			grant=grant.substring(0,grant.length-1);
					   		var roleType=grant.substring(0,2);
					   		if(roleType=='gw'){
					   			option='2';
					   		}else if(roleType=='yh'){
					   			option='1';
					   		}else if(roleType=='bm'){
					   			option='0';
					   		}else if(roleType=='js'){
					   			option='3';
					   		}
					   		var grantList=grant.split(",");
					   		for(i=0;i<grantList.length;i++){
					   			var roletypeNode = doc.createElement(roletype);
					   			roletypeNode.setAttribute("type",option);
					    		roletypeNode.setAttribute("value",grantList[i].substring(2));
			   		   			workflowNode.appendChild(roletypeNode);
					   		}
				   		}
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
			           		break;
			          }
			        }
		      
			       conNode.setAttribute("value",option.value);
			       if(option.value=="0"){
			       //如果是多人审核，读取人员个数
			        var amount=document.getElementById("amount");
			         conNode.setAttribute("amount",amount.value);
			       }
			       if(option.value=="2"){
			       	 var bxs=document.getElementsByName("bx");
			       	 for(var i=0;i<bxs.length;i++){
			       	 	if(bxs[i].checked==true){
			       	 		conNode.setAttribute("amount",bxs[i].value);
			       	 		break;
			       	 	}
			       	 }
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
			       var remindNode= doc.createElement("remind");
		   		   var remidRadio=document.getElementsByName("remind");
		   		     for(var i=0;i<remidRadio.length;i++){
			          if(remidRadio[i].checked==true){
			          	option=remidRadio[i];
			          }
			        }
		   		   remindNode.setAttribute("value",option.value);
		   		   workflowNode.appendChild(remindNode);
	   		   
		   		   //添加催办
		   		   var isCuibNode=doc.createElement("isCuib");
		   		   var isCuibRadio=document.getElementsByName("cuib");
		   		     for(var i=0;i<isCuibRadio.length;i++){
			          if(isCuibRadio[i].checked==true){
			          	option=isCuibRadio[i];
			          }
			        }
		   		   isCuibNode.setAttribute("value",option.value);
		   		   if(option.value=="0"){
		   		     var contime=document.getElementById("contime");
		   		     isCuibNode.setAttribute("contime",contime.value);
		   		   }
		   		   workflowNode.appendChild(isCuibNode);
	   		   }
	   		   roleConfig=doc.xml;
	   		   //alert(roleConfig);
	   		    //设置xiork
	   		   
			 //条件
			 if(stepType=="JoinProc"){
			 	var convalue=document.getElementById("conValue");
			 	if(convalue.value!=""){
	         		 var con1="jionconfig";
		      		 var doc1 = new ActiveXObject('MSXML2.DOMDocument');
		      		 //root
				  	 var workflowNode1 = doc1.createElement(workflow);
				  	 workflowNode1.setAttribute("nodeId",nodeID);
				  	 workflowNode1.setAttribute("nodeName",nodeName);
			  		 workflowNode1.setAttribute("nodeType",nodeType);
				   	 doc1.documentElement = workflowNode1;
				     //条件公式		 
		   		     var conNode= doc1.createElement(con1);
		   		     
		   		     conNode.setAttribute("value",convalue.value);
		   		     workflowNode1.appendChild(conNode);
		   		     joinConfig=doc1.xml;
		   		     //alert(joinConfig);	   		     
			   	}
			  }
			 //功能
          		var functions="functions";
          		var functionNode="function";
          		var doc2 = new ActiveXObject('MSXML2.DOMDocument');
				  var workflowNode2 = doc2.createElement(workflow);
				  workflowNode2.setAttribute("nodeId",nodeID);
				  doc2.documentElement = workflowNode2;
			      var type=document.getElementsByName("functions");
			      
			      for(var i=0;i<type.length;i++){
			        if(type[i].checked){
			   		   var functionsNode= doc2.createElement(functions);
			   		   functionsNode.setAttribute("value",type[i].value);
			   		   workflowNode2.appendChild(functionsNode);
			   		   //子节点
			   		   var cell=type[i].parentNode.parentNode.childNodes[1];
			   		   var cContent=cell.childNodes;
			   		   for(var j=0;j<cContent.length;j++){
			   		     var p = cContent[j];
			      		  if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
			      		      if(p.checked==true){
				      		      var fNode = doc2.createElement(functionNode);
					  			  fNode.setAttribute("value",p.value);
					  			  functionsNode.appendChild(fNode);
					  		   }
			      		  }
			   		  }
			        }
			       }
			       
			        functionConfig=doc2.xml;
			       //alert(functionConfig);
			//表单设置
				 var stepNode="step";
		         var formNode="form";
		         var tdNode="tdId";
		         var checkNode="check";
		         
		         doc3 = new ActiveXObject('MSXML2.DOMDocument');
		         var setpElement = doc3.createElement(stepNode);
				 setpElement.setAttribute("stepId",nodeID);
				 doc3.documentElement = setpElement;
				 
				 var formIdElement=doc3.createElement(formNode);
				 formIdElement.setAttribute("formId",formId);
				 setpElement.appendChild(formIdElement);
				 if(formFiled!=undefined){
					 for(var i=0;i<formFiled.length;i++){
						var fid=formFiled[i][1]
					 	var table=document.getElementById(fid);
			          	var tdElement=doc3.createElement(tdNode);
					  	tdElement.setAttribute("value",table.value);
					  	setpElement.appendChild(tdElement);
			          	initElement(table,tdElement);
			         }
			         formConfig=doc3.xml;
				 }
				 //alert(formConfig);
				 
				 //表单执行函数设置
				 if(stepType=="NormalProc"){
				 	var funcvalue=document.getElementById("funcname");
				 	if(funcvalue.value!=""){
			      		 var doc6 = new ActiveXObject('MSXML2.DOMDocument');
			      		 //root
					  	 var workflowNode1 = doc6.createElement(workflow);
					  	 workflowNode1.setAttribute("nodeId",nodeID);
					   	 doc6.documentElement = workflowNode1;
					     //表单执行函数		 
			   		     var conNode= doc6.createElement("func");
			   		     
			   		     conNode.setAttribute("value",funcvalue.value);
			   		     workflowNode1.appendChild(conNode);
			   		     formFuncConfig=doc6.xml;
			   		     //alert(formFuncConfig);		     
				   	}
				 }
				 
				 //子流程配置
				 if(useSub=="1"){
				 	if(stepType=="NormalProc"){
				 		var doc7 = new ActiveXObject('MSXML2.DOMDocument');
				 		 var workflowNode7 = doc7.createElement(workflow);
				 		 workflowNode7.setAttribute("nodeId",nodeID);
					   	 doc7.documentElement = workflowNode7;
					   	 //添加子项
					   	 //节点设置为子流程
					   	 var subbzvalue=document.getElementsByName("subbz_dm");
					   	 var subbzNode= doc7.createElement("subbz");
					   	 for(var i=0;i<subbzvalue.length;i++){
						  	if(subbzvalue[i].checked==true){
						  		option=subbzvalue[i];
						  	}
						 }
						 //流程类型
					   	 var docidvalue=$("#doc_id").val();  
					   	 if(option.value=="7811"){
					   	 	if(docidvalue==""){
					   	 		alert('该节点已设置为子流程，流程类型不能为空！');
					   	 		return;
					   	 	}
					   	 }	
			   		     subbzNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(subbzNode);
					   	 //流程类型
					   	 //var docidvalue=$("#doc_id").val(); 
					   	 if(docidvalue==null){
					   	 	docidvalue="";
					   	 }
					   	 var docidNode= doc7.createElement("docid");
			   		     docidNode.setAttribute("value",docidvalue);
			   		     workflowNode7.appendChild(docidNode);
			   		     //引用父流程表单
			   		     var formbzdmvalue=document.getElementsByName("formbz_dm"); 
					   	 var formbzdmNode= doc7.createElement("formbz");
					   	 for(var i=0;i<formbzdmvalue.length;i++){
						  	if(formbzdmvalue[i].checked==true){
						  		option=formbzdmvalue[i];
						  	}
						 }
			   		     formbzdmNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(formbzdmNode);
			   		     //引用父流程正文
			   		     var zwbzdmvalue=document.getElementsByName("zwbz_dm"); 
					   	 var zwbzdmNode= doc7.createElement("zwbz");
					   	 for(var i=0;i<zwbzdmvalue.length;i++){
						  	if(zwbzdmvalue[i].checked==true){
						  		option=zwbzdmvalue[i];
						  	}
						 }
			   		     zwbzdmNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(zwbzdmNode);
			   		     //引用父流程附件
			   		     var fjbzdmvalue=document.getElementsByName("fjbz_dm"); 
					   	 var fjbzdmNode= doc7.createElement("fjbz");
					   	 for(var i=0;i<fjbzdmvalue.length;i++){
						  	if(fjbzdmvalue[i].checked==true){
						  		option=fjbzdmvalue[i];
						  	}
						 }
			   		     fjbzdmNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(fjbzdmNode);
			   		     //引用父流程留言
			   		     var lybzdmvalue=document.getElementsByName("lybz_dm"); 
					   	 var lybzdmNode= doc7.createElement("lybz");
					   	 for(var i=0;i<lybzdmvalue.length;i++){
						  	if(lybzdmvalue[i].checked==true){
						  		option=lybzdmvalue[i];
						  	}
						 }
			   		     lybzdmNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(lybzdmNode);	
			   		     //流程结束标识
			   		     var lcjsbzdmvalue=document.getElementsByName("lcjsbz_dm"); 
					   	 var lcjsbzdmNode= doc7.createElement("lcjsbz");
					   	 for(var i=0;i<lcjsbzdmvalue.length;i++){
						  	if(lcjsbzdmvalue[i].checked==true){
						  		option=lcjsbzdmvalue[i];
						  	}
						 }
			   		     lcjsbzdmNode.setAttribute("value",option.value);
			   		     workflowNode7.appendChild(lcjsbzdmNode);
			   		     subConfig=doc7.xml;				   		     		   		     
				 	}
				 }
				 var ob=new Object();
				 //聚合节点只设置聚合信息，其他信息舍去
				 if(stepType=="JoinProc"){
				    ob.joinConfigs=joinConfig;
				    //ob.formId=formId;
				 }else{
					 ob.formConfigs=formConfig;
					 ob.functionConfigs=functionConfig;
					 ob.joinConfigs=joinConfig;
					 ob.roleConfigs=roleConfig;
					 ob.formFuncConfigs=formFuncConfig;
					 ob.subConfigs=subConfig;
					 ob.formId=formId;
				 }
				 //alert(functionConfig);
				 window.returnValue=ob;
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
	      if(rt==1) return;
	      rt=1;
	      var condition=document.getElementById("con");
	      condition.innerHTML=condition.innerHTML+"<input  type='text' id='amount' size='2'/> 人";
	   }
	   function delInput(){
	   	  if(rt==0) return;
	      rt=0;
	      var condition=document.getElementById("con");
	      condition.innerHTML="多人通过审核";
	   }
	   
	   //处理 催办，自动隐藏和现实输入框 
	   var cuib=0;
	   function addcuib(){
	      if(cuib!=0){
	        return;
	      }
	      cuib=1;
	      var condition=document.getElementById("cuibId");
	      condition.innerHTML=condition.innerHTML+"  时间：<input  type='text' id='contime' size='1'/> 小时后";
	   }
	   function delcuib(){
	      cuib=0;
	      var condition=document.getElementById("cuibId");
	      condition.innerHTML="催办";
	   }
	   
	   var bt=0;
	   //自动执行节点
	   function addRow(obj){
	   		//判断表单是否选择
	   		if(formId==undefined){
	   			document.all("autoType")[0].checked=true;
	   			alert("请先设置模板");
				return;
	   		}
			var connectStr="";
			if(formFiled.length!=0){
				for(var i=0;i<formFiled.length;i++){
					var id=formFiled[i][0];
        	  		var value=formFiled[i][1];
        	  		var remark=formFiled[i][2];
					connectStr=connectStr+"<option value='"+value+"'>"+remark+"</option>";
				}
			}else{
				alert('选择的模板没有可用列！');
				return;
			}
   			if(bt!=0){
	        	return;
	        }
	        bt=1;
	       
	        var superObj=obj.parentNode.parentNode.childNodes[3];
		    var opstr="<select size='1' id='configselect'	style='width: 150' onChange=''>";
		    opstr+=connectStr;
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
  		//表单设置
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
		//功能设置
		function disable1(obj){
		   	 var cell = obj.parentNode.parentNode.childNodes[1];
		   	 var c  = cell.childNodes;
		     for(var i=0;i<c.length;i++){
		        var p = c[i];
		        if(p.tagName&&p.tagName.toUpperCase()==='INPUT'){
		        	if(p.type=='radio'){
			        	if(i==0){
			        		p.checked=obj.checked;
			        	}
		        	}else{
		        		p.checked=obj.checked;
		        	}
		        	//p.checked = obj.checked;
		        	if(obj.checked)
		        		p.disabled=false;
		        	else
		        		p.disabled=true;
		        	}
		     }
		}
  		
		function s_chg(td_now){
		  //授权
		  if(td_now == "s_td_2"){
		  	$(".c_current_tab").removeClass("c_current_tab");
		 	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		    document.all.tabFormFunction.style.display="none";
		    document.all.tabSub.style.display="none";
		    if(useSub!="1"){
		    	$("#s_td_7").hide();
		    }
			if(stepType=="BeginProc"){
				document.getElementById("listDiv").style.display="none";
            }else{
             	document.all.showGwConfig.style.display="";
            }
		    var formConfigs=obj._FLOW.roleConfigs;
		    
		    if(formConfigs!=""){
		    for(var i=0;i<formConfigs.length;i++){
			    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	 		    xmlDoc.loadXML(formConfigs[i]);
			    var nId=$(xmlDoc).find("workflow").attr("nodeId");
			     
			    if(nId==nodeID){
				    var tb=$("#tab");
			        $(tb).find("input").each(function(){
			        	//手工 代办
			        	if(this.name=='doType'){
			        	   if(this.value==$(xmlDoc).find("workflow > taskAllocation").attr("value")){
			        	   
			        	       this.checked="true";
			        	   }
			        	}
			        	//回退
			        	if(this.name=='back'){
			        	   if(this.value==$(xmlDoc).find("workflow > backNode").attr("value")){
			        	       this.checked="true";
			        	   }
			        	}
			        	//审核
			        	 if(this.name=='condition'){
			        	     var value=$(xmlDoc).find("workflow > conditionType").attr("value");
			        	    
			        	   if(this.value==value){
			        	       this.checked="true";
			        	       if(value=="0"){
			        	           addInput();
			        	           var amount=$(xmlDoc).find("workflow > conditionType").attr("amount");
			        	           $("#amount").val(amount);
			        	       }
			        	       if(value="2"){
			        	           var amount=$(xmlDoc).find("workflow > conditionType").attr("amount");
			        	           var bxs=document.getElementsByName("bx");
							       for(var i=0;i<bxs.length;i++){
							          if(bxs[i].value==amount){
							          	bxs[i].checked="true";
							          }
							       }
			        	       }
			        	   }
			        	}
			        	//remind
			        	if(this.name=='remind'){
			        	     var value=$(xmlDoc).find("workflow > remind").attr("value");
			        	    
			        	   if(this.value==value){
			        	       this.checked="true";
			        	   }
			        	}
			        	//催办
			        	if(this.name=='cuib'){
			        	   if(this.value==$(xmlDoc).find("workflow > isCuib").attr("value")){
			        	       this.checked="true";
			        	       if(this.value=="0"){
			        	           addcuib();
			        	           var contime=$(xmlDoc).find("workflow > isCuib").attr("contime");
			        	           $("#contime").val(contime);
			        	       }
			        	   }
			        	}
			        	
			        	//手工执行节点 
			        	if(this.name=='autoType'){
			        	     var value=$(xmlDoc).find("workflow > autoNode").attr("value");
			        	    
			        	   if(this.value==value){
			        	       this.checked="true";
			        	       if(value=="1"){
			        	          var  autoInput=document.getElementById("autoName");
			        	           addRow(autoInput);
			        	           var formula=$(xmlDoc).find("workflow > autoNode").attr("formula");
			        	           var fo=formula.split(",");
			        	           //设置
			        	           $("#configselect").find("option").each(function(){
			        	               if(fo[0]==$(this).val()){
			        	                 $(this).checked="true";
			        	               }
			        	           });
			        	           $("#conselect").find("option").each(function(){
			        	               if(fo[1]==$(this).val()){
			        	                 $(this).checked="true";
			        	               }
			        	           });
			        	           $("#conValue").val(fo[2]);
			        	       }
			        	   }
			        	}
			        });
			        //权限
			           var roles=xmlDoc.getElementsByTagName("workflow/roletype");//权限
					   var roleTypeCode="";
					   var roleValues="";
					   //得到所有选中项目
					    for(var j=0;j<roles.length;j++){
					       var role=roles[j];
					       var roleType=role.getAttribute("type");//类型(0部门1个人2岗位)
					   	   var roleValue=role.getAttribute("value");
					   	   var gwrole=role.getAttribute("gwrole");//岗位类型（1、发启者　2、执行者）
					   	   if(gwrole!="0" && gwrole!=null){
					   	   		if(gwrole=="1"){
					   	   			$("input[@type=radio][name=gwRole]").get(1).checked = true; 
					   	   		}else if(gwrole=="2"){
					   	   			$("input[@type=radio][name=gwRole]").get(3).checked = true; 
					   	   		}else if(gwrole=="4"){
					   	   			$("input[@type=radio][name=gwRole]").get(2).checked = true; 
					   	   		}else{
					   	   			$("input[@type=radio][name=gwRole]").get(0).checked = true; 
					   	   		}
					   	   }else{
						   	   if(roleType=="0"){
						   	   		roleTypeCode="bm";
						   	   }else if(roleType=="1"){
						   	   		roleTypeCode="yh";
						   	   }else if(roleType=="2"){
						   	   		roleTypeCode="gw";
						   	   }else if(roleType=="3"){
						   	   		roleTypeCode="js";
						   	   }
						       roleValues=roleValues+roleTypeCode+roleValue+",";
						       
						  }
					    }
					    var url="${ctx}/org/default.do?method=select_pub&show=yh,bm,gw,js&issingle=1&wf_au=1&initItems="+roleValues;
					    //document.getElementById("grantFrame").location=url;
					    $("#grantFrame").attr("src",url);
			    }
		    }
		  }
		  }
		  //条件
		  if(td_now== "s_td_3"){
		  	if(stepType!="JoinProc"){
				alert("请选择汇聚节点设置汇聚条件！");
				return;
			}
			$(".c_current_tab").removeClass("c_current_tab");
		  	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		    document.all.tabFormFunction.style.display="none";
		    document.all.tabSub.style.display="none";
		    var joinConfigs=obj._FLOW.joinConfigs;
		    for(var i=0;i<joinConfigs.length;i++){
		        if(joinConfigs[i]!=undefined){
				    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		 		    xmlDoc.loadXML(joinConfigs[i]);
				    var nId=$(xmlDoc).find("workflow").attr("nodeId");
				    if(nId!=null&&nId==nodeID){ 
				       var value=$(xmlDoc).find("jionconfig").attr("value");
	                    $("#conValue").val(value);			       
				    }
				}
			}
		  }
		  //功能按钮
		  if(td_now== "s_td_4"){
		  	$(".c_current_tab").removeClass("c_current_tab");
		  	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="";
		    document.all.tabForm.style.display="none";
		    document.all.tabFormFunction.style.display="none";
		    document.all.tabSub.style.display="none";
		  }
		  //表单设置
		  if(td_now== "s_td_5"){
		    if(formId==null || formId=="" || formId=="unfined"){
		  		alert("请先设置模板");
				return;
		  	}
		  	$(".c_current_tab").removeClass("c_current_tab");
		  	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="";
		    document.all.tabFormFunction.style.display="none";
		    document.all.tabSub.style.display="none";
		  }
		  //执行函数
		  if(td_now== "s_td_6"){
		  	$(".c_current_tab").removeClass("c_current_tab");
		  	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		    document.all.tabFormFunction.style.display="";
		    document.all.tabSub.style.display="none";
		    var formFuncConfigs=obj._FLOW.formFuncConfigs;
		    if(formFuncConfigs!=""){
			    for(var i=0;i<formFuncConfigs.length;i++){
			        if(formFuncConfigs[i]!=undefined && formFuncConfigs[i]!=""){
					    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			 		    xmlDoc.loadXML(formFuncConfigs[i]);
					    var nId=$(xmlDoc).find("workflow").attr("nodeId");
					    if(nId!=null&&nId==nodeID){ 
					       var value=$(xmlDoc).find("func").attr("value");
		                    $("#funcname").val(value);	
		                    break;		       
					    }
					}
				}
			}
		  }
		  //子流程配置
		  if(td_now== "s_td_7"){
		  	$(".c_current_tab").removeClass("c_current_tab");
		  	$("#"+td_now).parent().addClass("c_current_tab");
		    document.all.tabConfig.style.display="none";
		    document.all.tabCondition.style.display="none";
		    document.all.tabFunction.style.display="none";
		    document.all.tabForm.style.display="none";
		    document.all.tabFormFunction.style.display="none";
		    document.all.tabSub.style.display="";
		    var subConfigs=obj._FLOW.subConfigs;
		    if(subConfigs!=""){
			    for(var i=0;i<subConfigs.length;i++){
			        if(subConfigs[i]!=undefined && subConfigs[i]!=""){
					    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			 		    xmlDoc.loadXML(subConfigs[i]);
					    var nId=$(xmlDoc).find("workflow").attr("nodeId");
					    if(nId!=null&&nId==nodeID){ 
					       var tb=$("#tab_Sub");
					       $(tb).find("input").each(function(){
					       		//节点设置为子流程
					        	if(this.name=='subbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > subbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}
					        	//引用父流程表单
					        	if(this.name=='formbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > formbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程正文
					        	if(this.name=='zwbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > zwbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程正文
					        	if(this.name=='fjbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > fjbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程留言
					        	if(this.name=='lybz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > lybz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					        	//引用父流程留言
					        	if(this.name=='lcjsbz_dm'){
					        	   if(this.value==$(xmlDoc).find("workflow > lcjsbz").attr("value")){
					        	       this.checked="true";
					        	   }
					        	}					        	
					       });
					       //流程类型
			       			var value=$(xmlDoc).find("workflow > docid").attr("value");
			       			$("#doc_id").val(value);
					    }
					}
				}
			}
		  }
		}
  </script>
  <body>
	  <div id="c_tab">
		  <ul>
		    <li class="c_current_tab" ><a id="s_td_2" href="#" onClick="s_chg('s_td_2');"><span>授权</span></a></li>
		    <li><a id="s_td_3" onClick="s_chg('s_td_3');" href="#"><span>条件</span></a></li>
		    <li><a id="s_td_4" onClick="s_chg('s_td_4');" href="#"><span>功能按钮</span></a></li>
		    <li><a id="s_td_5" onClick="s_chg('s_td_5');" href="#"><span>表单设置</span></a></li>
		    <li><a id="s_td_6" onClick="s_chg('s_td_6');" href="#"><span>数据同步</span></a></li>
		    <li><a id="s_td_7" onClick="s_chg('s_td_7');" href="#"><span>子流程配置</span></a></li>
		  </ul>
	 </div>
	<div align="center">
		<table id="tabConfig" width="100%" >
			<tr align="center">
				<td>
				<div style="float: left; width: 100%; height: 100%;">
				<div id="listDiv" style="float: left; width: 100%; height: 50px;">
					<table border="1" id="tab" class="c_table_list" width="100%">
						<tr>
							<td width="50px">
								<input type="radio" checked name="doType" value="0" />
							</td>
							<td >
								手工指定待办人
							</td>
							<td width="50px">
								<input type="radio"  name="doType" value="1" />
							</td>
							<td colspan="3">
								自动
							</td>
						</tr>
						<tr>
							<td  width="50px">
								<input type="radio" onClick="addInput();" name="condition"
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
							<td colspan="3">
								不回退
							</td>
							
						</tr>
						<tr>
							<td width="50px">
								<input type="radio" name="remind" 
									value="0" />
							</td>
							<td id="remindId">
								提醒
							</td>
							<td width="50px">
								<input type="radio" name="remind"  checked
									value="1" />
							</td>
							<td colspan="3">
								不提醒
							</td>
							
						</tr>
						<tr>
							<td width="50px">
								<input type="radio" name="cuib"  onclick="addcuib();"
									value="0" />
							</td>
							<td id="cuibId">
								催办
							</td>
							<td width="50px">
								<input type="radio" name="cuib"  checked onClick="delcuib();"
									value="1" />
							</td>
							<td colspan="3">
								不催办
							</td>
							
						</tr>
						<tr>
							<td  width="50px">
								<input type="radio" name="autoType" checked onClick="delRow(this);"
									value="0" />
							</td>
							<td>
								手工执行节点
							</td>
							<td width="50px" >
								<input type="radio" id="autoName" onClick="addRow(this);" name="autoType"
									value="1" />
							</td>
							<td colspan="3">
								自动执行节点
							</td>
							
						</tr>

					</table>
				</div>
				<hr>
				<table id="showGwConfig" style="display:none" class="c_table_list">
					<tr>
						<td><input type="radio" name="gwRole" value="3" />发起者</td>
						<td><input type="radio" name="gwRole" value="1" />发起者上级岗位</td>
						<td><input type="radio" name="gwRole" value="4" />上一步执行者下级岗位</td>
						<td><input type="radio" name="gwRole" value="2" />上一步执行者上级岗位</td>
						<td><input type="radio" name="gwRole" value="0" />取消选择</td>
					</tr>
				</table>
				<div id="buttomDiv" style="float: left; width: 100%; height: 300px;">
						<iframe id="grantFrame" width="100%" height="400" src="${ctx}/org/default.do?method=select_pub&show=yh,bm,gw,js&issingle=1&wf_au=1"></iframe>
				</div>
			</div>
				</td>
			</tr>
		</table>
		<table id="tabCondition" width="100%" style="display:none">
			<tr align="center">
				<td>
					<input type="text" id="conValue" />
				</td>
			</tr>
		</table>
		<table id="tabFunction" class="c_table_list" width="100%" style="display:none" border=1>
			<tr align="center">
				<td>
						<div id="showFunctions" />
				</td>
			</tr>
		</table>
		<table id="tabForm" width="100%" style="display:none" >
			<tr>
				<td align=center>
					<div id="showFormFiled" />
				</td>
			</tr>
		</table>
		<table id="tabFormFunction" width="100%"  style="display:none" >
			<tr align="center">
			 
			</tr>
			<tr align="center">
				<td height="40px">
				<font color=red >说明：主要用于将流程表单里的数据信息添加到同步审批数据列表里相对应的模块</font>
				</td>
			</tr>
		</table>
		<table id="tabSub" width="100%"  style="display:none" >
			<tr align="center">
				<td>
					<table border="1" id="tab_Sub" class="c_table_list" width="100%">
						<tr >
							<td width="180px" align="center">
								节点设置为子流程
							</td>
							<td align="left">
								<input type="radio" name="subbz_dm" value="7811" />是
								<input type="radio" name="subbz_dm" value="7812" checked/>否
							</td>
						</tr>
						<tr >
							<td width="180px" align="center">
								流程类型
							</td>
							<td align="left">
								<select id="doc_id" name="doc_id">
									<option></option>
								</select>
							</td>
						</tr>
						<tr >
							<td  align="center">
								引用父流程表单
							</td>
							<td align="left">
								<input type="radio" name="formbz_dm" value="7801" />引用
								<input type="radio" name="formbz_dm" value="7802" checked/>不引用
							</td>
						</tr>
						<tr >
							<td  align="center">
								引用父流程正文
							</td>
							<td align="left">
								<input type="radio" name="zwbz_dm" value="7803" />引用
								<input type="radio" name="zwbz_dm" value="7804" checked/>不引用
							</td>
						</tr>
						<tr >
							<td  align="center">
								引用父流程附件
							</td>
							<td align="left">
								<input type="radio" name="fjbz_dm" value="7805" />引用
								<input type="radio" name="fjbz_dm" value="7806" checked/>不引用
							</td>
						</tr>
						<tr >
							<td  align="center">
								引用父流程留言
							</td>
							<td align="left">
								<input type="radio" name="lybz_dm" value="7807" />引用
								<input type="radio" name="lybz_dm" value="7808" checked/>不引用
							</td>
						</tr>
						<tr >
							<td  align="center">
								流程结束标识
							</td>
							<td align="left">
								<input type="radio" name="lcjsbz_dm" value="7809" checked />主流程结束实例
								<!--  
								<input type="radio" name="lcjsbz_dm" value="7810" />子流程结束实例
								-->
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div style=" float:left;width:100%;height:50px;margin-top:20px">
   		     	 <input type="button" onClick="getSelect();" value="确定" />
   		</div>
	</div>
  </body>
   <script>
  //添加选中项目
  window.onload=function(){
	        //检查系统是否可以使用子流程
			sys_ajaxPostDirect("/oswf/manage/default.do?method=checkIsUseSub","",function(json){
				useSub=json.result;
			   //加载功能面板
		       showFunctions();
		       //加载表单面板
		       showForm();
		       //显示流程类型列表
		       if(useSub=="1"){
		       	showWktypeList();
		       }
		       //开始加载模板面板
		       if(obj.ProcType=="BeginProc"){
		       		document.getElementById("s_td_2").style.display="none";
		       		document.getElementById("s_td_3").style.display="none";
		       		document.getElementById("s_td_4").style.display="";
				    document.getElementById("s_td_5").style.display="";
				    document.getElementById("s_td_6").style.display="none";
				    document.getElementById("s_td_7").style.display="none";
		       		document.all.tabConfig.style.display="none";
				    document.all.tabCondition.style.display="none";
		       		document.all.tabFunction.style.display="";
				    document.all.tabForm.style.display="none";
				    document.all.tabFormFunction.style.display="none";
				    document.all.tabSub.style.display="none";
		       }
		       if(obj.ProcType=="NormalProc"){
		            s_chg('s_td_2');
		       		if(stepType=="BeginProc"){
						document.getElementById("listDiv").style.display="none";
		            }else{
		             	document.all.showGwConfig.style.display="";
		            }
		            document.getElementById("s_td_3").style.display="none";
		       }
		  		if(obj.ProcType=="JoinProc"){//聚合节点屏蔽其他设置
				    document.all.tabConfig.style.display="none";
				    document.all.tabCondition.style.display="";
				    document.all.tabFunction.style.display="none";
				    document.all.tabForm.style.display="none";
				    document.getElementById("s_td_2").style.display="none";
				    document.getElementById("s_td_4").style.display="none";
				    document.getElementById("s_td_5").style.display="none";
				    document.getElementById("s_td_6").style.display="none";
				    document.getElementById("s_td_7").style.display="none";
				    s_chg('s_td_3');
		        }
			});
	};
  </script>
</html>
