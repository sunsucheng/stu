package com.icss.test;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.Term;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icss.hr.common.Pager;
import com.icss.hr.dept.pojo.Dept;
import com.icss.hr.dept.service.DeptService;
import com.icss.hr.emp.dao.EmpMapper;
import com.icss.hr.emp.index.EmpIndexDao;
import com.icss.hr.emp.pojo.Emp;
import com.icss.hr.emp.service.EmpService;
import com.icss.hr.job.pojo.Job;
/**
 * 成员service测试
 * @author Administrator
 *
 */
public class TestEmpService {

	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private EmpService service = context.getBean(EmpService.class);
	
	private EmpIndexDao indexDao = new EmpIndexDao();
	
	@Test
	public void testInsert() {
		
		Dept dept = new Dept();
		dept.setDeptId(10);
		
		Job job = new Job();
		job.setJobId(1);
		
		Emp emp = new Emp("ttt","ttt","123456","tom@163.com","10086",Date.valueOf("1995-01-01"),2015.0,dept,job,null,"无");
		
		service.addEmp(emp);
		
	}
	
	@Test
	public void testInsertMany() {
		
		Dept dept = new Dept();
		dept.setDeptId(1);
		
		Job job = new Job();
		job.setJobId(3);
		
		for(int i = 3;i <= 20;i ++){
			Emp emp = new Emp("jack" + i,"jack" + i,"123456","jack@163.com","13012345678",Date.valueOf("1995-01-01"),2016.0,dept,job,null,"无");
			
			service.addEmp(emp);
		}
		
		
		
	}
	
	@Test
	public void testDelete() {
		
		service.deleteEmp(141);
		
	}
	
	@Test
	public void testQueryByPage() {
		
		Pager pager = new Pager(service.getEmpCount(), 7, 2);
		List<Emp> list = service.queryEmpByPage(pager);
		
		for(Emp emp : list){
			System.out.println(emp);
		}
		
		
	}
	
	@Test
	public void testGetCount() {
		
		int count = service.getEmpCount();
		System.out.println(count);
		
	}
	
	@Test
	public void testUpdateEmp() {
		Dept dept = new Dept();
		dept.setDeptId(10);
		
		Job job = new Job();
		job.setJobId(1);
		
		Emp emp = new Emp(122, "sss", "sss", "123456", "56789@qq.com","13624050022", Date.valueOf("1995-01-01"), 2015.0, dept, job, null, "aaa");
		
		service.updateEmp(emp);
		
		
	}
	
	@Test
	public void testQueryById() {
		
		Emp emp = new Emp();
		emp = service.queryEmpById(121);
		
		System.out.println(emp);
		
		
	}
	
	@Test
	public void testQueryByName() {
		
		EmpMapper mapper = context.getBean(EmpMapper.class);
		Emp emp = mapper.queryByName("tom");
		System.out.println(emp);
		
		//String empPwd = service.queryEmpPwd("ssc");
		
		//System.out.println(empPwd);
		
		
	}
	
	@Test
	public void testUpdateEmpPwd() {
				
		service.updateEmpPwd("ssc", "666666");
		
		
	}
	
	@Test
	public void testUpdateEmpPic() {
				
		service.updateEmpPic("jack17", "aaa");
		
	}
	
	@Test
	public void testQueryEmpPic() {
				
		String empPic = service.queryEmpPic("tom");
		System.out.println(empPic);
		
	}
	
	@Test
	public void testGetCountByCondition() {
		
		Integer deptId = null;
		Integer jobId = null;
		String empName = null;
		
		int count = service.getEmpCountByCondition(deptId, jobId, empName);
		System.out.println("count=" + count);
		
	}
	
	@Test
	public void testQueryByCondition() {
		
		Integer deptId = null;
		Integer jobId = null;
		String empName = "";
		
		Pager pager = new Pager(service.getEmpCountByCondition(deptId, jobId, empName),7,1);
		
		List<Emp> list = service.queryEmpByCondition(deptId, jobId, empName, pager);
		
		for (Emp emp : list) {
			System.out.println(emp);
		}
		
		
	}
	
	/**
	 * 重建emp表数据的索引
	 * @throws IOException 
	 */
	@Test
	public void createEmpIndex() throws IOException{
		
		Pager pager = new Pager(service.getEmpCount(), service.getEmpCount(), 1);
		
		List<Emp> list = service.queryEmpByPage(pager);
		
		for(Emp emp : list){
			
			Term term = new Term("empId" ,String.valueOf(emp.getEmpId()));
			
			Document document = new Document();
			document.add(new TextField("empId", String.valueOf(emp.getEmpId()),Store.YES));
			document.add(new TextField("empName", emp.getEmpName(),Store.YES));
			document.add(new TextField("empPhone", emp.getEmpPhone(),Store.YES));
			document.add(new TextField("empInfo", emp.getEmpInfo(),Store.YES));
			
			indexDao.update(term, document);
			
		}
	}
	
	
	
}
