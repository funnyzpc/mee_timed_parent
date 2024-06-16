package com.mee.timed.template;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

/**
 * Configuration
 *
 * @author shaoow
 * @version 1.0
 * @className Configuration
 * @date 2024/6/13 9:17
 */
public final class Configuration {
    public static final String DEFAULT_TABLE_NAME = "shedlock";
    private final JdbcTemplate jdbcTemplate;
//    private final PlatformTransactionManager transactionManager;
    private String application;
    private String tableName;
    private String tableAppName;
    private TimeZone timeZone;
    private ColumnNames columnNames = new ColumnNames("name", "lock_until", "locked_at", "locked_by");
    private AppColumnNames appColumnNames = new AppColumnNames("application", "host_ip", "host_name", "state","update_time");

    /**
     * 实例所在主机名称
     */
    private String lockedByValue;//=Utils.getHostname();
    /**
     * 实例所在主机名称
     */
    private final String hostName=Utils.getHostname();
    /**
     * 实例所在主机IP
     */
    private final String hostIP=Utils.getHostaddress();
    private boolean useDbTime=true;
    private Integer isolationLevel;
//    // add
//    private final boolean hasAppTable=false;

    public Configuration(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = requireNonNull(jdbcTemplate, "jdbcTemplate can not be null");
        this.lockedByValue = Utils.getHostname();
        this.useDbTime=Boolean.TRUE;
    }

    Configuration(
            JdbcTemplate jdbcTemplate,
            String tableName,
            String tableAppName,
            TimeZone timeZone,
            ColumnNames columnNames,
            AppColumnNames appColumnNames,
            String lockedByValue,
            boolean useDbTime,
            Integer isolationLevel) {

        this.jdbcTemplate = requireNonNull(jdbcTemplate, "jdbcTemplate can not be null");
        this.tableName = requireNonNull(tableName, "tableName can not be null");
        // 可以为null
        this.tableAppName = tableAppName;
        this.timeZone = timeZone;
        this.columnNames = requireNonNull(columnNames, "columnNames can not be null");
        this.appColumnNames =appColumnNames;
        this.lockedByValue = requireNonNull(lockedByValue, "lockedByValue can not be null");
        this.isolationLevel = isolationLevel;
        if (useDbTime && timeZone != null) {
            throw new IllegalArgumentException("Can not set both useDbTime and timeZone");
        }
        this.useDbTime = timeZone != null?false:useDbTime;
    }

//    Configuration(
//            JdbcTemplate jdbcTemplate,
//            String tableName,
//            String tableAppName,
//            TimeZone timeZone,
//            ColumnNames columnNames,
//            AppColumnNames appColumnNames,
//            String lockedByValue,
//            boolean useDbTime,
//            Integer isolationLevel) {
//
//        this.jdbcTemplate = requireNonNull(jdbcTemplate, "jdbcTemplate can not be null");
//        this.tableName = requireNonNull(tableName, "tableName can not be null");
//        // 可以为null
//        this.tableAppName = tableAppName;
//        this.timeZone = timeZone;
//        this.columnNames = requireNonNull(columnNames, "columnNames can not be null");
//        this.appColumnNames =appColumnNames;
//        this.lockedByValue = requireNonNull(lockedByValue, "lockedByValue can not be null");
//        this.isolationLevel = isolationLevel;
//        if (useDbTime && timeZone != null) {
//            throw new IllegalArgumentException("Can not set both useDbTime and timeZone");
//        }
//        this.useDbTime = useDbTime;
//        // add
//        this.hasAppTable=!(null == this.tableAppName || "".equals(this.tableAppName.trim()));
//    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableAppName() {
        return tableAppName;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public ColumnNames getColumnNames() {
        return columnNames;
    }

    public AppColumnNames getAppColumnNames() {
        return appColumnNames;
    }

    public String getLockedByValue() {
        return lockedByValue;
    }

    public String getApplication() {
        return application;
    }
    public boolean getUseDbTime() {
        return useDbTime;
    }

    public Integer getIsolationLevel() {
        return isolationLevel;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostIP() {
        return hostIP;
    }

    public boolean hasAppTable() {
        return !(null == this.tableAppName || "".equals(this.tableAppName.trim()));
    }

    public Configuration setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Configuration setTableAppName(String tableAppName) {
        this.tableAppName = tableAppName;
        return this;
    }

    public Configuration setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public Configuration setColumnNames(ColumnNames columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    public Configuration setAppColumnNames(AppColumnNames appColumnNames) {
        this.appColumnNames = appColumnNames;
        return this;
    }

    public Configuration setIsolationLevel(Integer isolationLevel) {
        this.isolationLevel = isolationLevel;
        return this;
    }

    public Configuration setApplication(String application) {
        this.application = application;
        return this;
    }

    public Configuration setLockedByValue(LockedTypeEnum lockedTypeEnum) {
        requireNonNull((null==lockedTypeEnum || lockedTypeEnum.equals(LockedTypeEnum.OTHER))?null:"", "columnNames can not be null");
        switch (lockedTypeEnum){
            case HOST_IP:
                this.lockedByValue= Utils.getHostaddress();
            case HOST_NAME:
                this.lockedByValue=Utils.getHostname();
            case OTHER:
                this.lockedByValue = null;
        }
//        this.lockedByValue = switch (lockedTypeEnum){
//            case HOST_IP -> Utils.getHostaddress();
//            case HOST_NAME -> Utils.getHostname();
//            case OTHER -> null;
//        };
        return this;
    }
    public Configuration setLockedByValue(String lockedByValue) {
        this.lockedByValue = lockedByValue;
        return this;
    }

    public Configuration setUseDbTime(boolean useDbTime) {
        this.useDbTime = useDbTime;
        return this;
    }

    public static enum LockedTypeEnum{
        HOST_IP,
        HOST_NAME,
        // NO use
        OTHER;
    }


}