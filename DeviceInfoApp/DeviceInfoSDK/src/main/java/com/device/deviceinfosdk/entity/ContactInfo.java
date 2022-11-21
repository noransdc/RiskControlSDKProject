package com.device.deviceinfosdk.entity;

public class ContactInfo {

    private String name = "";
    private String mobile = "";
    private String lastUpdateTime = "";//上次更新时间
    private String create_time = "";

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
