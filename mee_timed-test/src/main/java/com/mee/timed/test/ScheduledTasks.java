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
package com.mee.timed.test;

import com.mee.timed.annotation.MeeTimed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @MeeTimed(fixedRate = 10000,lockName = "exec1",lockAtLeastFor = "PT5S",lockAtMostFor ="PT5S" )
    public void exec01() {
        LOGGER.info("=====> [exec01] Already Executed! <=====");
    }

    @MeeTimed(cron = "0 */2 * * * *",lockName = "exec2",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
    public void exec02() {
        LOGGER.info("=====> [exec02] Already Executed! <=====");
    }

    @MeeTimed(cron = "*/20 * * * * *",lockAtLeastFor = "PT1M",lockAtMostFor ="PT1M" )
    public void exec03() {
        LOGGER.info("=====> [exec03] Already Executed! <=====");
    }


}
