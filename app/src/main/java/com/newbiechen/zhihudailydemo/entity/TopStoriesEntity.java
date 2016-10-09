package com.newbiechen.zhihudailydemo.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by PC on 2016/10/8.
 */

public class TopStoriesEntity extends DataSupport{
    //防止与LitePal冲突
    @SerializedName(value = "id")
    private int content_id;
    private String image;
    private int type;
    private String ga_prefix;
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return content_id;
    }

    public void setId(int id) {
        this.content_id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
