# 第三天的制作

## 分析知乎主界面所需要的功能

我们继续制作第二天未完成的任务：数据的显示

1、Fragment的切换

2、首先的轮播栏

3、文章简介的上划加载和下拉刷新，Header与RecyclerView的滑动。

4、文章简介的显示

5、文章简介的阴影绘制

6、修复在无网环境下加载SplashActivity的图片导致停止在SplashActivity页面的bug

## 分析每一部分所需要的技术

### 一、Fragment的切换

### 二、添加轮播栏

由于时间不充裕，使用了外部依赖控件（compile 'com.github.dongjunkun:BannerLayout:1.0.3'）希望以后有时间可以自制
一个轮播栏。然后通过RemoteService,获取数据，并显示。
(需要自制，未完成)

### 二、上划加载和下拉刷新，Header与RecyclerView的滑动

引用了外部依赖控件(compile 'homhomlin.lib:ptrrv-library:1.3.1')
(需要自制，未完成)

### 四、文章简介显示

一看文章显示就想到了使用ListView与RecyclerView。为了练习Android的最新控件本人就使用了RecyclerView。日报每次的数
据都是存在日期的，每次日期的显示都在文章显示前。这显然不适用于普通的RecyclerView显示。我就想，每次获取的数据就是一个
新闻清单，每个清单里面才是文章简介显示的位置，所以我创建一个清单类，作为RecyclerView的数据和ViewHolder。然后将获取的
stories通过addView()的形式加入到清单中(requestLayout()的原理)。但是发现，当刷新的时候，由于Adapter的原理，生成的list.xml是不会重新create
的，只是当数据清除的时候，重新获取当前pos对应的list.xml修改数据而已，那么在list.xml中添加的stories.xml是不会消失
的，所以，当使用下拉刷新的时候，又会将数据加载到list.xml中，然而之前的数据还没删除。这个问题可以用ViewGroup.
removeViewInLayout解决。但是发现一个重大的问题，使用的框架，Recycler不会下滑刷新和上拉加载了。
（这个框架当数据小于10以下不会调用loadMore()方法）



第二种方法：
创建StoryEntity并设置Type，当Type为0时显示日期，当Type为1时显示文章。
（个人感觉这方法不好，因为总感觉逻辑上不符合。逻辑是，每篇文章其实都有一个日期标示，标示该文章属于哪个日期，只是显示的
时候显示哪个的问题罢了。但是服务器传过来的参数，是按照清单的方式传递的）
（但是这种方法比第一种方法简单，而且效率高）

### 五、文章简介的阴影绘制

## 总结及效果预览

效果预览：

https://github.com/newbiechen1024/BiggerDaily/blob/develop/app/screenshot/my_main_activity_2.gif)

