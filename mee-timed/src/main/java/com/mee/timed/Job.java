package com.mee.timed;

/**
 * Job
 *
 * @author shaoow
 * @version 1.0
 * @className Job
 * @date 2024/6/7 13:42
 */
public interface Job {

    void execute(JobExecutionContext context);
}
