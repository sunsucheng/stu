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
 * 员工Service
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
	 * 登录验证 返回1用户名不存在 2密码错误 3登录成功
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
	 * 检查用户名是否存在 存在返回true，不存在返回false
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
	 * 增加新员工
	 * @throws IOException 
	 * 
	 * @throws SQLException
	 */
	public void addEmp(Emp emp) {
		
		//插入数据库
		mapper.insert(emp);
		
		try {
			//获得自动生成的id
			int empId = mapper.getLastInsertId();
			System.out.println("empId=" + empId);
			
			//创建索引文档
			Document document = new Document();
			document.add( new TextField("empId",String.valueOf(empId),Store.YES) );
			document.add( new TextField("empName",emp.getEmpName(),Store.YES) );
			document.add( new TextField("empPhone",emp.getEmpPhone(),Store.YES) );
			document.add( new TextField("empInfo",emp.getEmpInfo(),Store.YES) );
			
			//调用索引dao
			indexDao.create(document);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * 通过id查询员工数据
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public Emp queryEmpById(Integer empId) {

		return mapper.queryById(empId);
	}

	/**
	 * 返回员工总记录数
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public int getEmpCount() {

		return mapper.getCount();
	}

	/**
	 * 分页查询员工
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByPage(Pager pager) {

		return mapper.queryByPage(pager);
	}

	/**
	 * 修改员工
	 * 
	 * @throws SQLException
	 */
	public void updateEmp(Emp emp) {
		mapper.update(emp);
		
		//索引
		try {
			
			Term term = new Term("empId", String.valueOf(emp.getEmpId()));
			
			
			//创建索引文档
			Document document = new Document();
			document.add( new TextField("empId",String.valueOf(emp.getEmpId()),Store.YES) );
			document.add( new TextField("empName",emp.getEmpName(),Store.YES) );
			document.add( new TextField("empPhone",emp.getEmpPhone(),Store.YES) );
			document.add( new TextField("empInfo",emp.getEmpInfo(),Store.YES) );
			
			//调用索引dao
			indexDao.update(term, document);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}

	/**
	 * 删除员工
	 * 
	 * @throws SQLException
	 */
	public void deleteEmp(Integer empId) {
		mapper.delete(empId);
	}

	/**
	 * 更新头像
	 * 
	 * @throws SQLException
	 */
	public void updateEmpPic(String empLoginName, String empPic) {
		mapper.updateEmpPic(empLoginName, empPic);
	}

	/**
	 * 查询返回头像
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public String queryEmpPic(String empLoginName) {

		return mapper.queryEmpPic(empLoginName);
	}

	/**
	 * 修改密码
	 * 
	 * @throws SQLException
	 */
	public void updateEmpPwd(String empLoginName, String empPwd) {
		mapper.updateEmpPwd(empLoginName, empPwd);
	}

	/**
	 * 查询返回当前密码
	 * 
	 * @throws SQLException
	 */
	@Transactional(readOnly = true)
	public String queryEmpPwd(String empLoginName) {

		return mapper.queryByName(empLoginName).getEmpPwd();
	}

	/**
	 * 返回满足条件的总记录数
	 */
	@Transactional(readOnly = true)
	public int getEmpCountByCondition(Integer deptId, Integer jobId, String empName) {
		return mapper.getCountByCondition(deptId, jobId, empName);
	}

	/**
	 * 条件搜索
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByCondition(Integer deptId, Integer jobId, String empName, Pager pager) {
		return mapper.queryByCondition(deptId, jobId, empName, pager);
	}

	/**
	 * 下载excel报表
	 * @throws IOException 
	 */
	@Transactional(readOnly = true)
	public void writeExcel(List<Emp> list, OutputStream out) throws IOException {

		// 工作簿
		HSSFWorkbook wb = new HSSFWorkbook();

		// 工作表
		HSSFSheet sheet = wb.createSheet("员工数据");

		// 标题行
		HSSFRow titleRow = sheet.createRow(0);

		titleRow.createCell(0).setCellValue("员工编号");
		titleRow.createCell(1).setCellValue("员工姓名");
		titleRow.createCell(2).setCellValue("员工邮箱");
		titleRow.createCell(3).setCellValue("电话");
		titleRow.createCell(4).setCellValue("部门");
		titleRow.createCell(5).setCellValue("职务");
		
		//数据行
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
		
		//写入数据流
		wb.write(out);
	}
	
	/**
	 * 全文检索
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws InvalidTokenOffsetsException 
	 */
	@Transactional(readOnly = true)
	public List<Emp> queryEmpByIndex(String queryStr) throws ParseException, IOException, InvalidTokenOffsetsException {
		
		// 中文分词器
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
		
		//创建查询解析器对象，多字段搜索
		QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_47,new String[]{"empName","empPhone","empInfo"},analyzer);
		
		//创建查询对象
		Query query = queryParser.parse(queryStr);
		
		//调用索引dao
		List<Emp> list = indexDao.search(query);
		
		return list;
	}

}