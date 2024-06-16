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
package net.javacrumbs.shedlock.provider.jdbctemplate;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.support.Utils;
import net.javacrumbs.shedlock.support.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class MySqlServerTimeStatementsSource extends SqlStatementsSource {
    private static final String now = "UTC_TIMESTAMP(3)";
    private static final String lockAtMostFor = "TIMESTAMPADD(MICROSECOND, :lockAtMostForMicros, " + now + ")";

    MySqlServerTimeStatementsSource(JdbcTemplateLockProvider.Configuration configuration) {
        super(configuration);
    }

//    @Override
//    String getInsertStatement() {
//        // insert INTO SYS_SHEDLOCK_JOB(application, name, host_ip, lock_until, locked_at, locked_by, state, update_time)
//        // select 'shedlock-springboot-test', '127.0.0.1', 'MEE23120004', '1', CURRENT_TIMESTAMP from dual
//        // where ( select 1 from sys_shedlock_app where application='shedlock-springboot-test' and host_ip='127.0.0.1' and state='0' ) is not null
//        // ON DUPLICATE KEY update host_name=values(host_name), update_time = values(update_time)
//        return "INSERT INTO " + tableName() + "(" +application()+", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") " +
//            " select :application, :name, :hostIP, " + lockAtMostFor + ", " + now + ", :lockedBy, :state, CURRENT_TIMESTAMP FROM DUAL  "+
////            " WHERE ( SELECT 1 FROM "+tableAppName()+" WHERE application = :application and host_ip = :hostIP and state = :state ) IS NOT NULL  "+
//            " WHERE (SELECT 1 from "+tableName()+" WHERE "+application()+" = :application and "+name()+" = :name and "+state()+" = :state AND "+lockUntil()+" <= UTC_TIMESTAMP(3) ) IS NOT NULL "+isNeedAppCause() +
//            " ON DUPLICATE KEY UPDATE "+hostIP()+"=VALUES("+hostIP()+"), "+updateTime()+" = VALUES("+updateTime()+")   ";
//
////        return "INSERT IGNORE INTO " + tableName() + "(" +application()+", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:application, :name, :hostIP, " + lockAtMostFor + ", " + now + ", :lockedBy, :state, CURRENT_TIMESTAMP )" ;
////        return "INSERT INTO " + tableName() + "(" +application()+", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:application, :name, :hostIP, " + lockAtMostFor + ", " + now + ", :lockedBy, :state, CURRENT_TIMESTAMP )" +
////             " ON DUPLICATE KEY UPDATE  " + hostIP()+" = VALUES("+hostIP()+") , "+updateTime() + " = VALUES("+updateTime()+") ";
//    }
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
        return "INSERT INTO " + tableAppName() + "(" +application()+ ", "+ hostIP() + ", " +hostName()+", "+ state() + ", " + appUpdateTime() + ") VALUES(:application, :hostIP, :hostName, :state, CURRENT_TIMESTAMP)" +
            " ON DUPLICATE KEY UPDATE  " + hostName()+" = VALUES("+hostName()+") , "+appUpdateTime() + " = VALUES("+appUpdateTime()+") ";
    }
//    @NonNull
//    private String updateAppClause() {
//        //  SET lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), locked_at = timezone('utc', CURRENT_TIMESTAMP), locked_by = :lockedBy WHERE shedlock.name = :name AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
//        return " SET "+ hostName() + " = :hostName, " + appUpdateTime() + " = CURRENT_TIMESTAMP WHERE (" + tableAppName() + "." + hostName() + " != :hostName or " +tableAppName() + "." + appUpdateTime() + " != CURRENT_TIMESTAMP ) AND " +tableAppName()+"."+state()+" = :state";
//    }
//    @NonNull
//    private String updateClause() {
//        return " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = " + lockAtMostFor + ", " + lockedAt() + " = " + now + ", " + lockedBy() + " = :lockedBy, "+updateTime()+" = CURRENT_TIMESTAMP WHERE " + tableName() + "." + application() + " = :application AND " +tableName() + "." + name() + " = :name AND " + tableName() + "." + lockUntil() + " <= " + now+ " AND "+tableName()+"."+state()+" = :state " + isNeedAppCause();
//    }
//    @Override
//    public String isNeedAppCause(){
//        // todo 定义字段到 configuration 中
//        return ( null==configuration.getTableAppName() || "".equals(configuration.getTableAppName().trim()) )?" ":appCause();
//    }
//    private String appCause(){
//        return " AND (SELECT 1 from "+tableAppName()+" WHERE "+application()+" = :application AND "+hostIP()+" = :hostIP AND "+state()+" = :state ) IS NOT NULL ";
//    }
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
    @NonNull Map<String, Object> params(@NonNull LockConfiguration lockConfiguration) {
        Map<String, Object> params = new HashMap<>(10,1);
        params.put("name", lockConfiguration.getName());
        params.put("lockedBy", configuration.getLockedByValue());
        params.put("lockAtMostForMicros", lockConfiguration.getLockAtMostFor().toNanos() / 1_000);
        params.put("lockAtLeastForMicros", lockConfiguration.getLockAtLeastFor().toNanos() / 1_000);
        // add
        params.put("application", LockConfiguration.getSchedName());
        params.put("hostIP", Utils.getHostaddress());
        params.put("hostName", configuration.getLockedByValue());
        params.put("state","1");
        params.put("updateTime",new Date());
        return params;
    }
}
