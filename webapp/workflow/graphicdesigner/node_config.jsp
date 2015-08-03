<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<%@ include file="/common/xiorkFlowBasePath.jsp"%>
<%@ page import="java.util.*"%>
<%
	ArrayList list=(ArrayList)request.getAttribute("model");
%>
<html>
	<head>
		<title>节点配置</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css" />
		<script charset="UTF-8" src="${ctx}/js/XiorkFlow/js/XiorkFlowWorkSpace.js" language="javascript"></script>
		<script type="text/javascript" src="${ctx}/js/XiorkFlow/app/share.js"></script>
	</head>
	<script>
         var  roleArray=new Array();
         XiorkFlowWorkSpace.build();
         var workflow="workflow";
         var roletype="roletype";
         var taskallocation="taskAllocation";
         var con="conditionType";
         var backNode="backNode";
         var autoNode="autoNode";
         //父窗口参数
         var obj = window.dialogArguments;
         var nodeId=obj.nodeId;
         //确认
	     function getSelect(){
	      var obj=leftFrame.document.getElementById("configselect");
	      var pageType=document.getElementsByName("roletype");
	      var option;
	      //得到角色分配所有类型ID(按照角色-2,按照人员-1,按照部门-0)
	      for(var i=0;i<pageType.length;i++){
	          if(pageType[i].checked==true){
	          		option=pageType[i];
	          }
	      }
	      var doc = XMLDocument.newDomcument();
	      //root
		  var workflowNode = doc.createElement(workflow);
		  workflowNode.setAttribute("nodeId",nodeId);
		   doc.documentElement = workflowNode;
		   //权限
		   for(var i=0;i<obj.options.length;i++){
		     if(obj.options[i].selected==true){
			   var roletypeNode = doc.createElement(roletype);
			    roletypeNode.setAttribute("type",option.value);
			    roletypeNode.setAttribute("value",obj.options[i].value);
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
	        // end 
	       
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
   		  // end 
   			//alert(doc.xml);
   		    window.returnValue=doc.xml;
   		    window.close();
   		  // return doc.xml;
   		   
	     }
	   var bt=0;
	    function add_Row(the_table){
	        if(bt!=0){
	        	return;
	        }
	        bt=1;
		    var the_row,the_cell;
		    var cur_rows=the_table.rows.length;
		    the_row=cur_rows==null?-1:(cur_rows);
		    var i=the_row;
		    var newrow=the_table.insertRow(i);//得到插入位置
		    newrow.id="addrow";
		    the_cell=newrow.insertCell(0);
		    the_cell.innerHTML="<select size='1' id='configselect'	style='width: 200' onChange=''>"+
		                    "<option value='role1'>	项目经理	</option>"+
		                    "<option value='role2'>部门经理</option>"+
						     "<option value='role3'>财务部门</option>"+
						     "<option value='role4'>人力资源部门</option>	</select>";
		    the_cell=newrow.insertCell(1);
		    the_cell.innerHTML="<select size='1' id='conselect'	style='width: 200' onChange=''> "+
		     				"<option value='0'>></option> "+
		     				"<option value='1'><</option> "+
		     				"<option value='2'>=</option>"+
		     				"<option value='3'>!=</option>"+
		     				"</select>";
		    the_cell=newrow.insertCell(2);
		    the_cell.innerHTML="<input type='text' id='conValue' />";
    }
     function delRow2(){
     if(bt!=1){
        return;
      }
      bt=0;
      var tablecon=document.getElementById("tab");
      tablecon.deleteRow(tablecon.rows.length-1);
   }
   function delRow(obj){
     if(bt!=1){
        return;
      }
      bt=0;
      var parentNode=obj.parentNode.parentNode.childNodes[3];
      parentNode.innerHTML="自动执行节点";
   }
   function add_Row2(obj){
   			if(bt!=0){
	        	return;
	        }
	        bt=1;
	       
	        var superObj=obj.parentNode.parentNode.childNodes[3];
	        alert(superObj);
		    var opstr="<select size='1' id='configselect'	style='width: 150' onChange=''>";
		     <%
              for(Iterator ite=list.iterator();ite.hasNext();){
            	  String[] td=(String[])ite.next();
            	  String id=td[0].toString();
            	  String value=td[2].toString();
           %>
		   opstr+= "<option value='<%=id%>'>	<%=value%></option>";
	      <%
	     	 }
	      %>
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
   //end 
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
   
   //  end  
	</script>
	<body>
		<center>
			<div style="float: left; width: 100%; height: 100%;">
				<div id="listDiv" style="float: left; width: 100%; height: 50px;">
					<table border="1" id="tab">
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
								<input type="radio" id="autoName" onclick="add_Row2(this);" name="autoType"
									value="1" />
							</td>
							<td>
								自动执行节点
							</td>
							
						</tr>

					</table>
				</div>
				<hr>
				<div id="buttomDiv" style="float: left; width: 100%; height: 300px;">

					权限分配：
					<div style="margin-top: 0px; width: 300px; height: 15px;">
						<script language="javascript">
							DisplayRoleTabs("0","${ctx}")
					  	</script>
					</div>
					<hr>
					<div style="margin-top: 0px; width: 300px; height: 250px;">
						<iframe name="leftFrame" id="leftFrame" src="${ctx}/oswf/graphicdesigner/nodeConfig/role.jsp"
							style="width: 100%; height: 100%; border-bottom: #686b72 1px thick; border-top: #ef0911 1px thick; border-left: #686b72 50px thick; border-right: #686b72 50px thick;"
							scrolling="auto" frameborder="0" marginwidth="0" marginheight="0"></iframe>
					</div>
				</div>
			</div>
		</center>
	</body>
	<script>
	 //父窗口参数
         var wrapper = obj.xiorkFlow.getWrapper();
         var model=wrapper.getModel();
         //页面初始化方法
	     window.onload=function(){
	      if(obj.startNode=="yes"){
             document.getElementById("listDiv").style.display="none";
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
					  if(stepId==nodeId){
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
				  if(stepId!=nodeId){
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
				   //得到所有选中项目
				   
				    for(var j=0;j<roles.length;j++){
				       var role=roles[j];
				   	   var roleValue=role.getAttribute("value");
				        roleArray.add(roleValue);
				    }
				    for(var j=0;j<roles.length;j++){
				   		var role=roles[j];
				   		var roleType=role.getAttribute("type");
				   		var radios=document.getElementsByName("roletype");
				   		 for(var m=0;m<radios.length;m++){
				   		 	var radioValue=radios[m].value;
				   		 	if(radioValue==roleType){
				   		 	   radios[m].checked=true;
				   		 	   var src="";
				   		 	   if(radioValue=="0"){
				   		 	   		src="${ctx}/oswf/graphicdesigner/nodeConfig/department.jsp";
				   		 	   }else if(radioValue=="1"){
				   		 	   		src="${ctx}/oswf/graphicdesigner/nodeConfig/user.jsp";
				   		 	   }else if(radioValue=="2"){
				   		 	   		src="${ctx}/oswf/graphicdesigner/nodeConfig/role.jsp";
				   		 	   }else{
				   		 	       src="${ctx}/oswf/graphicdesigner/nodeConfig/role.jsp";
				   		 	   }
				   		 	  document.getElementById("leftFrame").src=src;
				   		 	  
				   		 	  break;
				   		 	}
				   		 }
				   }
				  
				  
         	}
	     };
	     
	</script>
</html>
