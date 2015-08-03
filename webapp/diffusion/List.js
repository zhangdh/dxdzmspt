var page_num  = 10;
$(function(){
	_Post("/diffusion.coffice?method=queryLb","",function(jsonData){
		_loadSelect(jsonData);
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/diffusion.coffice?method=queryJs&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'guid',checkbox:true}
		]],
		columns:[[
			{field:'zt',title:'信息主题',width:$(this).width() * 0.6,align:'center'},
			{field:'xxlb',title:'信息类别',width:$(this).width() * 0.2,align:'center'},
			{field:'fbr',title:'发布人',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'发送时间',width:$(this).width() * 0.2,align:'center'},
			{field:'yxqq',title:'有效时间起',width:$(this).width() * 0.2,align:'center'},
			{field:'yxqz',title:'有效时间止',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
					//alert('onBeforeLoad'+paras);
					//alert('onBeforeLoad');
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openDiff(rowData.guid);
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
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
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var guid = selectrow.guid;
	_Post("/diffusion.coffice?method=delJs","guid="+guid,function(jsonData){
		alertMes(jsonData);
		query();
	});
}
function openDiff(guid){
	openwindow(webcontext+"/diffusion/DiffMx.jsp?guid="+guid,'',852,600);
}
function openwindow(url,name,iWidth,iHeight)
{
   var url;                                 
   var name;                            
   var iWidth;                          
   var iHeight;                        
   var iTop = (window.screen.availHeight-30-iHeight)/2;        
   var iLeft = (window.screen.availWidth-10-iWidth)/2;         
   window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes');
}
//点击列表显示明细数据回调函数
function clickData_datalist(num){
	query(num,page_num);
}