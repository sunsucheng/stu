package com.icss.hr.pic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.hr.common.Pager;
import com.icss.hr.pic.dao.PicMapper;
import com.icss.hr.pic.pojo.Pic;

/**
 * Õºø‚Service
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class PicService {

	@Autowired
	private PicMapper mapper;
	
	public void addPic(Pic pic) {
		mapper.insert(pic);
	}
	
	public void deletePic(Integer picId)  {
		mapper.delete(picId);
	}
	@Transactional(readOnly=true)
	public List<Pic> queryPicByPage(Pager pager)  {
		return mapper.queryByPage(pager);
	}
	@Transactional(readOnly=true)
	public int getPicCount () {
		return mapper.getCount();
	}
	@Transactional(readOnly=true)
	public Pic queryPicById(Integer picId)  {
		return mapper.queryById(picId);
	}
	
}
