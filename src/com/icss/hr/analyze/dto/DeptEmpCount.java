package com.icss.hr.analyze.dto;
/**
 * 部门员工人数DTO
 * @author Administrator
 *
 */
public class DeptEmpCount {
	
	private String deptName;
	
	private Integer empCount;

	public DeptEmpCount() {
		super();
	}

	public DeptEmpCount(String deptName, Integer empCount) {
		super();
		this.deptName = deptName;
		this.empCount = empCount;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getEmpCount() {
		return empCount;
	}

	public void setEmpCount(Integer empCount) {
		this.empCount = empCount;
	}

	@Override
	public String toString() {
		return "DeptEmpCount [deptName=" + deptName + ", empCount=" + empCount + "]";
	}
	
}
