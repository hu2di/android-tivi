package com.blogspot.huyhungdinh.tv.model;

import java.io.Serializable;

/**
 * Created by HUNGDH on 5/25/2016.
 */
public class Schedule implements Serializable{
    private int id;
    private int idChannel;
    private String content;
    private String time;

    public Schedule(int id, int idChannel, String content, String time) {
        this.id = id;
        this.idChannel = idChannel;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
