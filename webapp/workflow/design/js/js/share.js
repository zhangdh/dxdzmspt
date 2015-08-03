

/*
    处理标签

*/

//角色标签
    function DisplayRoleTabs(me,url) {
		var output;
	    b = new Array(2);
		b[0] = 'taboff';
		b[1] = 'taboff';
		b[2] = 'taboff';
		b[me] = 'tabon';
		
			output = '<table id="tabTable" width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tableBorderInner2"><tr>';
			
			output += '<td  id="c'+ b[0]+'" class="' + b[0] + '" onClick=tabDownRole(0,"tabTable");document.getElementById("leftFrame").src="oswfconfig/nodeConfig/role.jsp"><input type="radio" checked name="roletype" value="2"/><span class="TC">按照角色</span></td>';
			output += '<td id="c'+ b[1]+'" class="' + b[1] + '" onClick=tabDownRole(1,"tabTable");document.getElementById("leftFrame").src="oswfconfig/nodeConfig/user.jsp"><input type="radio" name="roletype"  value="1"/><span class="TC">按照人员</span></td>';
		    output += '<td id="c'+ b[2]+'" class="' + b[2] + '" onClick=tabDownRole(2,"tabTable");document.getElementById("leftFrame").src="oswfconfig/nodeConfig/department.jsp"><input type="radio" name="roletype" value="0"/><span class="TC">按照部门</span></td>';
		
		   output += '</tr></table>';			
	
			self.document.write(output);
	   
	}
  function tabDownRole(index,objName,src,id,path) {
	   var idList = document.getElementById(objName);
	   for(var i=0 ;i <idList.rows[0].cells.length; i++){	
	               if(i==index){	
	                document.getElementsByName("roletype")[index].checked=true;
	                 idList.rows[0].cells[i].className="tabon";
	                 if(src!=undefined){
	                  document.getElementById("leftFrame").src=src;
	                 }
	                
	               }else{
	                 idList.rows[0].cells[i].className="taboff";
	               }
				
		}
		 
	}
	
