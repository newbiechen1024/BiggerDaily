# 第四天的制作

## 分析知乎主界面所需要的功能

1、首先是主页广告数据、文章简介数据的加载与存储功能

2、改进ImageLoader会发生图片错乱的现象

3、文章详情页面的制作

4、文章侧滑栏的文章专栏按钮的制作

5、文章专栏页面的制作

## 分析每一部分所需要的技术

### 一、数据的加载和存储

之前我们写过一个OkHttp的封装类，叫做RemoteService。现在我们只需要设置获取数据的url路径并设置回调就可以了。根据知乎的api我们可以知道，数据全部都是GET请求，并且返回的都是json数据。这里我们采用框架Gson及其json解析器GsonFormat，来解析json。 URL可以从api文档中获取。 之后是如何设置回调。首先我们分析如何设置
