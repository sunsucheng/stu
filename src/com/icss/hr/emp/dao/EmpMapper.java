package com.icss.hr.emp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.hr.common.Pager;
import com.icss.hr.emp.pojo.Emp;

/**
 * 员工的dao接口
 * @author Administrator
 *
 */
public interface EmpMapper {

	void insert(Emp emp);
	
	void delete(Integer empId);
	
	List<Emp> queryByPage(Pager pager);
	
	void update(Emp emp);
	
	Emp queryById(Integer empId);
	
	int getCount();
	
	Emp queryByName(String empLoginName);
	
	void updateEmpPwd(@Param("empLoginName")String empLoginName,@Param("empPwd")String empPwd);
	
	void updateEmpPic(@Param("empLoginName")String empLoginName,@Param("empPic")String empPic);
	
	String queryEmpPic(String empLoginName);
	
	/**
	 *通过部门，职务，姓名检索员工 
	 */
	List<Emp> queryByCondition(@Param("deptId")Integer deptId,@Param("jobId")Integer jobId,@Param("empName")String empName,@Param("pager")Pager pager);
	
	/**
	 * 满足查询条件的总记录数
	 */
	int getCountByCondition(@Param("deptId")Integer deptId,@Param("jobId")Integer jobId,@Param("empName")String empName);
	
	/**
	 * 获得最后一次插入行的自动编号
	 */
	int getLastInsertId();
	
}
