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
 * 职务控制器
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
		
		//中文文件名转码
		String filename = new String("职务数据.xls".getBytes(),"iso-8859-1");
		
		//设置浏览器以附件形式接收数据（下载文件）
		response.setHeader("content-disposition", "attachment;filename=" + filename);
		
		//设置响应类型为二进制类型
		response.setContentType("application/octet-stream");
		
		//获得响应二进制输出流
		OutputStream out = response.getOutputStream();
		
		service.writeJobExcel(out);
	}
	
	
}
