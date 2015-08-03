var page_num  = 10;
$(function() {
	_Post("/tjbb.coffice?method=initXzBlqk","",function(jsonData){
		_loadSelect(jsonData);		
	})
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/tjbb.coffice?method=xzBlqk&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'cghf',hidden:true},
	        {field:'hf',hidden:true}
		]],
		columns:[[
			{field:'bmmc',title:'部门名称',width:$(this).width() * 0.2,align:'center'},
			{field:'sjzs',title:'事件总数',width:$(this).width() * 0.1,align:'center'},
			{field:'asbj',title:'按时办结',width:$(this).width() * 0.1,align:'center'},
			{field:'csbj',title:'超时办结',width:$(this).width() * 0.1,align:'center'},
			{field:'wbj',title:'未办结',width:$(this).width() * 0.1,align:'center'},
			{field:'cswbj',title:'超时未办结',width:$(this).width() * 0.1,align:'center'},
			{field:'smy',title:'双满意',width:$(this).width() * 0.1,align:'center'},
			{field:'dmy',title:'单满意',width:$(this).width() * 0.1,align:'center'},
			{field:'bmy',title:'不满意',width:$(this).width() * 0.1,align:'center'},
			{field:'wfhf',title:'无法回访',width:$(this).width() * 0.1,align:'center'},
			{field:'whf',title:'未回访',width:$(this).width() * 0.1,align:'center'},
			{field:'yzy',title:'有争议',width:$(this).width() * 0.1,align:'center'},
			{field:'dhf',title:'待回访工单量',width:$(this).width() * 0.1,align:'center'},
			{field:'sj',title:'审结工单量',width:$(this).width() * 0.1,align:'center'},
			{field:'cbgd',title:'重办工单量',width:$(this).width() * 0.1,align:'center'},
			{field:'sqyq',title:'申请延期量',width:$(this).width() * 0.1,align:'center'},
			{field:'asbjl',title:'按时办结率',width:$(this).width() * 0.1,align:'center',formatter:asbjl},
			{field:'bjl',title:'办结率',width:$(this).width() * 0.1,align:'center',formatter:bjl},
			{field:'smyl',title:'双满意率',width:$(this).width() * 0.1,align:'center',formatter:smyl},
			{field:'myl',title:'满意率',width:$(this).width() * 0.1,align:'center',formatter:myl},
			{field:'hfl',title:'回复率',width:$(this).width() * 0.1,align:'center',formatter:hfl}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			//alert('加载数据失败');
		}
	});
});
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function asbjl(value,row,index){
	var asbj = parseInt(row.asbj);
	var csbj = parseInt(row.csbj);
	if((asbj+csbj)==0){
		return " ";
	}else{
		return Math.round((asbj*10000/(asbj+csbj)))/100+"%";
	}
}
function bjl(value,row,index){
	var asbj = parseInt(row.asbj);
	var csbj = parseInt(row.csbj);
	var wbj = parseInt(row.wbj);
	var cswbj = parseInt(row.cswbj);
	if((asbj+csbj+wbj+cswbj) == 0){
		return " ";
	}else{
		return Math.round(((asbj+csbj)*10000/(asbj+csbj+wbj+cswbj)))/100+"%";
	}
}
function smyl(value,row,index){
	var smy = parseInt(row.smy);
	var cghf = parseInt(row.cghf);
	if(cghf==0){
		return " ";
	}else{
		return Math.round((smy*10000/cghf))/100+"%";
	}
}
function myl(value,row,index){
	var smy = parseInt(row.smy);
	var dmy = parseInt(row.dmy);
	var cghf = parseInt(row.cghf);
	if(cghf==0){
		return " ";
	}else{
		return Math.round(((smy+dmy)*10000/cghf))/100+"%";
	}
}
function hfl(value,row,index){
	var hf = parseInt(row.hf);
	var asbj = parseInt(row.asbj);
	var csbj = parseInt(row.csbj);
	if((asbj+csbj) == 0){
		return " ";
	}else{
		return Math.round((hf*10000/(asbj+csbj)))/100+"%";
	}
}