package com.icss.hr.job.pojo;

/**
 *  µÃÂ¿‡
 * @author Administrator
 *
 */
public class Job {

	private Integer jobId;
	
	private String jobName;
	
	private Integer jobMinCls;
	
	private Integer jobMaxCls;

	public Job() {
		super();
	}

	public Job(Integer jobId, String jobName, Integer jobMinCls, Integer jobMaxCls) {
		super();
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobMinCls = jobMinCls;
		this.jobMaxCls = jobMaxCls;
	}

	public Job(String jobName, Integer jobMinCls, Integer jobMaxCls) {
		super();
		this.jobName = jobName;
		this.jobMinCls = jobMinCls;
		this.jobMaxCls = jobMaxCls;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Integer getJobMinCls() {
		return jobMinCls;
	}

	public void setJobMinCls(Integer jobMinCls) {
		this.jobMinCls = jobMinCls;
	}

	public Integer getJobMaxCls() {
		return jobMaxCls;
	}

	public void setJobMaxCls(Integer jobMaxCls) {
		this.jobMaxCls = jobMaxCls;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobName=" + jobName + ", jobMinCls=" + jobMinCls + ", jobMaxCls=" + jobMaxCls
				+ "]";
	}
	
	
	
}
