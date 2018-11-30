package com.icss.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.FileCopyUtils;

import com.icss.hr.common.Pager;
import com.icss.hr.pic.pojo.Pic;
import com.icss.hr.pic.service.PicService;

/**
 * Õºø‚service≤‚ ‘
 * @author Administrator
 *
 */
public class TestPicService {

	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private PicService service = context.getBean(PicService.class);
	
	@Test
	public void testInsert() throws IOException{
		
		File file = new File("d://1.jpg");
		
		byte[] picData = FileCopyUtils.copyToByteArray(file);
		
		Pic pic = new Pic(file.getName(), "Œ‚“‡∑≤", file.length(), "tom", picData, null);
		
		service.addPic(pic);
	}
	
	@Test
	public void testGetCount(){
		
		int count = service.getPicCount();
		System.out.println(count);
		
	}
	
	@Test
	public void testQueryByPage(){
		
		Pager pager = new Pager(service.getPicCount(), 10, 1);
		List<Pic> list = service.queryPicByPage(pager);
		
		for(Pic  pic : list){
			System.out.println(pic);
		}
		
		
	}
	
	@Test
	public void testQueryById() throws IOException{
		
		Pic pic = service.queryPicById(35);
		
		File file = new File("d:\\333.jpg");
		
		FileCopyUtils.copy(pic.getPicData(), file);
		
		
	}
	
}
