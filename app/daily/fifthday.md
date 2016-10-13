# 完成的需求：

## 1、问题：ListView的Header无法点击的问题

原先代码：
```java
        //添加头部
        View header = LayoutInflater.from(this)
                .inflate(R.layout.slide_header,mLvSlide,false);
        mLvSlide.addHeaderView(header);
```
解决代码：
```java
        //添加头部
        View header = LayoutInflater.from(this)
                .inflate(R.layout.slide_header,mLvSlide,false);
        mLvSlide.addHeaderView(header,null,false);
```
原理：

在ListView中添加addHeaderView 有两种方法：

    void addHeaderView (View v)  
    void addHeaderView (View v, Object data, boolean isSelectable)
    
第一种方法：添加View到ListView中，并当做ListView的第一个Item。就是相当于一个Item，接收ListView的点击事件，并且占用item的总数。

第二种方法：添加View到ListVIew中，Object data表示关联到View上的数据，**boolean isSelectable**最重要的参数，表是header的点击事件是否交给ListView。如果为true 则实现 第一种方法的效果。如果为false则header自己消耗点击事件，不交给ListView。

**注**：官方文档写了this method could only be called before setting the adapter with setAdapter(ListAdapter)。表示addHeaderView()必须在setAdapter()前。

为什么出错：

不知道ListView在addHeader()有两个方法。。。

## 问题：Toolbar的返回键为黑色。需求：返回键为白色的

原先的代码：
```xml
    <!--在style.xml中-->
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        ...    
        <!--返回键样式-->
        <item name="drawerArrowStyle">@style/AppTheme.DrawerArrowToggle</item>
    </style>
    
    <style name="AppBase.DrawArrowToggle" parent="Base.Widget.AppCompat.DrawerArrowToggle">
      <item name="color">@android:color/white</item>
    </style>
```

解决的代码：

```xml
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        ...
        <item name="colorControlNormal">@android:color/white</item>
    </style>
```

原理：

熟悉的使用\<style\>中属性的继承，并知道其效果。

说明：属性 name="colorControlNormal" 能够改变toolbar上Btn和图标的颜色。

做不出来的原因：

1、原先以为\<style\>的DrawerArrowToggle能够让所有的箭头变成white。但是其实只是**首页**的指示器有变化。其他子Activity的back键都没有变化。

2、对\<style\>的继承与属性不太了解。慢慢在实践中积累吧。

## 问题：如何在Toolbar上自定义menu。 实现知乎日报分享、评论、点赞、收藏的功能。

制作的知识要点：

1、如何使用ActionProvider类。该类用来自定义Menu而是用

ActionProvider的使用：

1、创建自定义Menu：View 继承 ActionProvider。不要修改ActionProvider的构造方法。如果需要参数，通过设置set and get方法来设置参数。

2、ActionProvider通过menu.xml链接到Toolbars上：将自定义的menu 通过 app:actionProviderClass="绝对包路径" 来添加ActionProvider。 千万不要使用android:actionProviderClass=""

3、通过

    MenuItem praiseMenu = menu.findItem(R.id.theme_content_praise);
    mApPraise = (PraiseActionProvider) MenuItemCompat.getActionProvider(praiseMenu);

来获取ActionProvider，**注意：**这个过程虽然创建了mApPraise，但是使用该类中的方法，必须是在onCreateOptionMenu()完成之后调用，一般都是推荐在onWindowFocusChanged()中设置，例：
```java
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mApComment.setCommentCount(10);
        mApPraise.setPraiseCount(20);
        mApPraise.setIsSelect(false);
    }
```  

做不出来的原因：不知道如何自定义Menu。现在学会了。。

## 解决Fragment中的ThemeBrief条目框，有空白的问题

原先代码：
```xml
    <TextView
    android:id="@+id/brief_tv_title"
    android:layout_width="0dp"
    android:layout_weight="1"
    android:layout_height="wrap_content"
    android:ellipsize="end"
    android:line="4"
    android:textSize="18sp"
    android:textColor="@color/black"/>
```

解决代码：
```xml
    <TextView
    android:id="@+id/brief_tv_title"
    android:layout_width="0dp"
    android:layout_weight="1"
    android:layout_height="wrap_content"
    android:ellipsize="end"
    android:maxlines="4"
    android:textSize="18sp"
    android:textColor="@color/black"/>
```
原理：
在原先代码中：android:line="4"表示TextView有4行， 每行都占据默认高度，所以才会留有空白区域。
修改之后：android:maxLines="4"表示最多有四行，当字必须换行的时候才会出现一行占据高度。

错误原因：
以为android:line=""是根据字的换行占据高度的，其实maxLines=""才是。
