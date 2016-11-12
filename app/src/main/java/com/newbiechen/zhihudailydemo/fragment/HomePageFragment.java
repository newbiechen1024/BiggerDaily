package com.newbiechen.zhihudailydemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.StringUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.activity.StoryContentActivity;
import com.newbiechen.zhihudailydemo.adapter.StoryBriefAdapter;
import com.newbiechen.zhihudailydemo.entity.LastNewsEntity;
import com.newbiechen.zhihudailydemo.entity.BeforeNewsEntity;
import com.newbiechen.zhihudailydemo.entity.StoriesEntity;
import com.newbiechen.zhihudailydemo.entity.StoryBriefEntity;
import com.newbiechen.zhihudailydemo.entity.TopStoriesEntity;
import com.newbiechen.zhihudailydemo.model.BriefImageEntity;
import com.newbiechen.zhihudailydemo.utils.DateUtils;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.yyydjk.library.BannerLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/3.
 */
public class HomePageFragment extends BaseFragment implements PullToRefreshRecyclerView.OnRefreshListener,PullToRefreshRecyclerView.PagingableListener{
    public static final String TAG = "HomePageFragment";

    private PullToRefreshRecyclerView mPtrrvRefresh;
    private BannerLayout mBannerAdv;
    private StoryBriefAdapter mAdapter;
    private List<TopStoriesEntity> mTopStoriesList;
    private Gson mGson;
    private String mBeforeDate;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage,container,false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    protected void initView() {
        mPtrrvRefresh = getViewById(R.id.ptrrv_refresh);
    }

    @Override
    protected void initWidget() {
        mGson = new Gson();
        //设置广告在RefreshLayout上
        setUpBannerAdv();
        //初始化RefreshLayout
        setUpRecyclerView();
        //首先从数据库中获取数据
        refreshDataFromDb();
        //从网络中加载数据
        refreshDataFromUrl();

        //设置Toolbar的标题
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("首页");
    }

    private void refreshDataFromDb(){
        //添加广告图片
        List<TopStoriesEntity> topStories = DataSupport.findAll(TopStoriesEntity.class);
        addData2Banner(topStories);

        //获取全部的entity
        List<StoryBriefEntity> storyBriefEntities = DataSupport.findAll(StoryBriefEntity.class,true);
        //获取图片的地址 - -
        for (StoryBriefEntity entity : storyBriefEntities){
           if (entity.getStoriesEntity() != null){
               int content_id = entity.getStoriesEntity().getId();
               List<BriefImageEntity> briefImages = DataSupport.findAll(BriefImageEntity.class);
               List<String> imgUrls = new ArrayList<>();
               for(BriefImageEntity imageEntity : briefImages){
                   if (content_id == imageEntity.getStoryBean_id()){
                       imgUrls.add(imageEntity.getImgUrl());
                   }
               }
               entity.getStoriesEntity().setImages(imgUrls);
           }
           else{
               //设置最远的日期
               mBeforeDate = entity.getDate();
           }
        }
        mAdapter.addItems(storyBriefEntities);
    }

    @Override
    protected void initClick() {

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                StoryBriefEntity entity = mAdapter.getItem(pos);

                if (entity.getType() == StoryBriefEntity.TYPE_STORY_BRIEF){
                    StoriesEntity storiesEntity = entity.getStoriesEntity();
                    startActivity2StoryContent(storiesEntity.getId(),
                            storiesEntity.getTitle());
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
            public void onRemoteFailure() {
                super.onRemoteFailure();
                //完成刷新
                mPtrrvRefresh.setOnRefreshComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
            }

            @Override
            public void onResponse(String data) {
                if (!StringUtils.isGoodJson(data)){
                    return;
                }
                //解析数据
                LastNewsEntity lastNewsEntity = mGson.fromJson(data,LastNewsEntity.class);
                //设置下一个文章的日期
                mBeforeDate = lastNewsEntity.getDate();
                //添加数据到广告中
                addData2Banner(lastNewsEntity.getTop_stories());
                //提取LastNews中的Story
                BeforeNewsEntity beforeNewsEntity = new BeforeNewsEntity();
                beforeNewsEntity.setDate(getResources().getString(R.string.hot_story));
                beforeNewsEntity.setStories(lastNewsEntity.getStories());
                //添加数据到Adapter中
                mAdapter.refreshItems(getStoryBriefEntities(beforeNewsEntity));
                //完成刷新
                mPtrrvRefresh.setOnRefreshComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
                //刷新数据库
                deleteDataFromDb();
            }
        };

        mRemoteService.loadData(URLManager.HOMEPAGE_LAST_NEWS,callback);
    }

    private void loadData(){
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                if (!StringUtils.isGoodJson(data)){
                    return;
                }
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

    private void addData2Banner(final List<TopStoriesEntity> topStories){
        if (topStories != null && topStories.size() > 0){
            //后面存储使用的
            mTopStoriesList = topStories;

            List<String> imgUrls = new ArrayList<>();
            for (TopStoriesEntity entity : topStories){
                //获取网址
                imgUrls.add(entity.getImage());
            }
            //显示图片
            mBannerAdv.setViewUrls(imgUrls);
            //添加广告监听器
            mBannerAdv.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int i) {
                    //页面的跳转
                    TopStoriesEntity entity = mTopStoriesList.get(i);
                    startActivity2StoryContent(entity.getId(),
                            entity.getTitle());
                }
            });
        }
    }

    private List<StoryBriefEntity> getStoryBriefEntities (BeforeNewsEntity entity){
        List<StoryBriefEntity> storyBriefEntities = new ArrayList<>();
        //设置日期
        StoryBriefEntity dateEntity = new StoryBriefEntity();
        dateEntity.setDate(entity.getDate());
        dateEntity.setType(StoryBriefEntity.TYPE_DATE);
        storyBriefEntities.add(dateEntity);
        //设置StoryBriefEntity
        List<StoriesEntity> storiesEntities = entity.getStories();
        for (StoriesEntity bean : storiesEntities){
            StoryBriefEntity contentEntity = new StoryBriefEntity();
            contentEntity.setStoriesEntity(bean);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_homepage,menu);

        MenuItem changeMode = menu.findItem(R.id.main_night_theme);
        if (ZhiHuApplication.isNightMode){
            changeMode.setTitle(R.string.light_mode);
        }
        else {
            changeMode.setTitle(R.string.night_mode);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_night_theme:
                if (ZhiHuApplication.isNightMode){
                    //切换为日间模式
                    SharedPreferenceUtils.saveData(getContext(),"NightMode",false);
                    ZhiHuApplication.isNightMode = false;
                    item.setTitle(R.string.light_mode);
                    getActivity().setTheme(R.style.LightMode);
                }
                else {
                    //切换为夜间模式
                    SharedPreferenceUtils.saveData(getContext(),"NightMode",true);
                    ZhiHuApplication.isNightMode = true;
                    item.setTitle(R.string.night_mode);
                    getActivity().setTheme(R.style.NightMode);
                }
                getActivity().recreate();
                break;
        }
        return true;
    }

    private void startActivity2StoryContent(int id,String title){
        //页面的跳转
        Intent intent = new Intent(getContext(), StoryContentActivity.class);
        intent.putExtra(StoryContentActivity.EXTRA_URL,id);
        intent.putExtra(StoryContentActivity.EXTRA_TITLE,title);
        startActivity(intent);
    }

    private void saveData2Db(){
        //存储广告
        for (TopStoriesEntity topStory : mTopStoriesList){
            topStory.save();
        }
        //存储数据
        List<StoryBriefEntity> storyBriefEntities = mAdapter.getItems();
        for (StoryBriefEntity entity : storyBriefEntities){
            //保存数据
            if(entity.getStoriesEntity() != null){
                entity.getStoriesEntity().save();
                List<String> imageUrls = entity.getStoriesEntity().getImages();
                for (String imageUrl : imageUrls){
                    BriefImageEntity imageEntity = new BriefImageEntity();
                    imageEntity.setImgUrl(imageUrl);
                    imageEntity.setStoryBean_id(entity.getStoriesEntity().getId());
                    imageEntity.save();
                }
            }
            entity.save();
        }
    }

    //清空数据库
    private void deleteDataFromDb(){
        DataSupport.deleteAll(StoriesEntity.class);
        DataSupport.deleteAll(StoryBriefEntity.class);
        DataSupport.deleteAll(BriefImageEntity.class);
        DataSupport.deleteAll(TopStoriesEntity.class);
    }
}
