var page_num = 10;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/formmanager.coffice?method=listForm&page_num='+page_num,
		toolbar:[{
			text:'删除',
			iconCls:'icon-cut',
			handler:function(){
				del();
			}
			
		},
		{
			text:'启用',
			iconCls:'icon-ok',
			handler:function(){
				 start();
			}
		},
		{
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				 add();
			}
		},
		{
			text:'编辑',
			iconCls:'icon-edit',
			handler:function(){
				 edit();
			}
		}
		],
		frozenColumns:[[
	        {field:'id',checkbox:true}
		]],
		columns:[[
			{field:'tablename',title:'表名',width:$(this).width() * 0.2,align:'center'},
			{field:'mc',title:'表单名称',width:$(this).width() * 0.2,align:'center'},
			{field:'zt',title:'状态',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openForm(rowData.id,rowData.name);
		}	
	});
})
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/formmanager.coffice?method=delForm","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function stop(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/managerflow.coffice?method=stopFlow","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function start(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/formmanager.coffice?method=startForm","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function clickData_datalist(num){
	query(num,page_num);
}
function openForm(id,name){
	window.open(webcontext+"/formmanager.coffice?method=showForm&id="+id,"表单展现");
}
function add(){
	$('#form_show')[0].reset();
	$("#dataedit").window("open");
}
function edit(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	$.ajax({type:"POST", url:webcontext+"/formmanager.coffice?method=loadTemplateData", data:"id="+id, dataType:"text", success:function (data) {
		var oEditor = FCKeditorAPI.GetInstance('EditorDefault');
		$("tableid").attr("value",id);
        oEditor.SetHTML(data, false);
        $("#dataedit").window("open");
	}});
}
function FCKeditor_OnComplete(editorInstance) {
		window.status = editorInstance.Description;
}
			/**
             * check name
             */
function checkNameByReg(formString){
        var reg = / name=[^\s|^>]*/gi;
        var result = formString.match(reg);
        if(result){
            for (var i = 0; i < result.length; i++) 
                 result[i] = replaceQuote(result[i]);
        }else{
            return false;
        }
        return checkUnique(result,'名称');
}
            /**
             * check id
             */
function checkIdByReg(formString){
       var reg = / id=[^\s|^>]*/gi;
       var result = formString.match(reg);
       if(result){
              for (var i = 0; i < result.length; i++) 
                   result[i] = replaceQuote(result[i]);
       }else{
            return false;
       }
       return checkUnique(result,'Id');
}
            /**
             * quote filter
             * @param {Object} targetString
             */
function replaceQuote(targetString){
        return targetString.replace(/"+/, '').replace(/"$/, '');
}
            /**
             * check unique
             * @param {Object} objContainer
             */
function checkUnique(objContainer,remark){
         var flag = false;
         var notUnique = "";
         for (var i = 0; i < objContainer.length; i++) {
             for (var j = i + 1; j < objContainer.length; j++) {
                     if (objContainer[i] == objContainer[j]) {
                         flag = true;
                         notUnique = objContainer[j];
                         break;
                     }
             }
             if (flag) break;
         }
         if (flag){
         	      alert(remark+' : '+notUnique.split('=')[1]+'不唯一');
         } 
         return flag;
}