package com.mee.timed.data;

/**
 * AppColumnNames
 *
 * @author shaoow
 * @version 1.0
 * @className AppColumnNames
 * @date 2024/6/13 9:24
 */
public final class AppColumnNames {
    private final String application;
    private final String hostIP;
    private final String hostName;
    private final String state;
    private final String updateTime;

    public AppColumnNames(String application, String hostIP, String hostName, String state, String updateTime) {
        this.application=application;
        this.hostIP=hostIP;
        this.hostName=hostName;
        this.state=state;
        this.updateTime=updateTime;
    }

    public String getApplication() {
        return application;
    }

    public String getHostIP() {
        return hostIP;
    }

    public String getHostName() {
        return hostName;
    }

    public String getState() {
        return state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "AppColumnNames{" +
                "application='" + application + '\'' +
                ", hostIP='" + hostIP + '\'' +
                ", hostName='" + hostName + '\'' +
                ", state='" + state + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}