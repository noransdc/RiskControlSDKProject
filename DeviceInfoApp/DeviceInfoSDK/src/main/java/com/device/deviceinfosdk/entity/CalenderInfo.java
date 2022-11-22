package com.fuerte.riskcontrol.entity;

import com.google.android.gms.common.internal.Objects;

public class CalenderInfo {


    private String id = "";//id
    private String title = "";//标题
    private String content = "";//内容
    private String start_time = "";//开始时间  yyyy-mm-dd HH:mm:ss
    private String end_time = "";//结束时间yyyy-mm-dd HH:mm:ss


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "CalenderInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}
