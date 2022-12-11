package com.risk.riskcontrol.entity;

public class ContactInfo {

    private String name = "";
    private String mobile = "";
    private String lastUpdateTime = "";//上次更新时间
    private String record_create_time = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRecord_create_time() {
        return record_create_time;
    }

    public void setRecord_create_time(String record_create_time) {
        this.record_create_time = record_create_time;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", record_create_time='" + record_create_time + '\'' +
                '}';
    }
}
