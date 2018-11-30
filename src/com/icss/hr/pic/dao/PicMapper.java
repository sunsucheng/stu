package com.icss.hr.pic.dao;

import java.util.List;

import com.icss.hr.common.Pager;
import com.icss.hr.pic.pojo.Pic;

/**
 * Í¼¿âdao
 * @author Administrator
 *
 */
public interface PicMapper {

	void insert(Pic pic);
	
	void delete(Integer picId);
	
	List<Pic> queryByPage(Pager pager);
	
	int getCount();
	
	Pic queryById(Integer picId);
	
}
