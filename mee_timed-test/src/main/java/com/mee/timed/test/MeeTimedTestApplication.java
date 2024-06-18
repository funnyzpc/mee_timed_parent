package com.mee.timed.test;

import com.mee.timed.annotation.EnableMeeTimed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * mee-timed-test-application
 *
 * @author shaoow
 * @version 1.0
 * @className MeeTimedTestApplication
 * @date 2024/6/17 10:02
 */
@SpringBootApplication
@EnableMeeTimed
public class MeeTimedTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeeTimedTestApplication.class);
    }


//    /**
//     * 设置执行线程数
//     *
//     * @return
//     */
//    @Bean
//    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize( Runtime.getRuntime().availableProcessors()/2 );
//        scheduler.setThreadNamePrefix("MEE_TIMED-");
//        scheduler.initialize();
//        return scheduler;
//    }

}
