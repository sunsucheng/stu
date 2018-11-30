package com.icss.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icss.hr.analyze.dto.DeptEmpCount;
import com.icss.hr.analyze.service.AnaService;
/**
 * Êý¾Ý·ÖÎöservice²âÊÔ
 * @author Administrator
 *
 */
public class TestAnaService {

	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private AnaService service = context.getBean(AnaService.class);
	
	@Test
	public void testQueryEmpCount() {
		
		List<DeptEmpCount> list = service.queryEmpCount();
		
		for(DeptEmpCount item : list){
			System.out.println(item);
		}
		
		
	}
	
	@Test
	public void testQueryAvgCls() {
		
		List<HashMap<String, Object>> list = service.queryAvgCls();
		
		for(HashMap<String, Object> item : list){
			System.out.println(item);
		}
		
		
	}
	
	@Test
	public void testQueryMinMaxCls() {
		
		List<HashMap<String, Object>> list = service.queryDeptMinMaxClass();
		
		for(HashMap<String, Object> item : list){
			System.out.println(item);
		}
		
		
	}
	
	@Test
	public void testQueryDeptInfo() {
		
		List<HashMap<String, Object>> list = service.QueryDeptInfo();
		
		for(HashMap<String, Object> item : list){
			System.out.println(item);
		}
		
		
	}
	
}
