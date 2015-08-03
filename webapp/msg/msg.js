var page_num  = 10;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/msg.coffice?method=queryLy&ywid='+ywid+'&page_num='+page_num,
		toolbar:[{
			text:'新增留言',
			iconCls:'icon-add',
			handler:function(){
				add();
			}
		}],
		frozenColumns:[
	        {field:'guid',hidden:true}
		],
		columns:[[
			{field:'msgnr',title:'留言内容',width:$(this).width() * 0.2,align:'center'},
			{field:'yhid',title:'留言人员',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'留言时间',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openMsg(rowData.msgnr);
		}	
	});
});
function clickData_datalist(num){
	query(num);
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	$('#datalist').datagrid('load');
}

function add(){
	$('#form_show')[0].reset();
	$("#msg_div").window("open");
	$("#btns_div").show();
}
function openMsg(msgnr){
	$("#msg_div").window("open");
	$("#msgnr").val(msgnr);
	$("#btns_div").hide();
}
function save(){
	_Post("/msg.coffice?method=saveLy","msgnr="+$("#msgnr").val()+"&ywid="+ywid,function(jsonData){
		$("#msg_div").window("close");
		query();
	});
}