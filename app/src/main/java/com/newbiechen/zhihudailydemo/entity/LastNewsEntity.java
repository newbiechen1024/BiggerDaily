package com.newbiechen.zhihudailydemo.entity;

import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class LastNewsEntity {
    /**
     * date : 20161004
     * stories : [{"images":["http://pic4.zhimg.com/957c7507ff88a09a31f71818e7f5ea0f.jpg"],"type":0,"id":8850639,"ga_prefix":"100411","title":"粗中有细的生疏凉拌，那就是乡土版的蔬菜沙拉啊"},{"images":["http://pic3.zhimg.com/a707b8621c83b3637df57b4d2e56135e.jpg"],"type":0,"id":8847443,"ga_prefix":"100410","title":"真想锻炼，上个厕所的时间都能练出好身材"},{"images":["http://pic1.zhimg.com/609c0b1634fce61d6a8c4c3aee920800.jpg"],"type":0,"id":8831440,"ga_prefix":"100409","title":"为什么有些人老了依然可以思维敏捷？"},{"images":["http://pic1.zhimg.com/0a23f9603d86be27a60fa4423510e41c.jpg"],"type":0,"id":8852172,"ga_prefix":"100408","title":"所谓「市值蒸发」，意思是钱就这样凭空消失了？"},{"images":["http://pic1.zhimg.com/45a64981474d38d82ce23d67904f53f8.jpg"],"type":0,"id":8851669,"ga_prefix":"100407","title":"你所知道的急救知识，也许是错误的"},{"images":["http://pic2.zhimg.com/136af6d7201cec0c5df21b0622327e8d.jpg"],"type":0,"id":8849728,"ga_prefix":"100407","title":"欣赏下昆虫界的谍战片，或许比电影好看"},{"images":["http://pic4.zhimg.com/46d08934557590c9be18f4a2bf1ff4cf.jpg"],"type":0,"id":8831233,"ga_prefix":"100407","title":"以为在法国酒庄工作会喝很多酒，没想到有时臭得没人靠近"},{"images":["http://pic4.zhimg.com/26017ba4cf29b41e028b8a43697b00f7.jpg"],"type":0,"id":8852608,"ga_prefix":"100407","title":"读读日报 24 小时热门 TOP 5 · 诺奖得主的工作，让病变细胞自杀"},{"images":["http://pic1.zhimg.com/c8fca4655b5aa6cea769e557523cf09c.jpg"],"type":0,"id":8852706,"ga_prefix":"100406","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic3.zhimg.com/26aff97ded87a80fd83c018b55d6a506.jpg","type":0,"id":8847443,"ga_prefix":"100410","title":"真想锻炼，上个厕所的时间都能练出好身材"},{"image":"http://pic3.zhimg.com/8adcbd626188b558d79c102ad131800e.jpg","type":0,"id":8831440,"ga_prefix":"100409","title":"为什么有些人老了依然可以思维敏捷？"},{"image":"http://pic2.zhimg.com/479e4e119d9e97d83ea0ba6f6bda42ed.jpg","type":0,"id":8851669,"ga_prefix":"100407","title":"你所知道的急救知识，也许是错误的"},{"image":"http://pic1.zhimg.com/dde418402d4394618554dd6ee27b1ea8.jpg","type":0,"id":8852608,"ga_prefix":"100407","title":"读读日报 24 小时热门 TOP 5 · 诺奖得主的工作，让病变细胞自杀"},{"image":"http://pic2.zhimg.com/6b963c60d0155c80b35965079d4d88cd.jpg","type":0,"id":8852256,"ga_prefix":"100321","title":"刚出炉的诺贝尔奖，通俗点讲是「我看自己很好吃，那我开动啦！」"}]
     */

    private String date;
    /**
     * images : ["http://pic4.zhimg.com/957c7507ff88a09a31f71818e7f5ea0f.jpg"]
     * type : 0
     * id : 8850639
     * ga_prefix : 100411
     * title : 粗中有细的生疏凉拌，那就是乡土版的蔬菜沙拉啊
     */

    private List<StoriesBean> stories;
    /**
     * image : http://pic3.zhimg.com/26aff97ded87a80fd83c018b55d6a506.jpg
     * type : 0
     * id : 8847443
     * ga_prefix : 100410
     * title : 真想锻炼，上个厕所的时间都能练出好身材
     */

    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;
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
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
}
