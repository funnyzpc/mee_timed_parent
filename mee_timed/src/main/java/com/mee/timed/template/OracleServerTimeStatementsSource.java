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

class OracleServerTimeStatementsSource extends SqlStatementsSource {
    private static final String now = "SYS_EXTRACT_UTC(SYSTIMESTAMP)";
    private static final String lockAtMostFor = now + " + :lockAtMostFor";

    private static final long millisecondsInDay = 24 * 60 * 60 * 1000;

    OracleServerTimeStatementsSource(Configuration configuration) {
        super(configuration);
    }

    @Override
    String getInsertStatement() {
       // MERGE INTO SYS_SHEDLOCK_JOB T
       // USING ( SELECT '' as application,'' as name,'' as host_ip,'' as locked_at,'' as lock_until,'' as locked_by,'' as state,'' as update_time FROM DUAL ) TT
       // ON (t.application = TT.application AND t.name = TT.name)
       // WHEN MATCHED THEN
       // UPDATE SET T.host_ip = TT.host_ip ,T.update_time = TT.update_time ,
       //     WHEN NOT MATCHED THEN
       // INSERT (application,name,host_ip,locked_at,lock_until,locked_by,state,update_time ) VALUES (TT.application,TT.name,TT.host_ip,TT.locked_at,TT.lock_until,TT.locked_by,TT.state,TT.update_time);

        return "MERGE INTO "+tableName()+" T \n" +
            "USING ( SELECT :application as "+application()+",:name as "+name()+",:hostIP as "+hostIP()+", "+now+" as "+lockedAt()+", "+now+" as "+lockUntil()+",:lockedBy as "+lockedBy()+",:state as "+state()+", :updateTime as "+updateTime()+" FROM DUAL ) TT\n" +
            "ON (T."+application()+" = TT."+application()+" AND T."+name()+" = TT."+name()+") \n" +
            "WHEN MATCHED THEN \n" +
            "UPDATE SET T."+hostIP()+" = TT."+hostIP()+" , T."+updateTime()+" = TT."+updateTime()+" \n" +
            "    WHEN NOT MATCHED THEN \n" +
            "INSERT ( "+application()+","+name()+","+hostIP()+","+lockedAt()+","+lockUntil()+","+lockedBy()+","+state()+","+updateTime()+" ) VALUES (TT."+application()+",TT."+name()+",TT."+hostIP()+",TT."+lockedAt()+",TT."+lockUntil()+",TT."+lockedBy()+",TT."+state()+",TT."+updateTime()+" )";
//        return "INSERT INTO " + tableName() + "(" +application()+", "+ name() + ", " +hostIP()+", "+ lockUntil() + ", " + lockedAt() + ", " + lockedBy() +", "+state()+", "+updateTime()+ ") VALUES(:name, " + now + ", " + now + ", :lockedBy, :state, SYSTIMESTAMP(0))";
    }
    @Override
    String getAppInsertStatement() {
        // MERGE INTO SYS_SHEDLOCK_JOB T
        // USING ( SELECT '' as application,'' as host_ip ,'' as host_name,'' as state,'' as update_time FROM DUAL ) TT
        // ON (t.application = TT.application AND t.host_ip = TT.host_ip)
        // WHEN MATCHED THEN
        //     UPDATE SET T.host_name = TT.host_name ,T.update_time = TT.update_time ,
        // WHEN NOT MATCHED THEN
        //     INSERT (application,host_ip ,host_name,state,update_time ) VALUES ( TT.application,TT.host_ip ,TT.host_name,TT.state,TT.update_time );
        return "MERGE INTO "+tableAppName()+" T \n" +
            "USING ( SELECT :application AS "+application()+",:hostIP AS "+hostIP()+" ,:hostName AS "+hostName()+",:state AS "+state()+", "+now+" AS "+updateTime()+" FROM DUAL ) TT \n" +
            "ON (T."+application()+" = TT."+application()+" AND T."+hostIP()+" = TT."+hostIP()+" ) \n" +
            "WHEN MATCHED THEN \n" +
            "    UPDATE SET T."+hostName()+" = TT."+hostName()+" ,T."+updateTime()+" = TT."+updateTime()+"  \n" +
            "WHEN NOT MATCHED THEN \n" +
            "    INSERT ("+application()+","+hostIP()+","+hostName()+","+state()+","+updateTime()+" ) VALUES ( TT."+application()+",TT."+hostIP()+" ,TT."+hostName()+",TT."+state()+",TT."+updateTime()+" )";
//        return "INSERT INTO " + tableAppName() + "(" +application()+ ", "+ hostIP() + ", " +hostName()+", "+ state() + ", " + appUpdateTime() + ") VALUES(:application, :hostIP, :hostName, :state, SYSTIMESTAMP(0)";
    }

    @Override
    public String getUpdateStatement() {
        return "UPDATE " + tableName() + " SET " +hostIP()+" = :hostIP, "+ lockUntil() + " = " + lockAtMostFor + ", " + lockedAt() + " = " + now + ", " + lockedBy() + " = :lockedBy, "+updateTime()+" = SYSTIMESTAMP WHERE " + application()+" = :application AND "+name() + " = :name AND " + lockUntil() + " <= " + now+" AND "+state()+" = :state "
            +super.appCause();
    }

    @Override
    public String getUnlockStatement() {
        String lockAtLeastFor = lockedAt() + " + :lockAtLeastFor";
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = CASE WHEN " + lockAtLeastFor + " > " + now + " THEN " + lockAtLeastFor + " ELSE " + now + " END WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockedBy() + " = :lockedBy AND "+state()+" = :state";
    }

    @Override
    public String getExtendStatement() {
        return "UPDATE " + tableName() + " SET " + lockUntil() + " = " + lockAtMostFor + " WHERE " + application()+" = :application AND " + name() + " = :name AND " + lockedBy() + " = :lockedBy AND " + lockUntil() + " > " + now+" and "+state()+" = :state ";
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
    Map<String, Object> params(LockConfiguration lockConfiguration) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lockConfiguration.getName());
        params.put("lockedBy", configuration.getLockedByValue());
        params.put("lockAtMostFor", ((double) lockConfiguration.getLockAtMostFor().toMillis()) / millisecondsInDay);
        params.put("lockAtLeastFor", ((double) lockConfiguration.getLockAtLeastFor().toMillis()) / millisecondsInDay);
        // add
        params.put("application", configuration.getApplication());
        params.put("hostIP", configuration.getHostIP());
        params.put("hostName", configuration.getHostName());
        params.put("state","1");
        params.put("updateTime",new Date());
        return params;
    }
}
