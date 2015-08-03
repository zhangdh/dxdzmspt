function initMsg(){
	//$("#msg_span").append("<input type='button' name='btn_msg' id='btn_msg'  onclick='openLy()' value='留言反馈' >");
	//$("#msg_span").append("<a href='javascript:void(0)' id='btn_msg' class='easyui-linkbutton' onclick='openLy()'>留言反馈</a>");
}
function openLy(){
	window.showModalDialog(webcontext+"/msg/msg.jsp?ywid="+$("#id").val()+"&mk_dm="+mk_dm,"","dialogWidth:800px;dialogHeight:360px;help:no;");
}