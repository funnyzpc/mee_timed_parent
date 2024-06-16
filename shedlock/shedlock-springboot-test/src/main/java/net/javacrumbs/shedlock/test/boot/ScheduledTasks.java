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

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedRate = 5000)
    @SchedulerLock(name = "reportCurrentTime",lockAtLeastFor = "${lock.at.most.for}")
    public void reportCurrentTime() {
//        assertLocked();
//        System.out.println(new Date());
        LOGGER.info("=====> Already Executed! <=====");
    }

    @Scheduled(fixedRate = 10000)
    @SchedulerLock(name = "exec",lockAtLeastFor = "PT8S")
    public void exec() {
        LOGGER.info("=====> [exec] Already Executed! <=====");
    }


}
