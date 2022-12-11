package com.risk.riskcontrol.entity;

/**
 * @author Nevio
 * on 2022/12/2
 */
public class AlbumData {

    private String name =""; //照片名称
    private String author =""; //拍摄者（无拍摄者则获取手机品牌）
    private String height =""; //照片高度，单位：像素
    private String width =""; //照片宽度，单位：像素
    private String longitude =""; //经度
    private String latitude =""; //纬度
    private String model =""; //拍摄照片的手机机型
    private String addTime =""; //拍摄时间(格式：yyyy-mm-dd HH:mm:ss)
    private String updateTime =""; //修改时间(格式：yyyy-mm-dd HH:mm:ss)
    private String save_time =""; //保存时间
    private String orientation =""; //照片方向
    private String x_resolution =""; //水平方向分辨率
    private String y_resolution =""; //垂直方向分辨率
    private String gps_altitude =""; //海拔
    private String gps_processing_method =""; //gps定位方式
    private String lens_make =""; //镜头生产商
    private String lens_model =""; //镜头模型
    private String focal_length =""; //镜头焦距
    private String flash =""; //闪光灯状态
    private String software =""; //软件
    private long id = 0;//图片存储id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSave_time() {
        return save_time;
    }

    public void setSave_time(String save_time) {
        this.save_time = save_time;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getX_resolution() {
        return x_resolution;
    }

    public void setX_resolution(String x_resolution) {
        this.x_resolution = x_resolution;
    }

    public String getY_resolution() {
        return y_resolution;
    }

    public void setY_resolution(String y_resolution) {
        this.y_resolution = y_resolution;
    }

    public String getGps_altitude() {
        return gps_altitude;
    }

    public void setGps_altitude(String gps_altitude) {
        this.gps_altitude = gps_altitude;
    }

    public String getGps_processing_method() {
        return gps_processing_method;
    }

    public void setGps_processing_method(String gps_processing_method) {
        this.gps_processing_method = gps_processing_method;
    }

    public String getLens_make() {
        return lens_make;
    }

    public void setLens_make(String lens_make) {
        this.lens_make = lens_make;
    }

    public String getLens_model() {
        return lens_model;
    }

    public void setLens_model(String lens_model) {
        this.lens_model = lens_model;
    }

    public String getFocal_length() {
        return focal_length;
    }

    public void setFocal_length(String focal_length) {
        this.focal_length = focal_length;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", model='" + model + '\'' +
                ", addTime='" + addTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", save_time='" + save_time + '\'' +
                ", orientation='" + orientation + '\'' +
                ", x_resolution='" + x_resolution + '\'' +
                ", y_resolution='" + y_resolution + '\'' +
                ", gps_altitude='" + gps_altitude + '\'' +
                ", gps_processing_method='" + gps_processing_method + '\'' +
                ", lens_make='" + lens_make + '\'' +
                ", lens_model='" + lens_model + '\'' +
                ", focal_length='" + focal_length + '\'' +
                ", flash='" + flash + '\'' +
                ", software='" + software + '\'' +
                ", id=" + id +
                '}';
    }
}
