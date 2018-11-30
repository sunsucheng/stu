package com.icss.hr.dept.service;

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

import com.icss.hr.dept.dao.DeptMapper;
import com.icss.hr.dept.pojo.Dept;

/**
 * 部门Service
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class DeptService {
	
	@Autowired
	private DeptMapper mapper;
	
	public void addDept(Dept dept){
		mapper.insert(dept);;
	}
	
	public void updateDept(Dept dept){
		mapper.update(dept);
	}
	
	public void deleteDept(Integer deptId){
		mapper.delete(deptId);
	}
	
	@Transactional(readOnly=true)
	public Dept queryDeptById(Integer deptId){
		return mapper.queryById(deptId);
	}
	@Transactional(readOnly=true)
	public List<Dept> queryDept(){
		return mapper.query();
	}
	
	/**
	 * 根据传入的输出流，把部门表数据转换为excel文件数据
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void writeExcel(OutputStream out) throws SQLException, IOException{
		
		//返回部门数据集合
		List<Dept> list = mapper.query();
		
		//工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		
		//工作表
		HSSFSheet sheet = wb.createSheet("部门数据");
		
		//标题行
		HSSFRow titleRow = sheet.createRow(0);
		
		titleRow.createCell(0).setCellValue("部门编号");
		titleRow.createCell(1).setCellValue("部门名称");
		titleRow.createCell(2).setCellValue("部门地址");
		
		//数据行
		for(int i = 1;i <= list.size();i ++){
			Dept dept = list.get(i - 1);
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(dept.getDeptId());
			row.createCell(1).setCellValue(dept.getDeptName());
			row.createCell(2).setCellValue(dept.getDeptLoc());
			
		}
		//写入数据流
		wb.write(out);
		
		
	}
	
}
