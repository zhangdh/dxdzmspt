package com.coffice.util.excel;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/excelupload/default.do")
public class ExcelUploadController extends BaseUtil{
	@RequestMapping(params = "method=list")
	public ModelAndView list(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.list());
	}
	
	@RequestMapping(params = "method=query")
	public ModelAndView query(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.query());
	}
	
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.save());
	}
	
	@RequestMapping(params = "method=show")
	public ModelAndView show(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.show());
	}
	
	@RequestMapping(params = "method=update")
	public ModelAndView update(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.update());
	}
	
	@RequestMapping(params = "method=delete")
	public ModelAndView delete(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", excelUpload.delete());
	}
	
	@RequestMapping(params = "method=saveexcelupload")
	public ModelAndView saveExcelUpload(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		Map _map = excelUpload.saveExcelUpload(request);		
		/*ExcelImport excelImport = new ExcelImport(RequestUtil.getMap(request));
		Map _map = excelImport.saveExcelUpload(request);*/
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
	@RequestMapping(params = "method=saveexcelupload2")
	public ModelAndView saveExcelUpload2(HttpServletRequest request) {
		//ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		//Map _map = excelUpload.saveExcelUpload(request);		
		ExcelImport excelImport = new ExcelImport(RequestUtil.getMap(request));
		Map _map = excelImport.saveExcelUpload(request);
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
	@RequestMapping(params = "method=resubexceldata2")
	public ModelAndView reSubExcelData2(HttpServletRequest request) {
		/*ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		Map _map = excelUpload.reSubExcelData(request);*/
		
		ExcelImport excelImport = new ExcelImport(RequestUtil.getMap(request));
		Map _map = excelImport.reSubExcelData(request);
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
	@RequestMapping(params = "method=resubexceldata")
	public ModelAndView reSubExcelData(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		Map _map = excelUpload.reSubExcelData(request);
		
		/*ExcelImport excelImport = new ExcelImport(RequestUtil.getMap(request));
		Map _map = excelImport.reSubExcelData(request);*/
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
	@RequestMapping(params = "method=subagodata")
	public ModelAndView subAgoData(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		Map _map = excelUpload.subAgoData(request);
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
	
	@RequestMapping(params = "method=canceldata")
	public ModelAndView cancelData(HttpServletRequest request) {
		ExcelUpload excelUpload = new ExcelUpload(RequestUtil.getMap(request));
		Map _map = excelUpload.cancelData(request);
		return new ModelAndView("/excel/excelUploadResult.jsp", _map);
	}
}
