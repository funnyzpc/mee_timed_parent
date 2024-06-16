/**
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.shedlock.test.boot;

import com.zaxxer.hikari.HikariDataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M")
public class TestngApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestngApplication.class);
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
//        return new JdbcTemplateLockProvider(dataSource, "sys_shedlock_job");
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withTableName("SYS_SHEDLOCK_JOB") // 这里定义表名(任务锁)
                .withTableAppName("SYS_SHEDLOCK_APP") // 这里定义应用表名(全局锁)
                .withJdbcTemplate(new JdbcTemplate(dataSource))
//                .withIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)
//                .usingDbTime() // DB time为UTC时区会有时差
//                        .withTimeZone(TimeZone.getTimeZone(DateUtil.zoneId))
                .build()
        );
    }
    @Bean
    public DataSource dataSource() {
        HikariDataSource datasource = new HikariDataSource();
//        datasource.setDriverClassName("org.postgresql.Driver");
//        datasource.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/mee?stringtype=unspecified&currentSchema=mee_quartz&reWriteBatchedInserts=true");
//        datasource.setUsername("mee_admin");
//        datasource.setPassword("mee_admin");
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setJdbcUrl("jdbc:mysql://localhost:3306/mee_quartz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        datasource.setUsername("root");
        datasource.setPassword("root");
        return datasource;
    }
//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource datasource = new HikariDataSource();
//        datasource.setJdbcUrl("jdbc:hsqldb:mem:mymemdb");
//        datasource.setUsername("SA");
//        datasource.setPassword("");
//
//        new JdbcTemplate(datasource).execute("CREATE TABLE shedlock(\n" +
//            "    name VARCHAR(64), \n" +
//            "    lock_until TIMESTAMP(3) NULL, \n" +
//            "    locked_at TIMESTAMP(3) NULL, \n" +
//            "    locked_by  VARCHAR(255), \n" +
//            "    PRIMARY KEY (name)\n" +
//            ")");
//        return datasource;
//    }

    /**
     * 设置执行线程数
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize( 4 );
        scheduler.setThreadNamePrefix("SCHEDULER-");
        scheduler.initialize();
        return scheduler;
    }

}
