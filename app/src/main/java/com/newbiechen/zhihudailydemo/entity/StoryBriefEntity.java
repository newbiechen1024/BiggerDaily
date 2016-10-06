package com.newbiechen.zhihudailydemo.entity;

/**
 * Created by PC on 2016/10/6.
 */

public class StoryBriefEntity {
    /*显示日期*/
    public static final int TYPE_DATE = 0;
    /*显示文章*/
    public static final int TYPE_STORY_BRIEF = 1;

    private String date;

    private StoriesBean storiesBean;

    private int type;


    public StoriesBean getStoriesBean() {
        return storiesBean;
    }

    public void setStoriesBean(StoriesBean storiesBean) {
        this.storiesBean = storiesBean;
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
                "storiesBean=" + storiesBean +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
