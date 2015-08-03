var obj = window.dialogArguments;
var ywid = obj.ywid;
var wr_flag = obj.wr_flag;
var mk_dm_mc = obj.mk_dm_mc;
var self_def=obj.self_def;
sys_btn_auth = "btn_save;btn_insert;btn_del;";
$(function() {
			sys_expandTable("table_list", 5);
			sys_ajaxGet("/msg/default.do?method=query&ywid=" + ywid
							+ "&mk_dm_mc=" + mk_dm_mc + "&wr_flag=" + wr_flag,
					"", function(json) {
						bindcs(json);
						if (json.wr_flag == "w") {
							sys_showButton("btn_save");
						} else if (json.wr_flag == "r") {
							try {
								sys_showButton("");
							} catch (err) {
							}
						}
						bind(json);
					});

			$("#btn_insert").click(function() {
						var _ywid = $("#ywid").val();
						var _wr_flag = $("#wr_flag").val();
						var _mk_dm_mc = $("#mk_dm_mc").val();
						$('#form_show')[0].reset();
						$("#ywid").val(_ywid);
						$("#wr_flag").val(_wr_flag);
						$("#mk_dm_mc").val(_mk_dm_mc);
						sys_showButton("btn_save");
					});
			// 保存数据
			$("#btn_save").click(function() {
				if(!Validate.CheckForm($('#form_show')[0]))return;
				if ($("#guid").val() == "") {
					var queryString = $("#form_show").serialize();
					// alert("保存的queryString"+queryString);
					sys_ajaxPost("/msg/default.do?method=save", queryString,
							function(msg) {
								ajaxAlert(msg);
								sys_ajaxGet(
										"/msg/default.do?method=query&ywid="
												+ ywid + "&mk_dm_mc="
												+ mk_dm_mc + "&wr_flag="
												+ wr_flag, "", function(json) {
											bindcs(json);
											bind(json);
										});
								sys_showButton("btn_save,btn_insert");
							});
				} else {
					// alert("执行修改");
					if (!Validate.CheckForm($('#form_show')[0]))
						return;
					$("#yh_id").val($("#yhid").val());
					var queryString = $("#form_show").serialize();
					// alert("修改的queryString"+queryString);
					sys_ajaxPost("/msg/default.do?method=modi", queryString,
							function(msg) {
								ajaxAlert(msg);
								sys_ajaxGet(
										"/msg/default.do?method=query&ywid="
												+ ywid + "&mk_dm_mc="
												+ mk_dm_mc + "&wr_flag="
												+ wr_flag, "", function(json) {
											bindcs(json);
											bind(json);
										});
								sys_showButton("btn_save");
							});
				}
			});
			$("#btn_del").click(function() {
				//if (!Validate.CheckForm($('#form_show')[0]))return;
				$("#yh_id").val($("#yhid").val());
				var queryString = $("#form_show").serialize();
				if (window.confirm("确定要删除当前的数据吗？") == false)
					return false;
				sys_ajaxPost("/msg/default.do?method=del", queryString,
						function(msg) {
							ajaxAlert(msg);
							$('#form_show')[0].reset();
							sys_showButton("btn_save");
							sys_ajaxGet("/msg/default.do?method=query&ywid="
											+ ywid + "&mk_dm_mc=" + mk_dm_mc
											+ "&wr_flag=" + wr_flag, "",
									function(json) {
										bindcs(json);
										bind(json);
									});
						});
			});
			if(self_def===undefined||self_def===""){
				window.onbeforeunload = function() {
					window.dialogArguments.window
							.sys_msg_setYyts($("#count").val());
					window.close();
				}
			}else{
				window.onbeforeunload = function() {
				window.dialogArguments.window
						.sys_msg_setYyts($("#count").val(),self_def);
				window.close();
			}
			}
			
		});
// 分页查询回调函数
function callback_getPageData_table_list(pagenum) {
	var querystr = $("#form_query").serialize();
	querystr += "&page_goto=" + pagenum;
	sys_ajaxGet("/msg/default.do?method=query&ywid=" + ywid + "&mk_dm_mc="
					+ mk_dm_mc + "&wr_flag=" + wr_flag, querystr,
			function(json) {
				bindcs(json);
				bind(json);
			});
}
// 点击列表显示明细数据回调函数
function callback_trclick_table_list(guid) {
	sys_ajaxGet("/msg/default.do?method=show&ywid=" + ywid + "&mk_dm_mc="
					+ mk_dm_mc + "&wr_flag=" + wr_flag, {
				guid : guid
			}, function(json) {
				// Dumper.alert(json);
				bind(json);
			});
	if ($("#wr_flag").val() == "w") {
		sys_showButton("btn_save,btn_insert,btn_del");
	} else if ($("#wr_flag").val() == "r") {
		try {
			sys_showButton("");
		} catch (err) {
		}
	}
}

function bindcs(json) {
	if (json.count) {
		$("#count").val(json.count);
	}
	if (json.wr_flag) {
		$("#wr_flag").val(json.wr_flag);
	}
	if (json.mk_dm_mc) {
		$("#mk_dm_mc").val(json.mk_dm_mc);
	}
	if (json.ywid) {
		$("#ywid").val(json.ywid);
	}
}
