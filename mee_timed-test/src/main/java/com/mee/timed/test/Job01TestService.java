package com.mee.timed.test;

import com.mee.timed.Job;
import com.mee.timed.JobExecutionContext;
import com.mee.timed.annotation.MeeTimed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Job01TestService implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(Job01TestService.class);

//    @MeeTimed(fixedRate = 10000,lockName = "exec1",lockAtLeastFor = "PT5S",lockAtMostFor ="PT5S" )
//    public void exec01() {
//        LOGGER.info("=====> [exec01] Already Executed! <=====");
//    }
//
//    @MeeTimed(cron = "0 */2 * * * *",lockName = "exec2",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
//    public void exec02() {
//        LOGGER.info("=====> [exec02] Already Executed! <=====");
//    }
//
//    @MeeTimed(cron = "*/20 * * * * *",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
//    public void exec03() {
//        LOGGER.info("=====> [exec03] Already Executed! <=====");
//    }

//    @MeeTimed(cron = "*/20 * * * * *",lockAtLeastFor = "PT5S",lockAtMostFor ="PT10S" )
    @MeeTimed(cron = "*/20 * * * * *",lockAtMostFor ="PT10S" )
    @Override
    public void execute(JobExecutionContext context) {
        LOGGER.info("=====> proxy job exec! data:"+context.getJobDataJson()+"  <=====");
    }
}
