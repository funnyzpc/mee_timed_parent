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
package com.mee.timed.template;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class MySqlServerTimeStatementsSource extends SqlStatementsSource {
    private static final String now = "UTC_TIMESTAMP(3)";
    private static final String lockAtMostFor = "TIMESTAMPADD(MICROSECOND, :lockAtMostForMicros, " + now + ")";

    MySqlServerTimeStatementsSource(Configuration configuration) {
        super(configuration);
    }
    
    @Override
    String getInsertStatement() {
//        return  "INSERT INTO " + tableName() + "(" +application()+ ", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:application, :name, :hostIP, " + lockAtMostFor + ", " + now + ", :lockedBy, :state, CURRENT_TIMESTAMP)" +
        return  "INSERT INTO " + tableName() + "(" +application()+ ", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:application, :name, :hostIP, " + now + ", " + now + ", :lockedBy, :state, CURRENT_TIMESTAMP)" +
            " ON DUPLICATE KEY UPDATE "+hostIP()+"=VALUES("+hostIP()+"), "+updateTime()+" = VALUES("+updateTime()+") ";
    }
    @Override
    String getAppInsertStatement() {
        // INSERT INTO SYS_SHEDLOCK_APP(application, host_ip, host_name, state, update_time)
        // VALUES('shedlock-springboot-test', '127.0.0.1', 'MEE23120004', '1', CURRENT_TIMESTAMP)
        // ON DUPLICATE KEY update host_name=values(host_name), update_time = values(update_time)
        return "INSERT INTO " + appTableName() + "(" +appApplication()+ ", "+ appHostIP() + ", " +appHostName()+", "+ appState() + ", " + appUpdateTime() + ") VALUES(:application, :hostIP, :hostName, :state, CURRENT_TIMESTAMP)" +
            " ON DUPLICATE KEY UPDATE  " + appHostName()+" = VALUES("+appHostName()+") , "+appUpdateTime() + " = VALUES("+appUpdateTime()+") ";
    }
    
    @Override
    public String getUpdateStatement() {
        return "UPDATE " + tableName() + " SET " +hostIP()+ " = :hostIP, " +lockUntil() + " = " + lockAtMostFor + ", " + lockedAt() + " = " + now + ", " + lockedBy() + " = :lockedBy, "+updateTime()+" = CURRENT_TIMESTAMP WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockUntil() + " <= " + now + " AND "+state()+" = :state "
            + super.appCause();
    }

    @Override
    public String getUnlockStatement() {
        String lockAtLeastFor = "TIMESTAMPADD(MICROSECOND, :lockAtLeastForMicros, " + lockedAt() + ")";
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = IF (" + lockAtLeastFor + " > " + now + " , " + lockAtLeastFor + ", " + now + ") WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockedBy() + " = :lockedBy AND "+state()+" = :state ";
    }

    @Override
    public String getExtendStatement() {
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = " + lockAtMostFor + " WHERE "+ application()+ " = :application AND " + name() + " = :name AND " + lockedBy() + " = :lockedBy AND " + lockUntil() + " > " + now+" AND "+state()+" = :state";
    }

    @Override
    Map<String, Object> params(LockConfiguration lockConfiguration) {
        Map<String, Object> params = new HashMap<>(10,1);
        params.put("name", lockConfiguration.getName());
        params.put("lockedBy", configuration.getLockedByValue());
        params.put("lockAtMostForMicros", lockConfiguration.getLockAtMostFor().toNanos() / 1_000);
        params.put("lockAtLeastForMicros", lockConfiguration.getLockAtLeastFor().toNanos() / 1_000);
        // add
        params.put("application", configuration.getApplication());
        params.put("hostIP", configuration.getHostIP());
        params.put("hostName", configuration.getHostName());
        params.put("state",configuration.getState());
        params.put("updateTime",new Date());
        return params;
    }
}
