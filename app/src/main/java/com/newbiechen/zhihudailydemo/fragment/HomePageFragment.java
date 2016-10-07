package com.newbiechen.zhihudailydemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.activity.StoryContentActivity;
import com.newbiechen.zhihudailydemo.adapter.StoryBriefAdapter;
import com.newbiechen.zhihudailydemo.entity.LastNewsEntity;
import com.newbiechen.zhihudailydemo.entity.LastNewsEntity.*;
import com.newbiechen.zhihudailydemo.entity.BeforeNewsEntity;
import com.newbiechen.zhihudailydemo.entity.StoriesBean;
import com.newbiechen.zhihudailydemo.entity.StoryBriefEntity;
import com.newbiechen.zhihudailydemo.model.BriefImageEntity;
import com.newbiechen.zhihudailydemo.utils.DateUtils;
import com.newbiechen.zhihudailydemo.utils.SharedPresManager;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.yyydjk.library.BannerLayout;

import org.litepal.crud.DataSupport;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 2016/10/3.
 */
public class HomePageFragment extends BaseFragment implements PullToRefreshRecyclerView.OnRefreshListener,PullToRefreshRecyclerView.PagingableListener{
    public static final String TAG = "HomePageFragment";

    private PullToRefreshRecyclerView mPtrrvRefresh;
    private BannerLayout mBannerAdv;
    private StoryBriefAdapter mAdapter;
    private Gson mGson;

    private String mBeforeDate;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage,container,false);
        return view;
    }

    @Override
    protected void initView() {
        mPtrrvRefresh = getViewById(R.id.homepage_ptrrv_refresh);
    }

    @Override
    protected void initWidget() {
        mGson = new Gson();
        setUpBannerAdv();
        setUpRecyclerView();
        //首先从数据库中获取数据
        refreshDataFromDb();
        //从网络中加载数据
        refreshDataFromUrl();
    }

    private void refreshDataFromDb(){
        //添加广告图片
        String bannerUrls = SharedPreferenceUtils.getData(getContext(),
                SharedPresManager.PRES_BANNER_IMG_URL,"");
        String [] bannerUrl = bannerUrls.split(",");
        if (!bannerUrl.equals("")){
            List<String> imgUrls = Arrays.asList(bannerUrl);
            mBannerAdv.setViewUrls(imgUrls);
        }

        //获取全部的entity
        List<StoryBriefEntity> storyBriefEntities = DataSupport.findAll(StoryBriefEntity.class,true);
        //获取图片的地址 - -
        for (StoryBriefEntity entity : storyBriefEntities){
           if (entity.getStoriesBean() != null){
               int content_id = entity.getStoriesBean().getId();
               List<BriefImageEntity> imageEntities = DataSupport.findAll(BriefImageEntity.class);
               List<String> imgUrls = new ArrayList<>();
               for(BriefImageEntity imageEntity : imageEntities){
                   if (content_id == imageEntity.getStoryBean_id()){
                       imgUrls.add(imageEntity.getImgUrl());
                   }
               }
               entity.getStoriesBean().setImages(imgUrls);
           }
        }
        mAdapter.addItems(storyBriefEntities);


    }

    @Override
    protected void initClick() {
        //添加广告监听器
        mBannerAdv.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int i) {

            }
        });

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                StoryBriefEntity entity = mAdapter.getItem(pos);

                if (entity.getType() == StoryBriefEntity.TYPE_STORY_BRIEF){
                    //详情页面的地址
                    String urlPath = URLManager.STORY_CONTENT +
                            entity.getStoriesBean().getId();
                    //跳转。
                    Intent intent = new Intent(getContext(), StoryContentActivity.class);
                    intent.putExtra(StoryContentActivity.EXTRA_URL,urlPath);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void setUpBannerAdv(){
        View view =  LayoutInflater.from(getContext())
                .inflate(R.layout.banner,null,false);
        mBannerAdv = (BannerLayout) view.findViewById(R.id.banner);
        mPtrrvRefresh.addHeaderView(view);
    }

    private void setUpRecyclerView(){
        mPtrrvRefresh.setLayoutManager(new LinearLayoutManager(getContext()));

        mPtrrvRefresh.setSwipeEnable(true);
        mPtrrvRefresh.setLoadmoreString("loading");

        mPtrrvRefresh.setOnRefreshListener(this);
        mPtrrvRefresh.setPagingableListener(this);

        mPtrrvRefresh.onFinishLoading(true, false);

        mAdapter = new StoryBriefAdapter(getContext());
        mPtrrvRefresh.setAdapter(mAdapter);
    }

    private void refreshDataFromUrl(){
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                //解析数据
                LastNewsEntity lastNewsEntity = mGson.fromJson(data,LastNewsEntity.class);
                //设置下一个文章的日期
                mBeforeDate = lastNewsEntity.getDate();
                //添加数据到广告中
                addData2Banner(lastNewsEntity);
                //提取LastNews中的Story
                BeforeNewsEntity beforeNewsEntity = new BeforeNewsEntity();
                beforeNewsEntity.setDate(getResources().getString(R.string.hot_story));
                beforeNewsEntity.setStories(lastNewsEntity.getStories());
                //添加数据到Adapter中
                mAdapter.refreshItems(getStoryBriefEntities(beforeNewsEntity));
                //完成刷新
                mPtrrvRefresh.setOnRefreshComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
            }
        };
        mRemoteService.loadData(URLManager.HOMEPAGE_LAST_NEWS,callback);
    }

    private void loadData(){
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                BeforeNewsEntity beforeNewsEntity = mGson.fromJson(data,BeforeNewsEntity.class);
                //设置接下来的数据
                mBeforeDate = beforeNewsEntity.getDate();
                //转换日期
                String dateStr = beforeNewsEntity.getDate();
                beforeNewsEntity.setDate(DateUtils.parseDateStr(dateStr));
                //添加数据
                mAdapter.addItems(getStoryBriefEntities(beforeNewsEntity));
                //添加到数据库中
                //完成加载
                mPtrrvRefresh.setOnLoadMoreComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
            }
        };
        String urlPath = URLManager.HOMEPAGE_BEFORE_NEWS+mBeforeDate;
        mRemoteService.loadData(urlPath,callback);
    }

    private void addData2Banner(LastNewsEntity lastNewsEntity){
        List<TopStoriesBean> topStoriesBean = lastNewsEntity.getTop_stories();
        List<String> imgUrls = new ArrayList<>();
        StringBuilder bannerUrls = new StringBuilder();
        for (TopStoriesBean bean : topStoriesBean){
            imgUrls.add(bean.getImage());
            bannerUrls.append(bean.getImage()+",");
        }
        SharedPreferenceUtils.saveData(getContext(),
                SharedPresManager.PRES_BANNER_IMG_URL,bannerUrls.toString());
        mBannerAdv.setViewUrls(imgUrls);
    }

    private List<StoryBriefEntity> getStoryBriefEntities (BeforeNewsEntity entity){
        List<StoryBriefEntity> storyBriefEntities = new ArrayList<>();
        //设置日期
        StoryBriefEntity dateEntity = new StoryBriefEntity();
        dateEntity.setDate(entity.getDate());
        dateEntity.setType(StoryBriefEntity.TYPE_DATE);
        storyBriefEntities.add(dateEntity);
        //设置StoryBriefEntity
        List<StoriesBean> storiesBeans = entity.getStories();
        for (StoriesBean bean : storiesBeans){
            StoryBriefEntity contentEntity = new StoryBriefEntity();
            contentEntity.setStoriesBean(bean);
            contentEntity.setType(StoryBriefEntity.TYPE_STORY_BRIEF);
            storyBriefEntities.add(contentEntity);
        }
        return storyBriefEntities;
    }

    @Override
    public void onRefresh() {
        refreshDataFromUrl();
    }

    @Override
    public void onLoadMoreItems() {
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData2Db();

    }

    private void saveData2Db(){
        //判断是否数据不为0，不为0则清空数据库，然后再添加数据
        if (mAdapter.getItemCount() != 0){
            //首先清空数据库
            DataSupport.deleteAll(StoriesBean.class);
            DataSupport.deleteAll(StoryBriefEntity.class);
            DataSupport.deleteAll(BriefImageEntity.class);
            //然后保存
            List<StoryBriefEntity> storyBriefEntities = mAdapter.getItems();
            for (StoryBriefEntity entity : storyBriefEntities){
                //保存数据
                if(entity.getStoriesBean() != null){
                    entity.getStoriesBean().save();
                    List<String> imageUrls = entity.getStoriesBean().getImages();
                    for (String imageUrl : imageUrls){
                        BriefImageEntity imageEntity = new BriefImageEntity();
                        imageEntity.setImgUrl(imageUrl);
                        imageEntity.setStoryBean_id(entity.getStoriesBean().getId());
                        imageEntity.save();
                    }
                }
                entity.save();
            }
        }
    }

}
