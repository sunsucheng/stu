package com.icss.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icss.hr.dept.pojo.Dept;
import com.icss.hr.dept.service.DeptService;
/**
 * ≤ø√≈service≤‚ ‘
 * @author Administrator
 *
 */
public class TestDeptService {

	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private DeptService service = context.getBean(DeptService.class);
	
	@Test
	public void testInsert() {
		
		Dept dept = new Dept("bbb","bbb");
		service.addDept(dept);
		
	}
	
	@Test
	public void testquery() {
		
		System.out.println(service.queryDept());
		
	}
	
}
