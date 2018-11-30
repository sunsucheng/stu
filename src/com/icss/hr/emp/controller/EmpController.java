package com.icss.hr.emp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.hr.common.Pager;
import com.icss.hr.emp.pojo.Emp;
import com.icss.hr.emp.service.EmpService;

/**
 * 成员控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	private EmpService service;
	
	@RequestMapping("/checkLoginName")
	@ResponseBody
	public String checkLoginName(String empLoginName,HttpServletRequest request,HttpServletResponse response){
		
		return String.valueOf(service.checkLoginName(empLoginName));
	}
	
	@RequestMapping("/add")	
	public void add(Emp emp,HttpServletRequest request,HttpServletResponse response){
		//密码加密，SHA256
		String newPwd = new Sha256Hash(emp.getEmpPwd()).toHex();
		emp.setEmpPwd(newPwd);
		service.addEmp(emp);			
	}
	
	@RequestMapping("/query")	
	@ResponseBody
	public HashMap<String, Object> query(int pageNum,int pageSize,HttpServletRequest request,HttpServletResponse response){
		
		Pager pager = new Pager(service.getEmpCount(), pageSize,pageNum);
		
		List<Emp> list = service.queryEmpByPage(pager);
		
		HashMap<String, Object> map = new HashMap<>();		
		map.put("pager", pager);
		map.put("list", list);
				
		return map;
	}
	
	@RequestMapping("/delete")	
	public void delete(Integer empId,HttpServletRequest request,HttpServletResponse response){
		
		service.deleteEmp(empId);		
	}
	
	@RequestMapping("/get")	
	@ResponseBody
	public Emp get(Integer empId,HttpServletRequest request,HttpServletResponse response){
		
		return service.queryEmpById(empId);		
	}
	
	
	@RequestMapping("/update")	
	public void update(Emp emp,HttpServletRequest request,HttpServletResponse response){
		
		service.updateEmp(emp);	
	}
	
	@RequestMapping("/login")	
	@ResponseBody
	public int login(String empLoginName,String empPwd,HttpServletRequest request,HttpServletResponse response){
		
		//进行SHA256加密
		empPwd = new Sha256Hash(empPwd).toHex();
		
		//封装用户名和密码
		UsernamePasswordToken upToken = new UsernamePasswordToken(empLoginName, empPwd);
		
		//设置rememberme（以cookie形式把用户名写入客户端）
		upToken.setRememberMe(true);
		
		//Shiro登录
		Subject subject = SecurityUtils.getSubject();
		
		try {
			subject.login(upToken);
		} catch (UnknownAccountException e) {
			return 1;
		} catch (IncorrectCredentialsException e){
			return 2;
		}
		
		//在session中存储登录标识
		HttpSession session = request.getSession();
		session.setAttribute("empLoginName", empLoginName);
		
		return 3;		
	}
	
	@RequestMapping("/getEmpPic")
	@ResponseBody
	public String getEmpPic(HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		String empLoginName = (String) session.getAttribute("empLoginName");
		
		String empPic = service.queryEmpPic(empLoginName);
		
		return empPic;
	}
	
	@RequestMapping("/updateEmpPic")	
	public void updateEmpPic(String empPic,HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		String empLoginName = (String) session.getAttribute("empLoginName");
		
		service.updateEmpPic(empLoginName, empPic);
	}
	
	@RequestMapping("/checkOldPwd")	
	@ResponseBody
	public String checkOldPwd(String oldPwd,HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		String empLoginName = (String) session.getAttribute("empLoginName");
		
		//进行SHA256加密
		oldPwd = new Sha256Hash(oldPwd).toHex();
		
		String empPwd = service.queryEmpPwd(empLoginName);
		
		if (oldPwd.equals(empPwd)) {
			return "yes";
		}else {
			return "no";
		}
	}
	
	@RequestMapping("/updateEmpPwd")

	public void updateEmpPwd(String empPwd,HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		String empLoginName = (String) session.getAttribute("empLoginName");
		
		//进行SHA256加密
		empPwd = new Sha256Hash(empPwd).toHex();
		
		service.updateEmpPwd(empLoginName, empPwd);			
	}
	
	@RequestMapping("/queryByCondition")	
	@ResponseBody
	public HashMap<String, Object> queryByCondition(String deptId,String jobId,String empName,int pageNum,int pageSize,HttpServletRequest request,HttpServletResponse response){
		
		Integer deptIdInt = null;
		
		try {
			deptIdInt = Integer.parseInt(deptId);
		} catch (Exception e) {
			
		}
		
		Integer jobIdInt = null;
		
		try {
			jobIdInt = Integer.parseInt(jobId);
		} catch (Exception e) {
			
		}
		
		Pager pager = new Pager(service.getEmpCountByCondition(deptIdInt, jobIdInt, empName), pageSize,pageNum);
		
		List<Emp> list = service.queryEmpByCondition(deptIdInt, jobIdInt, empName, pager);
		
		HashMap<String, Object> map = new HashMap<>();		
		map.put("pager", pager);
		map.put("list", list);
				
		return map;
	}
	
	/**
	 * 下载excel报表
	 * @throws IOException 
	 */
	@RequestMapping("/writeExcel")
	public void writeExcele(String deptId,String jobId,String empName,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Integer deptIdInt = null;
		
		try {
			deptIdInt = Integer.parseInt(deptId);
		} catch (Exception e) {
			
		}
		
		Integer jobIdInt = null;
		
		try {
			jobIdInt = Integer.parseInt(jobId);
		} catch (Exception e) {
			
		}
		
		int count = service.getEmpCountByCondition(deptIdInt, jobIdInt, empName);//满足条件的总记录数
		
		Pager pager = new Pager(count, count,1);
		
		List<Emp> list = service.queryEmpByCondition(deptIdInt, jobIdInt, empName, pager);
		
		//中文文件名转码
		String filename = new String("员工数据.xls".getBytes(),"iso-8859-1");
		
		//设置浏览器以附件形式接收数据（下载文件）
		response.setHeader("content-disposition", "attachment;filename=" + filename);
		
		//设置响应类型为二进制类型
		response.setContentType("application/octet-stream");
		
		//获得响应二进制输出流
		OutputStream out = response.getOutputStream();
		
		service.writeExcel(list,out);
	}
	
	/**
	 * 全文检索成员
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws InvalidTokenOffsetsException 
	 */
	@RequestMapping("/queryByIndex")
	@ResponseBody
	public List<Emp> queryByIndex(String queryStr, HttpServletRequest request,HttpResponse response) throws ParseException, IOException, InvalidTokenOffsetsException {
		return service.queryEmpByIndex(queryStr);
	}
	
}
