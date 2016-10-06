package com.newbiechen.zhihudailydemo.entity;

import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class BeforeNewsEntity {

    /**
     * date : 20141003
     * stories : [{"images":["http://pic1.zhimg.com/fdab4e552d3cdc9db745c9634a60ab5b.jpg"],"type":0,"id":4195104,"ga_prefix":"100322","title":"深夜食堂 · 要生活片吗？"},{"title":"正确无痛的增肥法：去台湾做美食饕餮（多图）","ga_prefix":"100321","images":["http://pic4.zhimg.com/b3e489f07e36fad96dbe852dee569c5a.jpg"],"multipic":true,"type":0,"id":4197669},{"title":"拥有伟大的对手，是费德勒的幸与不幸（多图）","ga_prefix":"100320","images":["http://pic4.zhimg.com/ef0220a32eccf495f6ab779fec6b2280.jpg"],"multipic":true,"type":0,"id":4200238},{"images":["http://pic2.zhimg.com/2abc839482bd884251a0b69b481d95c4.jpg"],"type":0,"id":4194722,"ga_prefix":"100318","title":"不能既丢了自行车又丢了女朋友"},{"images":["http://pic4.zhimg.com/5b2e83b4cc006c37c8878eb84c3570ec.jpg"],"type":0,"id":4205940,"ga_prefix":"100317","title":"家有小孩的，置备哪款安全座椅更安全靠谱？"},{"images":["http://pic4.zhimg.com/545f7190142ed180ab58b00c6975ac37.jpg"],"type":0,"id":4193068,"ga_prefix":"100316","title":"至尊宝是在哪个瞬间喜欢上了紫霞仙子？"},{"images":["http://pic4.zhimg.com/235ebb4fa1adb0cbe58bb226f30f4a1a.jpg"],"type":0,"id":4206494,"ga_prefix":"100314","title":"如何加入网络字幕组？"},{"images":["http://pic2.zhimg.com/afc663ffc2d64b47489d5211681eceaa.jpg"],"type":0,"id":4205774,"ga_prefix":"100313","title":"《海贼王》的后大航海时代，一个群魔乱舞的时代"},{"title":"广大女性的福音：画眉几步轻松搞定（多图）","ga_prefix":"100312","images":["http://pic3.zhimg.com/676e0f2036b9e047d41e732c5c4da3d2.jpg"],"multipic":true,"type":0,"id":4191800},{"title":"拿块上好的玉石雕个海绵宝宝，还值钱吗？（多图）","ga_prefix":"100311","images":["http://pic2.zhimg.com/2b9ba5adf314bd1dbb7dfe910edda2f5.jpg"],"multipic":true,"type":0,"id":4206218},{"images":["http://pic2.zhimg.com/3050b7e209801b4958de14ea8df3eef2.jpg"],"type":0,"id":4194596,"ga_prefix":"100310","title":"日常生活中有哪些十分钟学会但是终生受用的技能？"},{"title":"细数大热日剧 Legal High 中忍者各种变装（多图）","ga_prefix":"100309","images":["http://pic1.zhimg.com/f2e0618ba4aa24f75dfc0e81732bf969.jpg"],"multipic":true,"type":0,"id":4205477},{"images":["http://pic2.zhimg.com/f1f917e2d08a064f7568a4f4a7b8d5a1.jpg"],"type":0,"id":4201250,"ga_prefix":"100308","title":"买辆车前，做好试驾功课得关注这几方面"},{"images":["http://pic1.zhimg.com/e59b52cde3a8ba09daffcb61b0dd67fa.jpg"],"type":0,"id":4205758,"ga_prefix":"100307","title":"「健康产业」在中国，前景还是很值得期待的"},{"images":["http://pic3.zhimg.com/2cde501d2ea9356072e962126b291f8a.jpg"],"type":0,"id":4193518,"ga_prefix":"100307","title":"电影院无论坐在哪里票价都一样，演唱会却不同"},{"images":["http://pic2.zhimg.com/2800a4e51a4cca56930815d16f1ad62e.jpg"],"type":0,"id":4206126,"ga_prefix":"100307","title":"14-15 赛季骑士队前瞻：勒布朗能做到吗？"},{"images":["http://pic4.zhimg.com/c7b5ee67520daf26053e995ba190da3c.jpg"],"type":0,"id":4202458,"ga_prefix":"100307","title":"App Store 账号修改区域，必须花光所有的余额吗？"},{"images":["http://pic1.zhimg.com/8963f3662302745c5db46988ccb7e742.jpg"],"type":0,"id":4206226,"ga_prefix":"100306","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    /**
     * images : ["http://pic1.zhimg.com/fdab4e552d3cdc9db745c9634a60ab5b.jpg"]
     * type : 0
     * id : 4195104
     * ga_prefix : 100322
     * title : 深夜食堂 · 要生活片吗？
     */

    private List<StoriesBean> stories;

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
}
