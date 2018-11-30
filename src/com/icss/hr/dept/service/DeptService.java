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
 * ����Service
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
	 * ���ݴ������������Ѳ��ű�����ת��Ϊexcel�ļ�����
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void writeExcel(OutputStream out) throws SQLException, IOException{
		
		//���ز������ݼ���
		List<Dept> list = mapper.query();
		
		//������
		HSSFWorkbook wb = new HSSFWorkbook();
		
		//������
		HSSFSheet sheet = wb.createSheet("��������");
		
		//������
		HSSFRow titleRow = sheet.createRow(0);
		
		titleRow.createCell(0).setCellValue("���ű��");
		titleRow.createCell(1).setCellValue("��������");
		titleRow.createCell(2).setCellValue("���ŵ�ַ");
		
		//������
		for(int i = 1;i <= list.size();i ++){
			Dept dept = list.get(i - 1);
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(dept.getDeptId());
			row.createCell(1).setCellValue(dept.getDeptName());
			row.createCell(2).setCellValue(dept.getDeptLoc());
			
		}
		//д��������
		wb.write(out);
		
		
	}
	
}
