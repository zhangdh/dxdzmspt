<%@ page import="com.coffice.util.cache.Cache"%>
<%@ page import="com.coffice.util.SysPara"%>
<%@ page import="java.util.*"%>
<%
String pport=SysPara.getValue("pic_port");
%>
<link type="text/css" href="${ctx}/js/jquery.ui/css/jquery-ui-1.8.2.custom.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/css/default.css" rel="stylesheet" />
<script src="${ctx}/js/jquery-1.3.1.js?t=20090220" type="text/javascript"></script>
<script src="${ctx}/js/jquery.tablehover.js?t=20090220" type="text/javascript"></script>
<script src="${ctx}/js/jquery.checkboxes.js?t=20090220" type="text/javascript"></script>
<script src="${ctx}/js/jquery.ui/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
<script src="${ctx}/js/taffy.js?t=2010726" type="text/javascript"></script>
<script src="${ctx}/js/select.js?t=2010726" type="text/javascript"></script>
<script src="${ctx}/js/common.js" type="text/javascript"></script>
<script src="${ctx}/js/datadumper.js?t=2009011" type="text/javascript"></script>
<script src="${ctx}/js/validate.js?t=20090111" type="text/javascript"></script>
<script src="${ctx}/js/datePicker/WdatePicker.js?t=2123580090111" type="text/javascript"></script>
<script type="text/javascript">
	var sys_ctx="${ctx}";
	var simplefect = "3";
  var sys_btn_auth=""; 
	var sys_yh_gw_sel="";
	var sys_yh_gw="";
	var sys_pport='<%=pport%>';
	var sys_yhid='<%=(String)request.getAttribute("yhid")%>';
	var document_kj_type = '<%=SysPara.getValue("document_kj_type")%>';
	var att_show_type = '';
	var att_uploadify_type='';
	<%
		try {
			String str = SysPara.getValue("att_show_type");
			//System.out.println(str);
	%>
		att_show_type = '<%=str %>';
	<%
		} catch(Exception e) {
	%>
		att_show_type = '';
	<%		
		}
	%>
	
	<%
		try {
			String strUplodify = SysPara.getValue("att_uploadify_batch");
			if("".equals(strUplodify)) {
				strUplodify="true";
			}
	%>
		att_uploadify_batch = '<%=strUplodify %>';	
	<%
		} catch(Exception e) {
	%>
		att_uploadify_batch='true';
	<%		
		}
	%>
	
	<%
	String mk__dm = "";
	String uri = request.getRequestURI();
	uri = uri.replaceFirst(request.getContextPath()+"/","");
    String[] mk_str = uri.split("/");
	List<Map>  url__list = (List<Map>)Cache.getGlobalInfo("","url");
	for(Map map:url__list){
	 if(mk_str[0]!=null&&String.valueOf(map.get("url")).indexOf(mk_str[0])>=0){
	  mk__dm = String.valueOf(map.get("mk_dm"));
	  break;
	 }
	}
	String temp="";
	List<Map>  __list = (List<Map>)Cache.getUserInfo((String)request.getAttribute("yhid"),"gw_sel");
	
	Cache.setGlobalInfo("sys","contextPath",request.getContextPath());
	
	//String mk_dm = request.getParameter("mk_dm");
	//String mk_dm="100";
	if(__list!=null){
	for(Map map : __list){
	 if(String.valueOf(map.get("mk_dm")).equals(mk__dm)){
	  temp = String.valueOf(map.get("gwid"));
	  break;
	 }
	}
   }
    String sys_btn_auth="";
   	List<Map>  Btn__list = (List<Map>)Cache.getUserInfo((String)request.getAttribute("yhid"),"sys_btn_auth");
   	if(Btn__list!=null){
   	 for(Map map : Btn__list){
   	  if(String.valueOf(map.get("url")).equals("/"+uri)){
   	  sys_btn_auth= sys_btn_auth+","+String.valueOf(map.get("id"));
   	  }
   	 }
   	}
   	sys_btn_auth = sys_btn_auth.replaceFirst(",","");
	%>
	var mk_dm = '<%=mk__dm%>';
	sys_yh_gw_sel="<%=temp%>";
	if(sys_yh_gw_sel!="null"&&sys_yh_gw_sel!=""){
	sys_yh_gw = sys_yh_gw_sel;
	}else{
	sys_yh_gw="<%=Cache.getUserInfo((String)request.getAttribute("yhid"),"org")%>";//
    }
	sys_btn_auth='<%=sys_btn_auth%>';
	<%
	  String mHttpUrlName=request.getRequestURI();
	  String mScriptName=request.getServletPath();
	  String mServerName="template/OfficeServer.jsp";
	  String mServerUrl=mHttpUrlName.substring(0,mHttpUrlName.lastIndexOf(mScriptName))+"/"+mServerName;
	  String mHttpUrl=mHttpUrlName.substring(0,mHttpUrlName.lastIndexOf(mScriptName))+"/";
	  String mFjServerUrl="http://"+request.getServerName()+":"+request.getServerPort();
	%>
	var mServerUrl="http://"+location.hostname+":"+location.port+"<%=mServerUrl%>";
	var mHttpUrl="http://"+location.hostname+":"+location.port+"<%=mHttpUrl%>";
	var uname="<%=Cache.getUserInfo((String)request.getAttribute("yhid"),"xm")%>";
	var mFjServerUrl="http://"+location.hostname+":"+location.port;
</script>
