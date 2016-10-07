package com.newbiechen.zhihudailydemo.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class StoriesBean extends DataSupport{
    @SerializedName(value = "id")
    private int content_id;
    private int type;
    private String ga_prefix;
    private String title;
    private List<String> images;

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}