<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<script>
	function getData(){
		var d = window.opener.document.getElementById("formFrame").contentWindow.document;
		//alert(d.getElementById("tdjbyj").getElementsByTagName("span").length);
		document.getElementById("ldr").innerHTML=d.getElementById("ldr").value;
		document.getElementById("lxdh").innerHTML=d.getElementById("lxdh").value;
		document.getElementById("slbh").innerHTML=d.getElementById("slbh_day").value;
		document.getElementById("zblx").innerHTML=d.getElementById("bjxz").options[d.getElementById("bjxz").selectedIndex].text;
		document.getElementById("lxdz").innerHTML=d.getElementById("lxdz").value;
		document.getElementById("title").innerHTML=d.getElementById("undo_title").value;
		document.getElementById("fywt").innerHTML=d.getElementById("fywt").value;
		document.getElementById("cldwyj").innerHTML=d.getElementById("cldwyj").value;
		document.getElementById("ldrq").innerHTML=d.getElementById("ldrq").value;
		document.getElementById("clqx").innerHTML=d.getElementById("clqx").value;
		document.getElementById("jbyj").innerHTML=d.getElementById("tdjbyj").innerHTML;
		//alert(d.getElementById("djbyj").innerHTML);
	}
	function oper(){
		getData();
		var obj = document.getElementById('tableDiv');
    	var newWindow=window.open("","_blank");
    	var docStr = obj.innerHTML;
    	newWindow.document.write(docStr);
    	newWindow.document.close();
    	newWindow.print();
    	newWindow.close();
 		window.close();
	}
</script>
</head>
<body id="bodyid" onload = "oper();">  
<div id="tableDiv" align=center style="width:21cm;">    
<table width="100%"   cellspacing="0" cellpadding="0" style="border-collapse:collapse;border:none;">
	<caption><font size="5" style="font-family:'黑体';">平凉市综合便民服务热线反映事项交办单</font></caption>
	<tr >
	<td style="height:20px"></td>
	</tr>
    <tr>
      <td align="center"  style="WIDTH: 15%;border:solid #000 1px;font-size:18px;font-family:'黑体';">来电人</td>
      <td align="left" style="WIDTH: 30%; HEIGHT: 30px; border:solid #000 1px;">
           <label id="ldr" name="ldr"></label>
      </td>
      <td  align="center" style="WIDTH: 15%;border:solid #000 1px;font-size:18px;font-family:'黑体';">联系电话</td>
      <td  align="left" style="WIDTH: 30%; HEIGHT: 30px; border:solid #000 1px;">
           	<label id="lxdh"></label>
      </td>
    </tr>
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">受理编号</td>
      <td  align="left" style="HEIGHT: 30px;border:solid #000 1px;">
           	<label id="slbh"></label>
      </td>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">转办类型</td>
      <td  align="left" style="HEIGHT: 30px;border:solid #000 1px;">
            <label id="zblx"></label>
      </td>
    </tr>
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">来电日期</td>
      <td  align="left" style="HEIGHT: 30px;border:solid #000 1px;">
           	<label id="ldrq"></label>
      </td>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">处理期限</td>
      <td  align="left" style="HEIGHT: 30px;border:solid #000 1px;">
            <label id="clqx"></label>
      </td>
    </tr>
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">联系地址</td>
      <td  align="left" colspan=3  style="HEIGHT: 30px;border:solid #000 1px;">
       		<label id="lxdz"></label>
      </td>
    </tr>
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">标题</td>
      <td  align="left" colspan=3  style="HEIGHT: 40px;border:solid #000 1px;">
       		<label id="title"></label>
      </td>
    </tr>  
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">反映问题</td>
      <td  align="left" colspan=3  style="HEIGHT: 200px;border:solid #000 1px;">
       		<label id="fywt"></label>
      </td>
    </tr>  
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">拟办意见</td>
      <td  align="left" colspan=3  style="HEIGHT: 100px;border:solid #000 1px;">
       		<label id="jbyj"></label>
      </td>
    </tr> 
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">领导批示</td>
      <td  align="left" colspan=3  style="HEIGHT: 200px;border:solid #000 1px;">
       		<label id="ldyj"></label>
      </td>
    </tr>  
    <tr>
      <td  align="center" style="border:solid #000 1px;font-size:18px;font-family:'黑体';">处理情况</td>
      <td  align="left" colspan=3  style="HEIGHT: 100px;border:solid #000 1px;">
       		<label id="cldwyj"></label>
      </td>
    </tr>         
</table>
</div>
</body>
</html>
