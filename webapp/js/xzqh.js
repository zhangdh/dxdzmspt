function get_ds()
{
alert("dddd");
	$("#xq").empty();
	$("#xz").empty();
if($("#sf").val()!=""){
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=2&id="+$("#sf").val(),'',function(json){
		bind(json);
	});	
	}
}

function get_xq()
{
	$("#xz").empty();
	if($("#ds").val()!=""){
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=3&id="+$("#ds").val(),'',function(json){
		bind(json);
	});	
	}
}
function get_xz()
{
	if ($("#xq :selected").text()=="市辖区")
	{
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=4&id="+$("#xq").val(),'',function(json){
		bind(json);
	});	
	}
	else
	$("#xz").empty();
}
function get_cx_ds()
{
  if($("#cx_sf").val()!=""){
	$("#xq").empty();
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=12&id="+$("#cx_sf").val(),'',function(json){
		bind(json);
	});	
	}
}
function get_cx_xq()
{
if($("#cx_ds").val()!=""){
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=13&id="+$("#cx_ds").val(),'',function(json){
		bind(json);
	});	
   }
}

function get_cx_xz()
{
	if ($("#cx_xq :selected").text()=="市辖区")
	{
	sys_ajaxPostDirect("/dm/xzqhf/default.do?method=GetXzqh&jb=14&id="+$("#cx_xq").val(),'',function(json){
		bind(json);
	});	
	}
	else
	$("#cx_xz").empty();
}