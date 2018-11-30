package com.icss.hr.dept.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.hr.dept.pojo.Dept;
import com.icss.hr.dept.service.DeptService;

/**
 * ���ſ�����
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/dept")
public class DeptController {
	
	@Autowired
	private DeptService service;
		
	@RequestMapping("/add")
	public void add(Dept dept,HttpServletRequest request,HttpServletResponse response) {
		service.addDept(dept);		
	}
	
	@RequestMapping("/query")
	@ResponseBody
	public List<Dept> query(HttpServletRequest request,HttpServletResponse response) {
		return service.queryDept();
	}
	
	@RequestMapping("/delete")
	public void delete(Integer deptId,HttpServletRequest request,HttpServletResponse response) {
		service.deleteDept(deptId);		
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public Dept get(Integer deptId,HttpServletRequest request,HttpServletResponse response) {
		return service.queryDeptById(deptId);
	}
	
	@RequestMapping("/update")
	public void update(Dept dept,HttpServletRequest request,HttpServletResponse response) {
		service.updateDept(dept);		
	}
	
	@RequestMapping("/writeExcel")
	public void writeExcel(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
		
		//�����ļ���ת��
		String filename = new String("ְ������.xls".getBytes(),"iso-8859-1");
		
		//����������Ը�����ʽ�������ݣ������ļ���
		response.setHeader("content-disposition", "attachment;filename=" + filename);
		
		//������Ӧ����Ϊ����������
		response.setContentType("application/octet-stream");
		
		//�����Ӧ�����������
		OutputStream out = response.getOutputStream();
		
		service.writeExcel(out);
	}

	
}