package com.fuerte.riskcontrol.entity;


public class LocationInfo {


    private String geo_time ="";//获取时间  yyyy-mm-dd HH:mm:ss
    private String latitude ="";//当前纬度
    private String longtitude ="";//当前经度
    private String location ="";//当前位置名称
    private String gps_address_province ="";//gps解析出来的省 sublocality
    private String gps_address_city ="";//gps解析出来的城市 locailty
    private String gps_address_street ="";//gps解析的地址 maddresslines
    private String gps_address_country;

    public String getGeo_time() {
        return geo_time;
    }

    public void setGeo_time(String geo_time) {
        this.geo_time = geo_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGps_address_province() {
        return gps_address_province;
    }

    public void setGps_address_province(String gps_address_province) {
        this.gps_address_province = gps_address_province;
    }

    public String getGps_address_city() {
        return gps_address_city;
    }

    public void setGps_address_city(String gps_address_city) {
        this.gps_address_city = gps_address_city;
    }

    public String getGps_address_street() {
        return gps_address_street;
    }

    public void setGps_address_street(String gps_address_street) {
        this.gps_address_street = gps_address_street;
    }

    public String getGps_address_country() {
        return gps_address_country;
    }

    public void setGps_address_country(String gps_address_country) {
        this.gps_address_country = gps_address_country;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "geo_time='" + geo_time + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", location='" + location + '\'' +
                ", gps_address_province='" + gps_address_province + '\'' +
                ", gps_address_city='" + gps_address_city + '\'' +
                ", gps_address_street='" + gps_address_street + '\'' +
                ", gps_address_country='" + gps_address_country + '\'' +
                '}';
    }

}
