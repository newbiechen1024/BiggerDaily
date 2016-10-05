package com.newbiechen.zhihudailydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.adapter.StoryBriefAdapter;
import com.newbiechen.zhihudailydemo.entity.LastNewsEntity;
import com.newbiechen.zhihudailydemo.entity.LastNewsEntity.*;
import com.newbiechen.zhihudailydemo.entity.StoryList;
import com.newbiechen.zhihudailydemo.utils.DateUtils;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.yyydjk.library.BannerLayout;

import java.sql.Date;
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
    private Gson mGson;

    private int mCurrentDate;

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
        firstLoadData();
    }

    @Override
    protected void initClick() {
        //添加广告监听器
        mBannerAdv.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int i) {

            }
        });
        mAdapter.setOnItemClickListener(new StoryBriefAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                //传递并跳转
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

    private void firstLoadData(){
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                LastNewsEntity entity = mGson.fromJson(data,LastNewsEntity.class);
                List<TopStoriesBean> topStoriesBean = entity.getTop_stories();
                entity.setDate("今日热闻");
                mCurrentDate = new Integer(DateUtils.getCurrentDate());
                addData2Banner(topStoriesBean);
                //添加数据到其中
                StoryList storyList = new StoryList();
                storyList.setDate(entity.getDate());
                storyList.setStories(entity.getStories());

                mAdapter.refreshStoryList(storyList);

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

                StoryList storyList = mGson.fromJson(data,StoryList.class);
                mCurrentDate -= 1;

                String dateStr = storyList.getDate();
                storyList.setDate(DateUtils.parseDateStr(dateStr));
                addData2Adapter(storyList);

                mPtrrvRefresh.setOnLoadMoreComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
            }
        };
        String date = mCurrentDate+"";
        String urlPath = URLManager.HOMEPAGE_BEFORE_NEWS+date;
        mRemoteService.loadData(urlPath,callback);
    }

    private void addData2Banner(List<TopStoriesBean> topStoriesBeans){
        List<String> imgUrls = new ArrayList<>();
        for (TopStoriesBean bean : topStoriesBeans){
            imgUrls.add(bean.getImage());
        }
        mBannerAdv.setViewUrls(imgUrls);
    }

    private void addData2Adapter(StoryList storyList){
        mAdapter.addStoryList(storyList);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG,"调用了吗");
        firstLoadData();
    }

    @Override
    public void onLoadMoreItems() {
        loadData();
    }
}
