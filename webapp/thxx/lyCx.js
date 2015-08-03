var page_num  = 10;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/thxx.doAction?method=queryLy&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'caller',title:'主叫',width:$(this).width() * 0.25,align:'center'},
			{field:'called',title:'被叫',width:$(this).width() * 0.25,align:'center'},
			{field:'calltype',title:'录音类型',width:$(this).width() * 0.25,align:'center'},
			{field:'cjsj',title:'通话时间',width:$(this).width() * 0.25,align:'center'},
			{field:'filename',title:'播放',width:$(this).width() * 0.1,align:'center',formatter:rowformater}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function clickData_datalist(num){
	query(num,page_num);
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function rowformater(value,row,index){
	return "<a href='#' style='text-decoration:none;' onclick=play('"+value+"')>播放</a>";
}
function play(url){
    window.open("../play.jsp?url="+url,"","height=100, width=500,toolbar=no, menubar=no, scrollbars=no, left=500,top=400,resizable=no,location=no, status=no,titlebar=no");
}