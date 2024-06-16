package com.mee.timed;

import java.util.Date;
import java.util.Map;

/**
 * JobExecutionContext
 *
 * @author shaoow
 * @version 1.0
 * @className JobExecutionContext
 * @date 2024/6/7 13:43
 */
public interface JobExecutionContext {

    /**
     * 获取任务信息
     * @return Map
     */
    Map<String,Object> getJobInfo();

    /**
     * 获取任务数据
     * @return Map
     */
    Map<String,String> getJobData();

    /**
     * 获取原始任务数据json形式
     * @return
     */
    String getJobDataJson();

    /**
     * 设置执行结果
     * @return String
     */
    String setJobResult();
    /**
     * 获取执行结果
     * @return String
     */
    String getJobResult();


    Date getFireTime() ;

    Date getScheduledFireTime();

    Date getPrevFireTime();

    Date getNextFireTime() ;

    long getJobRunTime() ;


    
}
