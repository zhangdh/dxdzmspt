<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<html>
<head>
<title>Select Properties</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta content="noindex, nofollow" name="robots">
<script src="<%=request.getContextPath()%>/js/fckeditor/editor/dialog/common/fck_dialog_common.js?ver=111" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/fckeditor/editor/dialog/fck_select/fck_select.js"></script>
<script type="text/javascript">

var dialog	= window.parent ;
var oEditor = dialog.InnerDialogLoaded() ;

// Gets the document DOM
var oDOM = oEditor.FCK.EditorDocument ;

var oActiveEl = dialog.Selection.GetSelectedElement() ;

var oListText ;
var oListValue ;

window.onload = function()
{
	oEditor.FCKLanguageManager.TranslatePage(document) ;
	
	if ( oActiveEl && oActiveEl.tagName == 'SELECT' )
	{
		GetE('txtName').value	= oActiveEl.name ;
		GetE('txtType').value	= oActiveEl.typeid;	
		GetE('txtRemark').value	= GetAttribute( oActiveEl, 'remark' ) ;
	    GetE('order').value	= GetAttribute( oActiveEl, 'order' ) ;
	}
	else
		oActiveEl = null ;
	dialog.SetOkButton( true ) ;
	dialog.SetAutoSize( true ) ;
	SelectField( 'txtName' ) ;
	if(GetE('txtType').options[GetE('txtType').selectedIndex].text=="密级"){//密级
	  GetE('mc').style.display='none';
	}
}

function Ok()
{
	oEditor.FCKUndo.SaveUndoStep() ;
	if(GetE('txtType').options[GetE('txtType').selectedIndex].text=="密级"){//密级
	 GetE('txtName').value = "sdcncsi_ict_mj"; //yanggc
	}else{
	 var txtName = 	GetE('txtName').value ;
	 var c = /^[a-zA-Z][0-9a-zA-Z_]*$/;   
     if (!c.test(txtName)){
      alert("名称只能为字母、数字、下划线，且必须以字母开头！！"); 
      return;  
     } 
	}
	
	if(!CheckInput(GetE('txtName'),GetE('txtRemark')))
		return false;
	var order = GetE('order').value;	
	if(order!=''&&isNaN(order.Trim())){
	 alert("排序序号必须为数字！！");
	 return false;
	}
	
	var sSize = GetE('txtLines').value ;
	
	oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'SELECT', {name: GetE('txtName').value,typeid:GetE('txtType').value} ) ;
	SetAttribute( oActiveEl, 'remark', GetE('txtRemark').value ) ;
	SetAttribute( oActiveEl, 'order', GetE('order').value ) ;
	while ( oActiveEl.options.length > 0 )
		oActiveEl.remove(0) ;

	var sText = '\${t.sname}';
	var sValue = '\${t.svalue}';
	var oOption = AddComboOption( oActiveEl, sText , sValue , oDOM);
	oOption.selected = false ;
	if(oActiveEl.parentNode){
	  node=oActiveEl.parentNode;
	  while(true){
	   if(node.nodeName=='TD'){
	    break;
	   }else{
	   if(node.parentNode){
	   node = node.parentNode;
	   }else{
	   break;
	   }
	   }
	  }
	  if(node.nodeName=='TD'){
	  	 node.id="td"+GetE('txtName').value;
	 node.setAttribute("remark",GetE('txtRemark').value);  
	 node.setAttribute("class","\${"+node.id+"_tdClass}"); 
	  SetAttribute(oActiveEl,'pid',node.id);
	  }
	}
	return true ;
}

function CheckInput( name , remark )
{
	if(name.value.Trim()==''){
		alert('\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a');
		SelectField( 'txtName' ) ;
		return false;
	}
	if(name.value.Trim()!=='')
	{
		if(!isNaN(name.value.Trim())){
			alert('\u540d\u79f0\u4e0d\u80fd\u4e3a\u6570\u5b57');
			name.focus();
			return false;
		}
		if(remark.value.Trim()==''){
			alert('\u5185\u5bb9\u63cf\u8ff0\u4e0d\u80fd\u4e3a\u7a7a');
			remark.focus();
			return false;
		}
	}
	return true;
}
function showName(v){
if(v=="密级"){
 GetE('mc').style.display='none';
  SelectField( 'txtRemark' ) ;
 }else{
 GetE('mc').style.display='';
  SelectField( 'txtName' ) ;
 }
}
</script>
</head>
<body style="overflow: hidden">
<table width="100%" height="100%">
	<tr>
		<td>
		<table width="100%">
		   <!-- 
			<tr>
				<td nowrap></td>
				<td width="100%" colSpan="2"><input type="hidden" id="txtName" value="sdcncsi_ict"
					size="36" type="text"></td>
			</tr>
			 -->
			<tr id="mc">
				<td nowrap><span fcklang="DlgTextName">Name</span></td>
				<td><input id="txtName" type="text" size="36" /></td>
			</tr>
			<tr>
				<td nowrap><span fcklang="DlgRemarkText">Remark</span></td>
				<td><input id="txtRemark" type="text" size="36" /></td>
			</tr>
			<tr style="visibility: hidden">
				<td nowrap><span fckLang="DlgSelectSize">Size</span>&nbsp;</td>
				<td nowrap><input id="txtLines" type="text" size="2" value="">&nbsp;<span
					fckLang="DlgSelectLines">lines</span></td>
				<td nowrap align="right"><input id="chkMultiple"
					name="chkMultiple" type="checkbox"><label for="chkMultiple"
					fckLang="DlgSelectChkMulti">Allow multiple selections</label></td>
			</tr>
			<tr>
				<td nowrap><span fcklang="DlgComponentOrder">DlgComponentOrder</span></td>
				<td><input id="order" type="text" size="36" /></td>

			</tr>
		</table>
		<hr style="POSITION: absolute; width: 95%">
		<span style="LEFT: 10px; POSITION: relative; TOP: -7px"
			class="BackColor">&nbsp;<span fckLang="DlgSelectOpAvail">Available
		Options</span>&nbsp;</span>
		<table width="100%">
			<tr>
				<td width="50%"><span fckLang="DlgSelectTypeText">Text</span></td>
				<td><select id="txtType" onchange="showName(this.options[this.selectedIndex].text)">
					<%
								List<Map> list = (List<Map>)request.getAttribute("types");
							    for(Map map : list){
								out.print("<option");
								//out.append(" value="+(String)map.get("id"));
								out.append(" value="+Integer.parseInt(map.get("id").toString()));
								out.print(">");
								out.print((String)map.get("description"));
								out.print("</option>");
							}
							%>
				</select></td>
			</tr>
		</table>
		<table width="100%" style="display:none">
			<tr>
				<td width="50%"><span fckLang="DlgSelectOpText">Text</span><br>
				<input id="txtText" style="WIDTH: 100%" type="text" name="txtText">
				</td>
				<td width="50%"><span fckLang="DlgSelectOpValue">Value</span><br>
				<input id="txtValue" style="WIDTH: 100%" type="text" name="txtValue">
				</td>
				<td vAlign="bottom"><input onclick="Add();" type="button"
					fckLang="DlgSelectBtnAdd" value="Add"></td>
				<td vAlign="bottom"><input onclick="Modify();" type="button"
					fckLang="DlgSelectBtnModify" value="Modify"></td>
			</tr>
			<tr>
				<td rowSpan="2"><select id="cmbText" style="WIDTH: 100%"
					onchange="GetE('cmbValue').selectedIndex = this.selectedIndex;Select(this);"
					size="5" name="cmbText"></select></td>
				<td rowSpan="2"><select id="cmbValue" style="WIDTH: 100%"
					onchange="GetE('cmbText').selectedIndex = this.selectedIndex;Select(this);"
					size="5" name="cmbValue"></select></td>
				<td vAlign="top" colSpan="2"></td>
			</tr>
			<tr>
				<td vAlign="bottom" colSpan="2"><input style="WIDTH: 100%"
					onclick="Move(-1);" type="button" fckLang="DlgSelectBtnUp"
					value="Up"> <br>
				<input style="WIDTH: 100%" onclick="Move(1);" type="button"
					fckLang="DlgSelectBtnDown" value="Down"></td>
			</tr>
			<TR>
				<TD vAlign="bottom" colSpan="4"><INPUT
					onclick="SetSelectedValue();" type="button"
					fckLang="DlgSelectBtnSetValue" value="Set as selected value">&nbsp;&nbsp;
				<input onclick="Delete();" type="button"
					fckLang="DlgSelectBtnDelete" value="Delete"></TD>
			</TR>
		</table>
		</td>
	</tr>
</table>
</body>
</html>