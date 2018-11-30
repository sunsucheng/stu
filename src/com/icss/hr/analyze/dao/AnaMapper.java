package com.icss.hr.analyze.dao;

import java.util.HashMap;
import java.util.List;

import com.icss.hr.analyze.dto.DeptEmpCount;

/**
 * Êý¾Ý·ÖÎödao
 * @author Administrator
 *
 */
public interface AnaMapper {

	List<DeptEmpCount> queryEmpCount();
	
	List<HashMap<String, Object>> queryAvgCls();
	
	List<HashMap<String, Object>> queryMinMaxCls();
	
	List<HashMap<String, Object>> queryDeptInfo();
	
}
