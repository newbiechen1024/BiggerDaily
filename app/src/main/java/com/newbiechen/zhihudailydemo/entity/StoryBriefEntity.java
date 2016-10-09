package com.newbiechen.zhihudailydemo.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by PC on 2016/10/6.
 */

public class StoryBriefEntity extends DataSupport{
    /*显示日期*/
    public static final int TYPE_DATE = 0;
    /*显示文章*/
    public static final int TYPE_STORY_BRIEF = 1;

    private String date;

    private StoriesEntity storiesEntity;

    private int type;

    public StoriesEntity getStoriesEntity() {
        return storiesEntity;
    }

    public void setStoriesEntity(StoriesEntity storiesEntity) {
        this.storiesEntity = storiesEntity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Story{" +
                "storiesBean=" + storiesEntity +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
