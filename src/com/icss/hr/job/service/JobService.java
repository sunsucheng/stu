package com.icss.hr.job.service;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.hr.job.dao.JobMapper;
import com.icss.hr.job.pojo.Job;

@Service
@Transactional(rollbackFor=Exception.class)
public class JobService {

	@Autowired
	private JobMapper mapper ;
	
	public void addJob(Job job){
		mapper.insert(job);
	}
	
	public void updateJob(Job job) {
		mapper.update(job);
	}
	
	public void deleteJob(Integer jobId)  {
		mapper.delete(jobId);
	}
	@Transactional(readOnly=true)
	public Job queryJobById(Integer jobId)  {
		return mapper.queryById(jobId);
	}
	@Transactional(readOnly=true)
	public List<Job> queryJob() {
		return mapper.query();
	}
	/**
	 * 根据传入的输出流，把部门表数据转换为excel文件数据
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void writeJobExcel(OutputStream out) throws IOException, SQLException {
		
		//返回职务数据集合
		List<Job>list = mapper.query();
		
		//工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		
		//工作表
		HSSFSheet sheet = wb.createSheet("职务数据");
		
		//标题行
		HSSFRow titleRow = sheet.createRow(0);
		
		titleRow.createCell(0).setCellValue("职务编号");
		titleRow.createCell(1).setCellValue("职务名称");
		titleRow.createCell(2).setCellValue("最低年级");
		titleRow.createCell(3).setCellValue("最高年级");
		
		//数据行
		for(int i = 1;i <= list.size();i ++){
			
			Job job = list.get(i - 1);
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(job.getJobId());
			row.createCell(1).setCellValue(job.getJobName());
			row.createCell(2).setCellValue(job.getJobMinCls());
			row.createCell(3).setCellValue(job.getJobMaxCls());
			
		}
		wb.write(out);
		
	}
	
	
}
