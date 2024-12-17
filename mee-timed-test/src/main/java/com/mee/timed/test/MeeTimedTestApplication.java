package com.mee.timed.test;

import com.mee.timed.annotation.EnableMeeTimed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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



}
