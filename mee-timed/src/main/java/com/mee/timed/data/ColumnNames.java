package com.mee.timed.data;

import static java.util.Objects.requireNonNull;

/**
 * ColumnNames
 *
 * @author shaoow
 * @version 1.0
 * @className ColumnNames
 * @date 2024/6/13 9:25
 */

public final class ColumnNames {
    private final String name;
    private final String lockUntil;
    private final String lockedAt;
    private final String lockedBy;

    /***** 扩展参数 *****/
    // application 也即 schedName
    private final String application;
    private final String hostIP;
    private final String state;
    private final String updateTime;
    private final String data;
    private final String label;

    public ColumnNames(String name, String lockUntil, String lockedAt, String lockedBy) {
        this.name = requireNonNull(name, "'name' column name can not be null");
        this.lockUntil = requireNonNull(lockUntil, "'lockUntil' column name can not be null");
        this.lockedAt = requireNonNull(lockedAt, "'lockedAt' column name can not be null");
        this.lockedBy = requireNonNull(lockedBy, "'lockedBy' column name can not be null");
        this.application = "APPLICATION";
        this.hostIP = "HOST_IP";
        this.state = "STATE";
        this.updateTime = "UPDATE_TIME";
        this.data = "DATA";
        this.label = "LABEL";
    }
    public ColumnNames(String name, String lockUntil, String lockedAt, String lockedBy,String application, String hostIP, String state,String updateTime,String data,String label) {
        this.name = requireNonNull(name, "'name' column name can not be null");
        this.lockUntil = requireNonNull(lockUntil, "'lockUntil' column name can not be null");
        this.lockedAt = requireNonNull(lockedAt, "'lockedAt' column name can not be null");
        this.lockedBy = requireNonNull(lockedBy, "'lockedBy' column name can not be null");
        this.application = application;
        this.hostIP = hostIP;
        this.state = state;
        this.updateTime = updateTime;
        this.data = data;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public String getLockUntil() {
        return lockUntil;
    }

    public String getLockedAt() {
        return lockedAt;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public String getApplication() {
        return application;
    }
    public String getHostIP() {
        return hostIP;
    }
    public String getState() {
        return state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getData() {
        return data;
    }

    public String getLabel() {
        return label;
    }
}