package com.mee.timed.data;

import com.mee.timed.JobExecutionContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JobExecutionContextImpl
 *
 * @author shaoow
 * @version 1.0
 * @className JobExecutionContextImpl
 * @date 2024/6/7 14:42
 */
public class JobExecutionContextImpl implements JobExecutionContext, java.io.Serializable {

    private static final long serialVersionUID = -8139417614523942021L;

    private Date fireTime;

//    private Date scheduledFireTime;
//
//    private Date prevFireTime;
//
//    private Date nextFireTime;
//
//    private long jobRunTime = -1;
    /**
     * 执行任务用到的数据
     */
    private JobEntity jobInfo;
//    /**
//     * 执行任务用到的数据
//     */
//    private HashMap<String, String> jobData;
    /**
     * 执行任务用到的数据(原始json)
     */
    private String jobDataJson;

    /**
     * 执行任务的结果
     */
    private String jobResult;

    public JobExecutionContextImpl( ) {
    }

    public JobExecutionContextImpl(Date fireTime/*, Date scheduledFireTime, Date prevFireTime, Date nextFireTime, long jobRunTime*/ ) {
        this.fireTime = fireTime;
//        this.scheduledFireTime = scheduledFireTime;
//        this.prevFireTime = prevFireTime;
//        this.nextFireTime = nextFireTime;
//        this.jobRunTime = jobRunTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

//    public void setScheduledFireTime(Date scheduledFireTime) {
//        this.scheduledFireTime = scheduledFireTime;
//    }
//
//    public void setPrevFireTime(Date prevFireTime) {
//        this.prevFireTime = prevFireTime;
//    }
//
//    public void setNextFireTime(Date nextFireTime) {
//        this.nextFireTime = nextFireTime;
//    }
//
//    public void setJobRunTime(long jobRunTime) {
//        this.jobRunTime = jobRunTime;
//    }


    //===== getter =====

    @Override
    public JobEntity getJobInfo() {
        return this.jobInfo;
    }
//
//    @Override
//    public Map<String, String> getJobData() {
//        return this.jobData;
//    }

    @Override
    public String getJobDataJson() {
        return this.jobDataJson;
    }

    @Override
    public String setJobResult() {
        return this.jobResult;
    }
    @Override
    public String getJobResult() {
        return null;
    }
    @Override
    public Date getFireTime() {
        return fireTime;
    }
//    @Override
//    public Date getScheduledFireTime() {
//        return scheduledFireTime;
//    }
//    @Override
//    public Date getPrevFireTime() {
//        return this.prevFireTime;
//    }
//    @Override
//    public Date getNextFireTime() {
//        return nextFireTime;
//    }
//
//    @Override
//    public long getJobRunTime() {
//        return jobRunTime;
//    }

    public JobExecutionContextImpl setJobDataJson(String jobDataJson) {
        this.jobDataJson = jobDataJson;
        return this;
    }

    @Override
    public JobExecutionContext setJobResult(String jobResult) {
        this.jobResult = jobResult;
        return this;
    }

    public JobExecutionContextImpl setJobInfo(JobEntity jobInfo) {
        this.jobInfo = jobInfo;
        return this;
    }

    @Override
    public String toString() {
        return "JobExecutionContextImpl{" +
                "fireTime=" + fireTime +
                ", jobInfo=" + jobInfo +
                ", jobDataJson='" + jobDataJson + '\'' +
                ", jobResult='" + jobResult + '\'' +
                '}';
    }
}
