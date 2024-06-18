package com.mee.timed.data;

import com.mee.timed.template.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.time.Duration;
import java.util.TimeZone;

/**
 * MeeTimedProperties
 *
 * @author shaoow
 * @version 1.0
 * @className MeeTimedProperties
 * @date 2024/6/7 17:50
 */
@ConfigurationProperties("spring.mee.timed")
public class MeeTimedProperties {

    /**
     * 实例名称
     */
    private String shed;
    @Deprecated
    private int startupDelay = 0;

    @Value("${spring.application.name:}")
    private String applicationName;

    /**
     * 隔离级别
     * @see Connection.TRANSACTION_SERIALIZABLE ...
     */
    private Integer isolationLevel = null;
    /**
     * 是否使用DB时间(推荐方式)
     */
    private Boolean useDbTime;

    /**
     * 是否使用本地时间(机器时间)
     * @return
     */
    private String timeZone;
    /**
     * job表列名
     */
    private ColumnNames columnNames = new ColumnNames("NAME", "LOCK_UNTIL", "LOCKED_AT", "LOCKED_BY", "APPLICATION", "HOST_IP", "STATE", "UPDATE_TIME", "DATA", "LABEL");
    /**
     * app表列名
     */
    private AppColumnNames appColumnNames = new AppColumnNames("APPLICATION", "HOST_IP", "HOST_NAME", "STATE","UPDATE_TIME");
    /**
     * job表名
     */
    private String tableName= Configuration.DEFAULT_TABLE_NAME;
    /**
     * app表名(这个可以为空，为空则无实例锁)
     */
    private String tableAppName;
    /**
     * 锁定类型
     */
    private  Configuration.LockedTypeEnum lockedByType;

    /**
     * 最长锁定时间,超过这个时间必须会释放 (PT1M:1分钟 PT1S:1秒 PT1H:1小时)
     */
    private String defaultMostFor;
    /**
     * 最短锁定时间,即使时间内提前执行完也得等这么久 (PT1M:1分钟 PT1S:1秒 PT1H:1小时)
     */
    private String defaultLeastFor;
//    /**
//     * 是否有app表配置
//     */
//    private boolean hasAppTable=Boolean.FALSE;

    public String getShed() {
        return shed;
    }

    public MeeTimedProperties setShed(String shed) {
        this.shed = shed;
        return this;
    }

    public int getStartupDelay() {
        return startupDelay;
    }

    public MeeTimedProperties setStartupDelay(int startupDelay) {
        this.startupDelay = startupDelay;
        return this;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public MeeTimedProperties setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }
    public String getSchedName(){
        final String schedName = (null==shed || "".equals(shed.trim()))?applicationName:shed;
        if( null==schedName || "".equals(schedName.trim()) ){
            throw new RuntimeException("need [spring.application.name] or [spring.mee.timed.sched]");
        }
        return schedName;
    }

    public Integer getIsolationLevel() {
        return isolationLevel;
    }

    public MeeTimedProperties setIsolationLevel(Integer isolationLevel) {
        this.isolationLevel = isolationLevel;
        return this;
    }

    public Boolean isUseDbTime() {
        return useDbTime;
    }

    public MeeTimedProperties setUseDbTime(boolean useDbTime) {
        this.useDbTime = useDbTime;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public MeeTimedProperties setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public ColumnNames getColumnNames() {
        return columnNames;
    }

    public MeeTimedProperties setColumnNames(ColumnNames columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    public AppColumnNames getAppColumnNames() {
        return appColumnNames;
    }

    public MeeTimedProperties setAppColumnNames(AppColumnNames appColumnNames) {
        this.appColumnNames = appColumnNames;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAppName() {
        return tableAppName;
    }

    public MeeTimedProperties setTableAppName(String tableAppName) {
        this.tableAppName = tableAppName;
        return this;
    }

    public Configuration.LockedTypeEnum getLockedByType() {
        return lockedByType;
    }

    public MeeTimedProperties setLockedByType(Configuration.LockedTypeEnum lockedByType) {
        this.lockedByType = lockedByType;
        return this;
    }

    public String getDefaultMostFor() {
        return defaultMostFor;
    }

    public MeeTimedProperties setDefaultMostFor(String defaultMostFor) {
        this.defaultMostFor = defaultMostFor;
        return this;
    }

    public String getDefaultLeastFor() {
        return defaultLeastFor;
    }

    public MeeTimedProperties setDefaultLeastFor(String defaultLeastFor) {
        this.defaultLeastFor = defaultLeastFor;
        return this;
    }

    public Configuration toCfg(JdbcTemplate jdbcTemplate){
        Configuration configuration = new Configuration(jdbcTemplate)
//                .setTableName("SYS_SHEDLOCK_JOB")
                // 默认值
                .setTableName(Configuration.DEFAULT_TABLE_NAME)
//                .setTableAppName("SYS_SHEDLOCK_JOB")
                .setUseDbTime(true)
                .setApplication(this.getSchedName());
        if(null!=this.useDbTime){
            configuration.setUseDbTime(this.useDbTime);
        }
        if(null!=this.getIsolationLevel()){
            configuration.setIsolationLevel(this.getIsolationLevel());
        }
        if(null!=this.getTimeZone()){
            configuration.setTimeZone(TimeZone.getTimeZone(this.getTimeZone()));
        }
        if(null!=this.getColumnNames()){
            configuration.setColumnNames(this.getColumnNames());
        }
        if(null!=this.getAppColumnNames()){
            configuration.setAppColumnNames(this.getAppColumnNames());
        }
        if(null!=this.getTableName()){
            configuration.setTableName(this.getTableName());
        }
        if(null!=this.getTableName()){
            configuration.setTableName(this.getTableName());
        }
        if(null!=this.getTableAppName()){
            configuration.setTableAppName(this.getTableAppName());
        }
        if(null!=this.getLockedByType()){
            configuration.setLockedByValue(this.getLockedByType());
        }

        if(null!=this.getDefaultLeastFor() && !"".equals(this.getDefaultLeastFor().trim())){
            configuration.setDefaultLeastFor(Duration.parse(this.getDefaultLeastFor()));
        }
        if(null!=this.getDefaultMostFor() && !"".equals(this.getDefaultMostFor().trim())){
            configuration.setDefaultMostFor(Duration.parse(this.getDefaultMostFor()));
        }
        return configuration;
    }

    @Override
    public String toString() {
        return "MeeTimedProperties{" +
                "shed='" + shed + '\'' +
                ", startupDelay=" + startupDelay +
                ", applicationName='" + applicationName + '\'' +
                ", isolationLevel=" + isolationLevel +
                ", useDbTime=" + useDbTime +
                ", timeZone='" + timeZone + '\'' +
                ", columnNames=" + columnNames +
                ", appColumnNames=" + appColumnNames +
                ", tableName='" + tableName + '\'' +
                ", tableAppName='" + tableAppName + '\'' +
                ", lockedByType=" + lockedByType +
                ", defaultMostFor='" + defaultMostFor + '\'' +
                ", defaultLeastFor='" + defaultLeastFor + '\'' +
                '}';
    }


}
