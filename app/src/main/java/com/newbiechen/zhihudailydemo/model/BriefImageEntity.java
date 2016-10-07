package com.newbiechen.zhihudailydemo.model;

import org.litepal.crud.DataSupport;

/**
 * Created by PC on 2016/10/7.
 */

public class BriefImageEntity extends DataSupport{

    private String imgUrl;

    private int storyBean_id;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStoryBean_id() {
        return storyBean_id;
    }

    public void setStoryBean_id(int storyBean_id) {
        this.storyBean_id = storyBean_id;
    }

    @Override
    public String toString() {
        return "BriefImageEntity{" +
                "imgUrl='" + imgUrl + '\'' +
                ", storyBean_id='" + storyBean_id + '\'' +
                '}';
    }
}
