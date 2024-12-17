package com.mee.timed.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * JobFieldsEntity
 *
 * @author shaoow
 * @version 1.0
 * @className JobFieldsEntity
 * @date 2024/6/17 15:13
 */
public class JobEntity implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private LocalDateTime lockUntil;
    private LocalDateTime lockedAt;
    private Boolean isUTC;
    private String lockedBy;
    private String application;
    private String hostIp;
    private String state;
    private String updateTime;
    private String data;
    private String label;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(LocalDateTime lockUntil) {
        this.lockUntil = lockUntil;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getIsUTC() {
        return isUTC;
    }

    public void setIsUTC(Boolean UTC) {
        isUTC = UTC;
    }

    @Override
    public String toString() {
        return "JobEntity{" +
                "name='" + name + '\'' +
                ", lockUntil=" + lockUntil +
                ", lockedAt=" + lockedAt +
                ", isUTC=" + isUTC +
                ", lockedBy='" + lockedBy + '\'' +
                ", application='" + application + '\'' +
                ", hostIp='" + hostIp + '\'' +
                ", state='" + state + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", data='" + data + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
