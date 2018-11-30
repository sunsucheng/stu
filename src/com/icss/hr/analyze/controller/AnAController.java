package com.icss.hr.analyze.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.hr.analyze.dto.DeptEmpCount;
import com.icss.hr.analyze.service.AnaService;

/**
 * 数据分析控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/ana")
public class AnAController {
	
	@Autowired
	private AnaService service;
	
	@RequestMapping("/queryEmpCount")
	@ResponseBody
	public List<DeptEmpCount> queryEmpCount(HttpServletRequest request,HttpServletResponse response) {
		
		return service.queryEmpCount();
	}
	
	@RequestMapping("/queryAvgCls")
	@ResponseBody
	public List<HashMap<String, Object>> queryAvgCls(HttpServletRequest request,HttpServletResponse response) {
		
		return service.queryAvgCls();
	}	
	
	@RequestMapping("/queryDeptMinMaxClass")
	@ResponseBody
	public List<HashMap<String, Object>> queryDeptMinMaxClass(HttpServletRequest request,HttpServletResponse response) {
		
		return service.queryDeptMinMaxClass();
	}
	
//	@RequestMapping("/queryDeptInfo")
//	@ResponseBody
//	public List<HashMap<String, Object>> queryDeptInfo(HttpServletRequest request,HttpResponse response) {
//		return service.QueryDeptInfo();
//	}
	
}
