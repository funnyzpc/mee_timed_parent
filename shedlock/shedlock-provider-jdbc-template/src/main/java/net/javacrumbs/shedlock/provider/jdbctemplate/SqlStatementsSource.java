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

import net.javacrumbs.shedlock.core.ClockProvider;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider.Configuration;
import net.javacrumbs.shedlock.support.Utils;
import net.javacrumbs.shedlock.support.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

class SqlStatementsSource {
    protected final Configuration configuration;

    private static final Logger logger = LoggerFactory.getLogger(SqlStatementsSource.class);

    SqlStatementsSource(Configuration configuration) {
        this.configuration = configuration;
    }

    private static final String POSTGRESQL = "postgresql";
    private static final String MSSQL = "microsoft sql server";
    private static final String ORACLE = "oracle";
    private static final String MYSQL = "mysql";
    private static final String MARIADB = "mariadb";
    private static final String HSQL = "hsql database engine";
    private static final String H2 = "h2";


    static SqlStatementsSource create(Configuration configuration) {
        String databaseProductName = getDatabaseProductName(configuration).toLowerCase();

        if (configuration.getUseDbTime()) {
            switch (databaseProductName) {
                case POSTGRESQL:
                    logger.debug("Using PostgresSqlServerTimeStatementsSource");
                    return new PostgresSqlServerTimeStatementsSource(configuration);
                case MSSQL:
                    logger.debug("Using MsSqlServerTimeStatementsSource");
                    return new MsSqlServerTimeStatementsSource(configuration);
                case ORACLE:
                    logger.debug("Using OracleServerTimeStatementsSource");
                    return new OracleServerTimeStatementsSource(configuration);
                case MYSQL:
                    logger.debug("Using MySqlServerTimeStatementsSource");
                    return new MySqlServerTimeStatementsSource(configuration);
                case MARIADB:
                    logger.debug("Using MySqlServerTimeStatementsSource (for MariaDB)");
                    return new MySqlServerTimeStatementsSource(configuration);
                case HSQL:
                    logger.debug("Using HsqlServerTimeStatementsSource");
                    return new HsqlServerTimeStatementsSource(configuration);
                case H2:
                    logger.debug("Using H2ServerTimeStatementsSource");
                    return new H2ServerTimeStatementsSource(configuration);
                default:
                    if (databaseProductName.startsWith("db2")) {
                        logger.debug("Using Db2ServerTimeStatementsSource");
                        return new Db2ServerTimeStatementsSource(configuration);
                    }
                    throw new UnsupportedOperationException("DB time is not supported for '" + databaseProductName + "'");
            }
        } else {
            if (POSTGRESQL.equals(databaseProductName)) {
                logger.debug("Using PostgresSqlServerTimeStatementsSource");
                return new PostgresSqlStatementsSource(configuration);
            } else if (MYSQL.equals(databaseProductName)){
                return new MysqlSqlStatementsSource(configuration);
            } else if (ORACLE.equals(databaseProductName)){
                return new OracleSqlStatementsSource(configuration);
            } else {
                logger.debug("Using SqlStatementsSource");
                return new SqlStatementsSource(configuration);
            }
        }
    }

    private static String getDatabaseProductName(Configuration configuration) {
        try {
            return configuration.getJdbcTemplate().execute((ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            logger.debug("Can not determine database product name " + e.getMessage());
            return "Unknown";
        }
    }

    @NonNull
    Map<String, Object> params(@NonNull LockConfiguration lockConfiguration) {
        Map<String, Object> params = new HashMap<>(10,1);
        params.put("name", lockConfiguration.getName());
        params.put("lockUntil", timestamp(lockConfiguration.getLockAtMostUntil()));
        params.put("now", timestamp(ClockProvider.now()));
        params.put("lockedBy", configuration.getLockedByValue());
        params.put("unlockTime", timestamp(lockConfiguration.getUnlockTime()));
        params.put("application", LockConfiguration.getSchedName());
        params.put("hostIP", Utils.getHostaddress());
        params.put("hostName", configuration.getLockedByValue());
        params.put("state","1");
        params.put("updateTime",new Date());
        return params;
    }

    @NonNull
    private Object timestamp(Instant time) {
        TimeZone timeZone = configuration.getTimeZone();
        if (timeZone == null) {
            return Timestamp.from(time);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(time));
            calendar.setTimeZone(timeZone);
            return calendar;
        }
    }


    String getInsertStatement() {
        // INSERT INTO shedlock(name, lock_until, locked_at, locked_by) VALUES(:name, :lockUntil, :now, :lockedBy)
//        return "INSERT INTO " + tableName() + "(" + application() + ", " +name() + ", "+hostIP() + ", " + lockUntil() + ", " + lockedAt() + ", " + lockedBy()+ ", " + state() +", "+updateTime()+ ") VALUES(:application, :name, :hostIP, :lockUntil, :now, :lockedBy, :state, :updateTime)";
        return "INSERT INTO " + tableName() + "(" + application() + ", " +name() + ", "+hostIP() + ", " + lockUntil() + ", " + lockedAt() + ", " + lockedBy()+ ", " + state() +", "+updateTime()+ ") VALUES(:application, :name, :hostIP, :now, :now, :lockedBy, :state, :updateTime)";
    }

    String getAppInsertStatement() {
        // INSERT INTO SYS_SHEDLOCK_APP(application, host_ip, host_name, state, create_time) VALUES(:application, :hostIP, :hostName :state, :createTime)
        return "INSERT INTO " + appTableName() + "(" + appApplication() + ", " +appHostIP() + ", "+appHostName() + ", " + appState() + ", " + appUpdateTime() + ") VALUES( :application, :hostIP, :hostName, :state, :updateTime)";
    }

    public String getUpdateStatement() {
        return "UPDATE " + tableName() + " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = :lockUntil, " + lockedAt() + " = :now, " + lockedBy() + " = :lockedBy WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockUntil() + " <= :now AND "+state()+" = :state "+
            appCause();
    }

    @Deprecated
    public String getExtendStatement() {
        return "UPDATE " + tableName() + " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = :lockUntil WHERE " +application()+" = :application AND "+ name() + " = :name AND " + lockedBy() + " = :lockedBy AND " + lockUntil() + " > :now AND "+state()+" = :state";
    }

    public String getUnlockStatement() {
        return "UPDATE " + tableName() + " SET "+ hostIP() + " = :hostIP, " + lockUntil() + " = :unlockTime WHERE " + application() + " = :application AND "+ name() + " = :name AND "+state()+" = :state";
    }
    public String appCause(){
        return configuration.getHasAppTable()?" AND (SELECT 1 from "+ appTableName()+" WHERE "+appApplication()+" = :application AND "+appHostIP()+" = :hostIP AND "+appState()+" = :state ) IS NOT NULL " : "";
    }

    String name() {
        return configuration.getColumnNames().getName();
    }

    String lockUntil() {
        return configuration.getColumnNames().getLockUntil();
    }

    String lockedAt() {
        return configuration.getColumnNames().getLockedAt();
    }

    String lockedBy() {
        return configuration.getColumnNames().getLockedBy();
    }

    String tableName() {
        return configuration.getTableName();
    }
    String application(){
        return configuration.getColumnNames().getApplication();
    }
    String hostIP(){
        return configuration.getColumnNames().getHostIP();
    }
    String state(){
        return configuration.getColumnNames().getState();
    }
    String updateTime(){
        return configuration.getColumnNames().getUpdateTime();
    }
    String hostName(){
        return configuration.getColumnNames().getHostName();
    }

    String appTableName() {
        return configuration.getTableAppName();
    }
    String appHostName(){
        return configuration.getAppColumnNames().getHostName();
    }
    String appUpdateTime(){
        return configuration.getAppColumnNames().getUpdateTime();
    }
    String appHostIP(){
        return configuration.getAppColumnNames().getHostIP();
    }
    String appState(){
        return configuration.getAppColumnNames().getState();
    }
    String appApplication(){
        return configuration.getAppColumnNames().getApplication();
    }

}
