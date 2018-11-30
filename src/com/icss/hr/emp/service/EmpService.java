package com.icss.hr.emp.service;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.Term;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.util.Version;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.hr.common.Pager;
import com.icss.hr.emp.dao.EmpMapper;
import com.icss.hr.emp.index.EmpIndexDao;
import com.icss.hr.emp.pojo.Emp;

/**
 * Ա��Service
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmpService {

	@Autowired
	private EmpMapper mapper;
	
	@Autowired
	private EmpIndexDao indexDao;

	/**
	 * ��¼��֤ ����1�û��������� 2������� 3��¼�ɹ�
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public int checkLogin(String empLoginName, String empPwd) {

		Emp emp = mapper.queryByName(empLoginName);

		if (emp == null) {
			return 1;
		} else if (!empPwd.equals(emp.getEmpPwd())) {
			return 2;
		}

		return 3;
	}

	/**
	 * ����û����Ƿ���� ���ڷ���true�������ڷ���false
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public boolean checkLoginName(String empLoginName) {

		Emp emp = mapper.queryByName(empLoginName);

		if (emp == null)
			return false;

		return true;
	}

	/**
	 * ������Ա��
	 * @throws IOException 
	 * 
	 * @throws SQLException
	 */
	public void addEmp(Emp emp) {
		
		//�������ݿ�
		mapper.insert(emp);
		
		try {
			//����Զ����ɵ�id
			int empId = mapper.getLastInsertId();
			System.out.println("empId=" + empId);
			
			//���������ĵ�
			Document document = new Document();
			document.add( new TextField("empId",String.valueOf(empId),Store.YES) );
			document.add( new TextField("empName",emp.getEmpName(),Store.YES) );
			document.add( new TextField("empPhone",emp.getEmpPhone(),Store.YES) );
			document.add( new TextField("empInfo",emp.getEmpInfo(),Store.YES) );
			
			//��������dao
			indexDao.create(document);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * ͨ��id��ѯԱ������
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public Emp queryEmpById(Integer empId) {

		return mapper.queryById(empId);
	}

	/**
	 * ����Ա���ܼ�¼��
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public int getEmpCount() {

		return mapper.getCount();
	}

	/**
	 * ��ҳ��ѯԱ��
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByPage(Pager pager) {

		return mapper.queryByPage(pager);
	}

	/**
	 * �޸�Ա��
	 * 
	 * @throws SQLException
	 */
	public void updateEmp(Emp emp) {
		mapper.update(emp);
		
		//����
		try {
			
			Term term = new Term("empId", String.valueOf(emp.getEmpId()));
			
			
			//���������ĵ�
			Document document = new Document();
			document.add( new TextField("empId",String.valueOf(emp.getEmpId()),Store.YES) );
			document.add( new TextField("empName",emp.getEmpName(),Store.YES) );
			document.add( new TextField("empPhone",emp.getEmpPhone(),Store.YES) );
			document.add( new TextField("empInfo",emp.getEmpInfo(),Store.YES) );
			
			//��������dao
			indexDao.update(term, document);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}

	/**
	 * ɾ��Ա��
	 * 
	 * @throws SQLException
	 */
	public void deleteEmp(Integer empId) {
		mapper.delete(empId);
	}

	/**
	 * ����ͷ��
	 * 
	 * @throws SQLException
	 */
	public void updateEmpPic(String empLoginName, String empPic) {
		mapper.updateEmpPic(empLoginName, empPic);
	}

	/**
	 * ��ѯ����ͷ��
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public String queryEmpPic(String empLoginName) {

		return mapper.queryEmpPic(empLoginName);
	}

	/**
	 * �޸�����
	 * 
	 * @throws SQLException
	 */
	public void updateEmpPwd(String empLoginName, String empPwd) {
		mapper.updateEmpPwd(empLoginName, empPwd);
	}

	/**
	 * ��ѯ���ص�ǰ����
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public String queryEmpPwd(String empLoginName) {

		return mapper.queryByName(empLoginName).getEmpPwd();
	}

	/**
	 * ���������������ܼ�¼��
	 */
	@Transactional(readOnly = true)
	public int getEmpCountByCondition(Integer deptId, Integer jobId, String empName) {
		return mapper.getCountByCondition(deptId, jobId, empName);
	}

	/**
	 * ��������
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByCondition(Integer deptId, Integer jobId, String empName, Pager pager) {
		return mapper.queryByCondition(deptId, jobId, empName, pager);
	}

	/**
	 * ����excel����
	 * @throws IOException 
	 */
	@Transactional(readOnly = true)
	public void writeExcel(List<Emp> list, OutputStream out) throws IOException {

		// ������
		HSSFWorkbook wb = new HSSFWorkbook();

		// ������
		HSSFSheet sheet = wb.createSheet("Ա������");

		// ������
		HSSFRow titleRow = sheet.createRow(0);

		titleRow.createCell(0).setCellValue("Ա�����");
		titleRow.createCell(1).setCellValue("Ա������");
		titleRow.createCell(2).setCellValue("Ա������");
		titleRow.createCell(3).setCellValue("�绰");
		titleRow.createCell(4).setCellValue("����");
		titleRow.createCell(5).setCellValue("ְ��");
		
		//������
		for (int i = 1;i <= list.size();i ++) {
			Emp emp = list.get(i - 1);
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(emp.getEmpId());
			row.createCell(1).setCellValue(emp.getEmpName());
			row.createCell(2).setCellValue(emp.getEmpEmail());
			row.createCell(3).setCellValue(emp.getEmpPhone());
			row.createCell(4).setCellValue(emp.getDept().getDeptName());
			row.createCell(5).setCellValue(emp.getJob().getJobName());			
		}
		
		//д��������
		wb.write(out);
	}
	
	/**
	 * ȫ�ļ���
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws InvalidTokenOffsetsException 
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByIndex(String queryStr) throws ParseException, IOException, InvalidTokenOffsetsException {
		
		// ���ķִ���
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
		
		//������ѯ���������󣬶��ֶ�����
		QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_47,new String[]{"empName","empPhone","empInfo"},analyzer);
		
		//������ѯ����
		Query query = queryParser.parse(queryStr);
		
		//��������dao
		List<Emp> list = indexDao.search(query);
		
		return list;
	}

}