package com.device.deviceinfosdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceBattery implements Parcelable {

    private String battery_max = "";//最大电量
    private String battery_now = "";//当前电量
    private String battery_level = "";//电量百分比
    private int health = 0;//电池健康度
    private int status = 0;//电池状态
    private String temperature = "";//电池温度
    private String technology = "";//电池技术
    private int plugged = 0; //充电类型

    public String getBattery_max() {
        return battery_max;
    }

    public void setBattery_max(String battery_max) {
        this.battery_max = battery_max;
    }

    public String getBattery_now() {
        return battery_now;
    }

    public void setBattery_now(String battery_now) {
        this.battery_now = battery_now;
    }

    public String getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(String battery_level) {
        this.battery_level = battery_level;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getPlugged() {
        return plugged;
    }

    public void setPlugged(int plugged) {
        this.plugged = plugged;
    }

    @Override
    public String toString() {
        return "DeviceBattery{" +
                "battery_max='" + battery_max + '\'' +
                ", battery_now='" + battery_now + '\'' +
                ", battery_level='" + battery_level + '\'' +
                ", health=" + health +
                ", status=" + status +
                ", temperature='" + temperature + '\'' +
                ", technology='" + technology + '\'' +
                ", plugged=" + plugged +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.battery_max);
        dest.writeString(this.battery_now);
        dest.writeString(this.battery_level);
        dest.writeInt(this.health);
        dest.writeInt(this.status);
        dest.writeString(this.temperature);
        dest.writeString(this.technology);
        dest.writeInt(this.plugged);
    }

    public void readFromParcel(Parcel source) {
        this.battery_max = source.readString();
        this.battery_now = source.readString();
        this.battery_level = source.readString();
        this.health = source.readInt();
        this.status = source.readInt();
        this.temperature = source.readString();
        this.technology = source.readString();
        this.plugged = source.readInt();
    }

    public DeviceBattery() {
    }

    protected DeviceBattery(Parcel in) {
        this.battery_max = in.readString();
        this.battery_now = in.readString();
        this.battery_level = in.readString();
        this.health = in.readInt();
        this.status = in.readInt();
        this.temperature = in.readString();
        this.technology = in.readString();
        this.plugged = in.readInt();
    }

    public static final Creator<DeviceBattery> CREATOR = new Creator<DeviceBattery>() {
        @Override
        public DeviceBattery createFromParcel(Parcel source) {
            return new DeviceBattery(source);
        }

        @Override
        public DeviceBattery[] newArray(int size) {
            return new DeviceBattery[size];
        }
    };
}
