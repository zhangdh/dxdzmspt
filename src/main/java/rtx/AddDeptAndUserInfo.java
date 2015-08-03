package rtx;

import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.util.BaseUtil;
import com.coffice.util.Db;


public class AddDeptAndUserInfo extends BaseUtil {

	/**
	 * 批量导入数据
	 * flag: 表示导入的是部门还是人员
	 * 	0表示部门，1表示人员
	 * infoBean里面是实体Bean的集合
	 * 实体Bean里面保存的是关于部门和人员的属性
	 * 
	 * 返回值：0表示成功，其它表示失败
	 */
	public int addDeptOrUserdBatch(int flag,List infoBean) {
		int iRet = -1;
		
		String deptId= "";//部门id
		String DetpInfo = "";//部门信息
		String DeptName = "";//部门名称
		String ParentDeptId = "";//上级部门id
		String userName = "";//用户姓名(即英文账号)
		String deptID = "";//部门id
		String chsName = "";//用户中文名称
		String pwd = "";//密码
		
		Iterator it = infoBean.iterator();
		while(it.hasNext()) {
			InfoBean info = (InfoBean)it.next();
			deptId = info.getDeptId();
			DetpInfo = info.getDetpInfo();
			DeptName = info.getDeptName();
			ParentDeptId = info.getParentDeptId();
			userName = info.getUserName();
			deptID = info.getDeptId();
			chsName = info.getChsName();
			pwd = info.getPwd();
			iRet = addDeptOrUserInfo(flag, deptId, DetpInfo, DeptName, ParentDeptId, userName, deptID, chsName, pwd);
		}
		return iRet;
	}
	
	/**
	 * 添加部门信息需要的基础信息：如果flag为0，这些字段必传
	 *  deptId：部门id
	 *  DetpInfo：部门信息
	 *  DeptName：部门名称
	 *  ParentDeptId：上级部门id
	 *  
	 * 添加人员信息需要的基础信息：如果flag为1，这些字段必传
	 *  userName: 用户姓名(即英文账号)
	 *  deptID: 部门id
	 *  chsName: 用户中文名称
	 *  pwd: 密码
	 *  
	 *  flag: 辨别维护的是部门信息还是人员信息
	 *   0表示添加部门
	 *   1表示添加人员
	 *   
	 *   返回值：0表示成功，其它表示失败
	 */
	public int addDeptOrUserInfo(int flag,String deptId,String DetpInfo,String DeptName,
			String ParentDeptId,String userName,String deptID,String chsName,String pwd) {		
		NamedParameterJdbcTemplate npjt = this.getNpjtA();
		String [][] info  = null;
		String rtxNum = "";
		int iRet = -1;
	//添加部门
		if(flag == 0) {
	    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
	    	if( RtxsvrapiObj.Init()) {   
	    		iRet = RtxsvrapiObj.addDept(deptId,DetpInfo,DeptName,ParentDeptId);
	    		if (iRet == 0) {
	    			System.out.println("添加部门成功");
	    		} else {
	    			System.out.println("添加部门失败");
	    		}
		    }	
	    	RtxsvrapiObj.UnInit();
		}
		
		//添加人员
		if(flag == 1) {
	    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
	    	if( RtxsvrapiObj.Init()) {   
	    		iRet =RtxsvrapiObj.addUser(userName,deptID,chsName,pwd);
	    		System.out.println("============"+userName);
	    		if (iRet==0) {
	    			//获取刚刚添加的人员的RTX号码，并将其维护到OA数据库中
	    			info =RtxsvrapiObj.GetUserDetailInfo(userName);
	    			if (info!=null) {
	    				//维护到数据库中去
	    				System.out.println("获取RTX号码成功");
	        			//System.out.println("新添加人员RTX号码："+info[info.length-1][1]);
	        			rtxNum = info[info.length-1][1];
	        			int id=0;
	        			try {
		        			id= this.getNextId();
						} catch (Exception e) {
							e.printStackTrace();
						}

	        			npjt.getJdbcOperations().update("insert into t_rtx_user (id,rtxNum,userName,deptID,chsName,pwd) " +
	        				" values("+id+",'"+rtxNum+"','"+userName+"','"+deptID+"','"+chsName+"','"+pwd+"')");
	        		} else {
	        			System.out.println("获取RTX号码失败");
	        		}
	    		} else {
	    			System.out.println("添加人员失败");
	    		}
		    }	
	    	RtxsvrapiObj.UnInit();
		}
		return iRet;
	}
	
	/**
	 * 删除用户
	 * 返回0 表示成功，其他表示失败
	 */
	public int delUser(String userName) {
		NamedParameterJdbcTemplate npjt = this.getNpjtA();
		int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet =RtxsvrapiObj.deleteUser(userName);
    		if (iRet==0) {
    			System.out.println("删除人员成功");
    			npjt.getJdbcOperations().update("delete from t_rtx_user where userName='"+userName+"'");
    		} else {
    			System.out.println("删除人员失败");
    		}
	    }	
    	RtxsvrapiObj.UnInit();
    	return iRet;
	}
	
	/**
	 * 修改用户资料(简单用户资料)
	 * userName：用户姓名(即英文账号)
	 * ChsName：用户中文名称
	 * pwd：密码
	 * 成功返回0，失败返回其他。
	 */
	public int setUserSimpleInfo(String userName,String ChsName,String pwd) {
		NamedParameterJdbcTemplate npjt = this.getNpjtA();
		int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init()) {   
    		iRet =RtxsvrapiObj.SetUserSimpleInfo(userName,ChsName,"","","","",pwd);
    		if (iRet==0) {
    			System.out.println("设置简单资料成功");
    			npjt.getJdbcOperations().update("update t_rtx_user set chsName='"+ChsName+"',pwd='"+pwd+"' where userName='"+userName+"'");
    		} else {
    			System.out.println("设置简单资料失败");
    		}
	    }	
    	RtxsvrapiObj.UnInit();
    	return iRet;
	}
	
	/**
	 * 删除部门
	 * 	deptId:部门id
	 *  type: type删除类型（0为不删除部门下用户，1为删除部门下用户）
	 * 成功返回0，失败返回其他。
	 */
	public int delDept(String deptId,String type) {
		int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init()) {   
    		iRet = RtxsvrapiObj.deleteDept(deptId,type);
    		if (iRet == 0) {
    			System.out.println("删除部门成功");
    		} else {
    			System.out.println("删除部门失败");
    		}
	    }	
    	RtxsvrapiObj.UnInit();
    	return iRet;
	}
	
	/**
	 * 修改部门
	 * deptId: 部门id
	 * DetpInfo：部门信息
	 * DeptName：部门名称
	 * ParentDeptId：上级部门id
	 */
	public int setDept(String deptId,String DetpInfo,String DeptName,String ParentDeptId) {
		int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init()) {   
    		iRet = RtxsvrapiObj.setDept(deptId,DetpInfo,DeptName,ParentDeptId);
    		if (iRet == 0) {
    			System.out.println("修改部门资料成功");
    		} else {
    			System.out.println("修改部门资料失败");
    		}
	    }	
    	RtxsvrapiObj.UnInit();
    	return iRet;
	}
	
}
