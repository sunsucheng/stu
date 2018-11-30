package com.icss.test;
/**
 * ְ��service����
 * @author Administrator
 *
 */

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icss.hr.job.pojo.Job;
import com.icss.hr.job.service.JobService;

public class TestJobService {

	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private JobService service = context.getBean(JobService.class);
	
	@Test
	public void testInsert() {
		
		Job job = new Job("���",2018,2017);
		service.addJob(job);
		
	}
			
	
}
