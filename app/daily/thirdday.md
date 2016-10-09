# 第三天的制作

## 分析知乎主界面所需要的功能

我们继续制作第二天未完成的任务：数据的显示

1、Fragment的切换

2、首先的轮播栏

3、上拉滑动、下拉刷新，并带有页面滚动效果

4、文章简介的显示

5、文章简介的阴影绘制

6、修复在无网环境下加载SplashActivity的图片导致停止在SplashActivity页面的bug

## 分析每一部分所需要的技术

### 一、Fragment的切换

首先判断Bundle是否存在，如果存在则使用FragmentManager.findFragmentByTag()获取Fragment。（解决Framgnet重影的问题）。如果不存在，则创建全部的Fragment并将其加入到FramgnetManager中，并隐藏。最后显示HomePageFragment。之后切换的是后只需要判断切换界面是否与当前界面相同，不相同则切换(show()和hide()方法)

### 二、添加轮播栏

由于时间不充裕，使用了外部依赖控件（compile 'com.github.dongjunkun:BannerLayout:1.0.3'）希望以后有时间可以自制
一个轮播栏。然后通过RemoteService,获取数据，并显示。
(需要自制，未完成)

### 二、上划加载和下拉刷新，并带有页面滚动效果

引用了外部依赖控件(compile 'homhomlin.lib:ptrrv-library:1.3.1')
(需要自制，未完成)

### 四、文章简介显示

首先我们知道功能是设置文章显示，就想到使用ListView与RecyclerView。但是我们发现我们需要把日期算进去，但是日期不属于文章简介的内容。所以我想到了两种办法。这里本人选择使用RecyclerView,仅仅是为了练习RecyclerView。

<strong>第一种办法</strong>：每次接受的数据有日期和文章简介的列表。那么可以把数据看成一个清单（list）将文章简介的列表当做清单的数据。那么这个清单就可以看做一个RecyclerView的一个单元数据，当RecyclerView初始化清单的时候，清单再将自己的列表放入清单中。但是在实现过程中发现这种方法太麻烦，且不太适用于引用的外部控件。

麻烦：由于ListView与RecyclerView的原理是根据getItem()数量来创建清单的list.xml的，之后将创建的清单存入缓存中。比如说，当pos == 0的时候创建了一个list.xml的实例，之后再回到pos == 0处，就不会再创建list.xml的实例了，而是从缓冲池中获取之前pos == 0时创建的list.xml实例。那么当第一次刷新的时候通过addView()将列表的news.xml添加到list.xml中，当第二次刷新的时候，又会将之前的数据再添加到list.xml中。

解决办法：在二次添加之前移除第一次添加的列表...整个过程需要不断的使用addView()与removeView()需要不断的调用requestLayout()重新绘制界面。非常的耗时与浪费资源。并且为ReyclerView封装的点击监听也不适用了。(requestLayout()的原理，RecyclerView的原理)

引用控件失效：引用的控件只有当Adapter的数据达到10以上的时候才会触发loadMore()方法。由于不断的改变list实例自身的大小，刷新无法判断位置，失效。

<strong>第二种办法</strong>：将日期作为文章简介的一部分，创建StoryBriefEntity类。然后通过type判断，当前是显示日期还是显示文章简介。
这种方法比刚才的方法方便了非常多。只需要将所有的story加上date就可以了。然后在ReyclerView中判断显示哪个的问题。最后设置ImageView的异步加载，为了减小内存的消耗，我们采取压缩图片的方式，将图片压缩到ImageView的大小。但是不能使用ImageView.getMeasureWidth()/getMeasureHeight()进行获取ImageView
的宽/高。或者说一定要在View.setVisiblity()之前进行获取宽/高。因为，当调用setVisiblity()时候也会调用requestLayout()对界面进行重新测量大小，且是异步进行的，所以如果在setVisiblity()很可能获取不到宽/高。(requestLayout()的原理)

解决办法：

1、将ImageView的宽/高，设置在dimen.xml中，然后获取

2、将imageView.getMeasureWidth()方法的调用放在setVisiblity()之前。

当这一切解决了，发现需要调整date的显示因为原始date是20161006这样的形式，当显示的时候需要10月06日 星期四 这样的形式。那么如何转换呢？ 

<strong>第一种办法</strong>：利用String类的方法，将数据按照4,2,2的形式截取下来。然后返回日期就可以了。

优点：简单，方便。              缺点：不能完全的满足要求

<strong>第二种办法</strong>：通过SimpleDateFormat，将String转换成Date，再将Date转换成Calendler，利用Calender获取其年、月、日、星期。但是Calender也是有问题的。获取到的月份是从0开始计算的（0表示一月）。几号不是01,02这样的形式，而是整型1,2这样的形式。还有获取的星期也是整型，并且是按照星期日 == 1，开始计算的（星期一 == 2.。。以次类推）。所以，我们修改对这基础进行修改，返回正确的值

### 五、文章简介的阴影绘制

通过使用layout-list，将半透明阴影放在底部，然后background放在其上

## 总结及效果预览

1、理解了requestLayout()基本原理（会给自身一个标记，让后将事件传递给根父类，根父类通过判别是否有标记来重绘控件）

2、RecyclerView的基本原理（缓冲区）。并且知道，最好不要再ReyclerView中再将View塞进其ViewHolder中

3、DateFormat的使用（Calender的坑有点多。。）

效果预览：

![](https://github.com/newbiechen1024/BiggerDaily/blob/develop/app/screenshot/my_main_activity_2.gif)
