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
<script type="text/javascript">
var dialog	= window.parent ;
var oEditor = dialog.InnerDialogLoaded() ;

// Gets the document DOM
var oDOM = oEditor.FCK.EditorDocument ;
var oActiveEl = dialog.Selection.GetSelectedElement() ;

window.onload = function()
{
	// First of all, translate the dialog box texts
	oEditor.FCKLanguageManager.TranslatePage(document) ;

	if ( oActiveEl && oActiveEl.tagName == 'SPAN' )
	{
		GetE('txtName').value		= oActiveEl.name ;
	}
	else
		oActiveEl = null ;

	dialog.SetOkButton( true ) ;
	dialog.SetAutoSize( true ) ;
    window.GetE( 'txtRemark' ).focus();
}
function Ok()
{
	//yupeng
	 oEditor.FCKUndo.SaveUndoStep() ;

	if(!CheckInput(GetE('txtName'),GetE('txtRemark')))
		return false;
	var order = GetE('order').value;	
	if(order!=''&&isNaN(order.Trim())){
	 alert("排序序号必须为数字！！");
	 return false;
	}
	oActiveEl = oEditor.FCK.InsertElement( 'div' ) ;
	oActiveEl = oEditor.FCK.InsertElement( 'span' ) ;
	oActiveEl.innerHTML = '\${'+GetE('txtName').value+'}';
	SetAttribute( oActiveEl, 'name', GetE('txtName').value ) ;
	SetAttribute( oActiveEl, 'id', GetE('txtName').value ) ;
	SetAttribute( oActiveEl, 'datatype', GetE('txtType').value ) ;
	SetAttribute( oActiveEl, 'remark', GetE('txtRemark').value ) ;
	SetAttribute( oActiveEl, 'inserttype', GetE('insertType').value ) ;
	SetAttribute( oActiveEl, 'order', order ) ;
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
</script>
</head>
<body style="overflow: hidden">
<table width="100%" height="100%">
	<tr>
		<td>
		<table width="100%">
			<tr>
				<td nowrap></td>
				<td width="100%" colSpan="2"><input type="hidden" id="txtName" value="sdcncsi_ict_wh"
					style="WIDTH: 99%" type="text"></td>
			</tr>
			<tr>
				<td nowrap><span fckLang="DlgRemarkText">Remark</span>&nbsp;</td>
				<td width="100%" colSpan="2"><input id="txtRemark" type="text" style="WIDTH: 99%"/></td>
			</tr>
		   <tr>
				<td nowrap>生成方式&nbsp;</td>
				<td width="100%" colSpan="2">
                <select id="insertType">
                <option value="0">自动</option>
                <option value="3">手动</option>
                </select>
				</td>
			</tr>
			<tr>
			  <td nowrap><span fcklang="DlgComponentOrder">DlgComponentOrder</span>&nbsp;</td>
			  <td width="100%" colSpan="2"><input id="order" type="text" style="WIDTH: 99%"/></td>
			</tr>
			<tr></tr>
		</table>
		<hr style="POSITION: absolute; width: 95%">
		<span style="LEFT: 10px; POSITION: relative; TOP: -7px"
			class="BackColor">&nbsp;<span fckLang="DlgSelectOpAvail">Available
		Options</span>&nbsp;</span>
		<table width="100%">
			<tr>
				<td width="50%"><span fckLang="DlgSelectTypeText">Text</span></td>
				<td><select id="txtType">
					<%
								List<Map> list = (List<Map>)request.getAttribute("types");
							for(Map map : list){
								out.print("<option");
								out.append(" value="+(String)map.get("id"));
								out.print(">");
								out.print((String)map.get("mc"));
								out.print("</option>");
							}
							%>
				</select></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>