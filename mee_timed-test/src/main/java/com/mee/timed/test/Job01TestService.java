package com.mee.timed.test;

import com.mee.timed.Job;
import com.mee.timed.JobExecutionContext;
import com.mee.timed.annotation.MeeTimed;
import com.mee.timed.annotation.MeeTimeds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Job01TestService implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(Job01TestService.class);

    @MeeTimed(fixedRate = 10000,lockAtLeastFor = "PT5S",lockAtMostFor ="PT5S" )
    public void exec01() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        LOGGER.info("=====> [exec01] Already Executed! <=====");
    }

    @MeeTimed(cron = "0 */2 * * * *",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
    public void exec02() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        LOGGER.info("=====> [exec02] Already Executed! <=====");
    }

    @MeeTimed(cron = "*/20 * * * * *",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
    public void exec03() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        LOGGER.info("=====> [exec03] Already Executed! <=====");
    }

    @MeeTimeds({
         @MeeTimed(cron = "10,20,30,40,50 * * * * *",lockAtMostFor ="PT5S",lockName = "execute1"),
         @MeeTimed(cron = "0 */3 * * * *",lockAtMostFor ="PT1M",lockName = "execute2"),
         @MeeTimed(cron = "0 */6 * * * *",lockAtMostFor ="PT1M",lockName = "execute3")
    })
    @Override
    public void execute(JobExecutionContext context)   {
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("=====> proxy job exec! data:"+context.getJobDataJson()+"  <=====");
    }
}
