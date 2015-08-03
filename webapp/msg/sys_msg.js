window.returnValue="refresh";
/**
 * 留言反馈，
 * @param {} mk_dm  模块代码
 * @param {} ywid   当前记录业务id
 * @param {} wr_flag 是否可写
 */
 
 function sys_msg_span_show(mk_dm,ywid,wr_flag,self_def){
	if(self_def===undefined||self_def===""){
		if(($("#sys_msg_span").html())==""){
		   $("#sys_msg_span").append("<input type='button' name='sys_msg_lyfk' id='sys_msg_lyfk' value='留言反馈' ><input type='hidden' name='sys_msg_yyts' id='sys_msg_yyts' size='1'>");
		}
		$("#sys_msg_lyfk").unbind('click');
		$("#sys_msg_lyfk").click(function(){
			var obj=new Object();
		    obj.ywid=ywid;
		    obj.mk_dm_mc=mk_dm;
		    obj.wr_flag=wr_flag;
		    obj.window = window;
			window.showModalDialog(sys_ctx+"/msg/msgList.jsp",obj,"dialogWidth:800px;dialogHeight:360px;help:no;");
		});
	}else{
		if(($("#sys_msg_span_"+self_def).html())==""){
		   $("#sys_msg_span_"+self_def).append("<input type='button' name='sys_msg_lyfk_"+self_def+"' id='sys_msg_lyfk_"+self_def+"' value='留言反馈' ><input type='hidden' name='sys_msg_yyts_"+self_def+"' id='sys_msg_yyts_"+self_def+"' size='1'>");
		}
		$("#sys_msg_lyfk_"+self_def).unbind('click');
		$("#sys_msg_lyfk_"+self_def).click(function(){
			var obj=new Object();
		    obj.ywid=ywid;
		    obj.mk_dm_mc=mk_dm;
		    obj.wr_flag=wr_flag;
		    obj.self_def=self_def;
		    obj.window = window;
			window.showModalDialog(sys_ctx+"/msg/msgList.jsp",obj,"dialogWidth:800px;dialogHeight:360px;help:no;");
		});		
	}
}


function  sys_msg_setYyts(count,self_def){
	if(self_def==undefined||self_def==""){
		$("#sys_msg_yyts").val(count);
		$("#sys_msg_lyfk").val("留言反馈"+"（"+$("#sys_msg_yyts").val()+"）条");
	}else{
		$("#sys_msg_yyts_"+self_def).val(count);
		$("#sys_msg_lyfk_"+self_def).val("留言反馈"+"（"+$("#sys_msg_yyts_"+self_def).val()+"）条");
	}
		
}
function sys_msg_intYyts(self_def){
	if(self_def==undefined||self_def==""){
	    $("#sys_msg_lyfk").val("留言反馈"+"("+$("#sys_msg_yyts").val()+")条");
	}else{
		$("#sys_msg_lyfk_"+self_def).val("留言反馈"+"("+$("#sys_msg_yyts_"+self_def).val()+")条");
	}
}
