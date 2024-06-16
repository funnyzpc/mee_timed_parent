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

/**
 * 使用数据库时间的 StatementsSource
 */
class PostgresSqlServerTimeStatementsSource extends SqlStatementsSource {
    private static final String now = "timezone('utc', CURRENT_TIMESTAMP)";
    private static final String lockAtMostFor = now + " + cast(:lockAtMostForInterval as interval)";

    PostgresSqlServerTimeStatementsSource(JdbcTemplateLockProvider.Configuration configuration) {
        super(configuration);
    }

    @Override
    String getInsertStatement() {
        // INSERT INTO shedlock(name, lock_until, locked_at, locked_by) VALUES(:name, timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), timezone('utc', CURRENT_TIMESTAMP), :lockedBy) ON CONFLICT (name) DO UPDATE SET lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), locked_at = timezone('utc', CURRENT_TIMESTAMP), locked_by = :lockedBy WHERE shedlock.name = :name AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
        return "INSERT INTO " + tableName() + "(" +application()+ ", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:application, :name, :hostIP, "+now+", "+now+", :lockedBy, :state, CURRENT_TIMESTAMP)" +
//            " ON CONFLICT (" + application()+", "+name() + ") DO UPDATE" + updateClause();
            " ON CONFLICT (" + application()+", "+name() + ") DO UPDATE  SET "+ hostIP() + " = :hostIP, " +updateTime()+" = CURRENT_TIMESTAMP";
    }
    @Override
    String getAppInsertStatement() {
        // INSERT INTO SYS_SHEDLOCK_APP(application, host_ip, host_name, state, create_time) VALUES(:application, :hostIp, :hostName :state, CURRENT_TIMESTAMP) ON CONFLICT (name) DO UPDATE SET lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), locked_at = timezone('utc', CURRENT_TIMESTAMP), locked_by = :lockedBy WHERE shedlock.name = :name AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
        return "INSERT INTO " + tableAppName() + "(" +application()+ ", "+ hostIP() + ", " +hostName()+", "+ state() + ", " + appUpdateTime() + ") VALUES(:application, :hostIP, :hostName, :state, CURRENT_TIMESTAMP)" +
//            " ON CONFLICT (" + application()+", "+hostIP() + ") DO UPDATE " + updateAppClause();
            " ON CONFLICT (" + application()+", "+hostIP() + ") DO UPDATE SET "+ hostName() + " = :hostName, " + appUpdateTime() + " = CURRENT_TIMESTAMP ";
    }
//    @NonNull
//    private String updateAppClause() {
//        //  SET lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), locked_at = timezone('utc', CURRENT_TIMESTAMP), locked_by = :lockedBy WHERE shedlock.name = :name AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
//        return " SET "+ hostName() + " = :hostName, " + appUpdateTime() + " = CURRENT_TIMESTAMP WHERE (" + tableAppName() + "." + hostName() + " != :hostName or " +tableAppName() + "." + appUpdateTime() + " != CURRENT_TIMESTAMP ) AND " +tableAppName()+"."+state()+" = :state";
//    }
    @NonNull
    private String updateClause() {
        //  SET lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(:lockAtMostForInterval as interval), locked_at = timezone('utc', CURRENT_TIMESTAMP), locked_by = :lockedBy WHERE shedlock.name = :name AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
//        return " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = " + lockAtMostFor + ", " + lockedAt() + " = " + now + ", " + lockedBy() + " = :lockedBy WHERE " + tableName() + "." + application() + " = :application AND " +tableName() + "." + name() + " = :name AND " + tableName() + "." + lockUntil() + " <= " + now+ " AND "+tableName()+"."+state()+" = :state";
        return " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = " + lockAtMostFor + ", " + lockedAt() + " = " + now + ", " + lockedBy() + " = :lockedBy, "+updateTime()+" = CURRENT_TIMESTAMP WHERE " + tableName() + "." + application() + " = :application AND " +tableName() + "." + name() + " = :name AND " + tableName() + "." + lockUntil() + " <= " + now+ " AND "+tableName()+"."+state()+" = :state "
            + super.appCause();
    }
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
        return "UPDATE " + tableName() + updateClause();
    }

    @Override
    public String getUnlockStatement() {
        String lockAtLeastFor = lockedAt() + " + cast(:lockAtLeastForInterval as interval)";
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = CASE WHEN " + lockAtLeastFor + " > " + now + " THEN " + lockAtLeastFor + " ELSE " + now + " END WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockedBy() + " = :lockedBy AND "+state()+" = :state";
    }

    @Deprecated
    @Override
    public String getExtendStatement() {
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = " + lockAtMostFor +" WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockedBy() + " = :lockedBy AND " + lockUntil() + " > " + now+" AND "+state()+" = :state";
    }

    @Override
    @NonNull Map<String, Object> params(@NonNull LockConfiguration lockConfiguration) {
        Map<String, Object> params = new HashMap<>(10,1);
        params.put("name", lockConfiguration.getName());
        params.put("lockedBy", configuration.getLockedByValue());
        params.put("lockAtMostForInterval", lockConfiguration.getLockAtMostFor().toMillis() + " milliseconds");
        params.put("lockAtLeastForInterval", lockConfiguration.getLockAtLeastFor().toMillis() + " milliseconds");
        // add
        params.put("application", LockConfiguration.getSchedName());
        params.put("hostIP", Utils.getHostaddress());
        params.put("hostName", configuration.getLockedByValue());
        params.put("state","1");
        params.put("updateTime",new Date());
        return params;
    }
}
