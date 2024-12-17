package com.mee.timed.test.job;

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
        LOGGER.info("=====> [exec01] Already Executed! <=====");
        TimeUnit.SECONDS.sleep(6);
    }

    @MeeTimed(cron = "0/2 * * * * ?",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
    public void exec02() throws InterruptedException {
        LOGGER.info("=====> [exec02] Already Executed! <=====");
        TimeUnit.SECONDS.sleep(5);
    }

    @MeeTimed(cron = "0/20 * * * * ?",lockAtLeastFor = "PT10S",lockAtMostFor ="PT10S" )
    public void exec03(JobExecutionContext context) throws InterruptedException {
        LOGGER.info("=====> "+context.getJobInfo().getName()+" <=====");
        TimeUnit.SECONDS.sleep(5);
    }

    @MeeTimeds({
         @MeeTimed(cron = "10,20,30,40,50 * * * * ?",lockAtMostFor ="PT5S",lockName = "execute1"),
         @MeeTimed(cron = "0 0/2 * * * ?",lockAtMostFor ="PT1M",lockName = "execute2"),
         @MeeTimed(cron = "0 0/4 * ? * MON-FRI",lockAtMostFor ="PT1M",lockName = "execute3"),
         // 纽约时间每年的7月9号22点2分执行
         @MeeTimed(cron = "0 2 22 9 7 ?",lockAtMostFor ="PT1M",lockName = "execute4",zone = "America/New_York"),
         // 每月最后一天的十点半(eg:2024-07-31 10:30:00)
         @MeeTimed(cron = "0 30 10 L * ?",lockAtMostFor ="PT1M",lockName = "execute5")
    })
    @Override
    public void execute(JobExecutionContext context)   {
        LOGGER.info("=====> proxy job exec! data:"+context.getJobInfo().getName()+"  <=====");
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
