package com.icss.hr.perm.dao;

import java.util.List;
import java.util.Map;

/**
 * 角色dao
 * @author Administrator
 *
 */
public interface RoleMapper {
    
	/**
	 * 根据登录名查询返回对应角色名称
	 */
	List<Map<String, Object>> queryRole(String empLoginName);
	
	
	
}