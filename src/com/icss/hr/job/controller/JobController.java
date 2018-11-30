package com.icss.hr.job.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.hr.job.pojo.Job;
import com.icss.hr.job.service.JobService;

/**
 * ְ�������
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService service;
	
	@RequestMapping("/add")
	public void add(Job job,HttpServletRequest request,HttpServletResponse response){
		service.addJob(job);
	}
	
	@RequestMapping("/update")
	public void update(Job job,HttpServletRequest request,HttpServletResponse response){
		service.updateJob(job);
	}
	
	@RequestMapping("/delete")
	public void delete(Integer jobId,HttpServletRequest request,HttpServletResponse response){
		service.deleteJob(jobId);
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public Job get(Integer jobId,HttpServletRequest request,HttpServletResponse response){
		return service.queryJobById(jobId);
	}
	
	@RequestMapping("/query")
	@ResponseBody
	public List<Job> query(HttpServletRequest request,HttpServletResponse response){
		return service.queryJob();
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
		
		service.writeJobExcel(out);
	}
	
	
}
