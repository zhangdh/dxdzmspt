package com.coffice.util.excel;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * 数据导出公共类
 * @author 王艳 wywhwn@163.com
 * @version 1.0 2012/2/24
 * 适用于将数据以Excel方式导出
 * 中国联通集成公司版权所有.
 */
@SuppressWarnings("unchecked")

public class Export {
	/**
	 * Excel导出
	 * @param list 源数据
	 * @param title Excel对应列代码和注释（字段名：注释）
	 * @param filename Excel目标文件名 
	 * @param response 服务器端响应
	 */
    public static void exportExcel(List<Map> list,String title,String filename,HttpServletResponse response)
    {
    	 OutputStream outputstream=null;
    	 jxl.write.WritableWorkbook wwb =null;
    	 String[] titleary=null;
    	 String[] titlename=null;
    	 String[] columnid=null;
    	 try{
    		   if(title.equals("")){
    			   throw new Exception("filename不能为空！");
    		   }
    		      
	    		titleary=title.split(",");
	    		columnid=new String[titleary.length];
	    		titlename=new String[titleary.length];
	    		for(int j=0;j<titleary.length;j++){
	    			if(titleary[j]!=null&&!titleary[j].equals("")){
	    				String curtitle=titleary[j];
	    				if(curtitle.indexOf(":")<0){
	    					throw new Exception("title格式不正确");
	    				}
	    				columnid[j]=curtitle.split(":")[0];
	    				titlename[j]=curtitle.split(":")[1];
	    				
	    			}
	    			
	    		}
	    		String filenameconvert = new String(filename.getBytes(),"iso-8859-1");
			    response.setCharacterEncoding("utf-8");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","attachment; filename="+filenameconvert+".xls");
				outputstream=response.getOutputStream();
				wwb = jxl.Workbook.createWorkbook(outputstream); 
	    		jxl.write.WritableSheet sheet = wwb.createSheet(filename, 0);
	    		int index =0;
	    		for(int j=0;j<titlename.length;j++){
	    			jxl.write.Label lable = new jxl.write.Label(j, index, titlename[j]); 
    				sheet.addCell(lable);
	    		}
	    		index=index+1;
	    		for(Map map:list){
	    			for(int i=0;i<columnid.length;i++){
	    				jxl.write.Label lable = new jxl.write.Label(i, index, map.get(columnid[i]).toString()); 
	    				sheet.addCell(lable);
	    			}
	    		index++;
	    		}
	    	    // 写入数据 
	    	    wwb.write(); 
	    		wwb.close();
	    		outputstream.close();
	    	    wwb=null;
	    	    outputstream=null;
	    		}catch(Exception ex){
	    			ex.printStackTrace();
	    			
	    		}
	    		finally{
	    			if(wwb!=null){
	    				try {
							wwb.close();
							wwb=null;
						} catch (Exception e) {
							e.printStackTrace();
						}
	    			}
	    			if(outputstream!=null){
	    				try {outputstream.close();
	    					outputstream=null;
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
        }
     }
     
}
