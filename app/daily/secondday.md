# 第二天的制作

## 分析知乎主界面所需要的功能

![](https://github.com/newbiechen1024/BiggerDaily/blob/develop/app/screenshot/main_activity.gif)

1、添加Toolbar，并为Toolbar设置menu

2、添加DrawerLayout

3、设置侧滑栏的头部。设置侧滑栏的内容

3、DrawerLayout与Toolbar的交互

## 分析每一部分所需要的技术

### 一、添加Toolbar。

首先Toolbar并非Android自带的ActionBar，所以需要设置XML加载，之后并且利用代码setActionBar()将其设置为Activity的ActionBar。在做这一切之前我们可以纵观

整个知乎日报，发现，其Activity上，大部分都有个Toolabar，并且每个Toolbar上都有返回按钮。所以我们应该将Toolbar设置为公用的xml，并在AppBaseActivity中添
加Toolabar为Activity的ActionBar。并设置返回键getSupportActionBar.setDisplayHomeAsUpEnabled(true)。之后就是设置Toolbar的menu，通过创建menu.xml来
来设置Toolbar的点击按钮与图片。但是这里遇到了一些问题，知乎的Toolbar显示的图标的颜色都是白色的，但是Android默认的Toolbar显示的居然是黑色的。那么如何
将白色的图标转换成黑色。

首先如何定义返回键的图标，根据原理我们知道android的默认样式躲在< style > 中，那么我们只需要修改< style > 标签中关于图标的一部分就好了。

    <style name="AppTheme.DrawerArrowToggle" parent="Base.Widget.AppCompat.DrawerArrowToggle">
        <item name="color">@android:color/white</item>
    </style>

问题：不是对android的 < style > 很熟练的人，很难知道，我该继承哪个 < style > ，然后用哪个属性能够达到目的。

第二种方法：返回标签叫做navigationIcon，那么只需要使用Toolbar.setNavigationIcon()就能替换返回键的图标，将图标替换成白色的箭头就可以了

然后如何设置Toolbar溢出的图标，也是通过使用 < style > 

    <style name="OverflowButtonStyle" parent="@android:style/Widget.ActionButton.Overflow">
        <item name="android:src">@mipmap/abc_ic_menu_moreoverflow_mtrl_alpha</item>
    </style>

最后加载在主 < style > 中

    <item name="drawerArrowStyle">@style/AppTheme.DrawerArrowToggle</item>
    <item name="actionOverflowButtonStyle">@style/OverflowButtonStyle</item>

那么如何设置Toolbar title的颜色呢？

利用Toolbar.setTitleColor()设置。

遇到的问题：对于 < style > 标签之间的关系不熟练，不知道继承哪一个 < style > 重写哪一个属性，才能设置Toolbar

### 二、添加DrawerLayout

记住DrawerLayout在xml中的时候，然后开始创建侧滑栏的头部，本来想用NavigationView设置侧滑栏的点击按钮的，但是发现NavigtaionView只能设置为左图、右文字的，但是知乎日报使用的是左文字右图的，并且排版相同。所以就不能使用NaigationView类，因为有点类似于ListView的线性滑动，所以准备使用自定义的RecyclerView
（原理：1、在xml中添加scrollView，并重写其onInterceptEvent()当，为下拉的时候位置小于HeaderView的高度的时候，直接交给ScrollView处理，如果大于HeaderView那么就
将Event不交给ScrollView处理，并将event向下传递给RecylerView。但是当上划的时候，需要判断是否RecyclerView的滑动到达顶部了，然后才将事件交给ScrollView处理）

优点：逻辑清晰，直接将Header与RecyclerView装进ScrollView中就可以了。

缺点：复杂

(原理2、创建HeaderAdapter基础类，设置当position == 0 的时候使用HeaderViewHolder)

优点：简单实用

缺点：逻辑不太清晰，因为Adapter不应该使用来装Header的，感觉这一点很奇怪。

（该控件未制作。。。）

### 三、DrawerLayout与Toolbar的交互

通过使用ActionBarToggle，ActionBarToggle其实是重写了DrawerLayout的onDrawerListener()。所以能够在Toolbar上交互。之后通过mToggle.syncState();设置
指示器（左上角，点击打开DrawerLayout的按钮，本来是返回键的）

## 总结问题

1、熟练掌握 < style > 之间的关系(Android提供的基础属性，需要用到的基础 < style > 父类)， < style > 的继承和命名

2、还未制作DrawerLayout的按钮类

## 自制的效果图

![](https://github.com/newbiechen1024/BiggerDaily/blob/develop/app/screenshot/my_main_activity_1.gif)