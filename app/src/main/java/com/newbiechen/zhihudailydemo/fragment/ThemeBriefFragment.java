package com.newbiechen.zhihudailydemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.activity.ThemeContentActivity;
import com.newbiechen.zhihudailydemo.adapter.ThemeBriefAdapter;
import com.newbiechen.zhihudailydemo.entity.StoriesEntity;
import com.newbiechen.zhihudailydemo.entity.ThemeBriefEntity;
import com.newbiechen.zhihudailydemo.utils.URLManager;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeBriefFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,PullToRefreshRecyclerView.PagingableListener{
    public static final String TAG = "ThemeBriefFragment";
    private static final String BUNDLE_ID = "id";
    private PullToRefreshRecyclerView mPtrrvRefresh;
    private ThemeBriefAdapter mAdapter;
    private ImageView mIvThemeImg;
    private TextView mTvThemeBrief;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_brief,container,false);
        return view;
    }

    @Override
    protected void initView() {
        mPtrrvRefresh = getViewById(R.id.ptrrv_refresh);
        setUpRefreshLayout();
    }

    @Override
    protected void initWidget() {

    }

    private void setUpRefreshLayout(){
        setUpHeader();
        setUpAdapter();
    }

    private void setUpHeader(){
        //设置头部
        View header = LayoutInflater.from(getContext())
                .inflate(R.layout.theme_brief_header,null,false);
        mPtrrvRefresh.addHeaderView(header);
        mIvThemeImg = (ImageView) header.findViewById(R.id.header_iv_img);
        mTvThemeBrief = (TextView) header.findViewById(R.id.header_tv_theme_brief);
    }

    private void setUpAdapter(){
        mPtrrvRefresh.setLayoutManager(new LinearLayoutManager(getContext()));

        mPtrrvRefresh.setSwipeEnable(true);
        mPtrrvRefresh.setLoadmoreString("loading");

        mPtrrvRefresh.setOnRefreshListener(this);
        mPtrrvRefresh.setPagingableListener(this);

        mPtrrvRefresh.onFinishLoading(true, false);

        mAdapter = new ThemeBriefAdapter(getContext());
        mPtrrvRefresh.setAdapter(mAdapter);
    }

    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                StoriesEntity entity = mAdapter.getItem(pos);
                Intent intent = new Intent(getContext(), ThemeContentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshData();
    }

    private void refreshData(){
        String url = URLManager.THEME_BRIEF+getArguments().getInt(BUNDLE_ID);
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                Gson gson = new Gson();
                ThemeBriefEntity entities = gson.fromJson(data,ThemeBriefEntity.class);
                //设置头部简介
                mTvThemeBrief.setText(entities.getDescription());
                //获取图片大小
                int imgWidth = mIvThemeImg.getMeasuredWidth();
                int imgHeight = mIvThemeImg.getMeasuredHeight();
                ImageLoader.getInstance(getContext())
                        .bindImageFromUrl(entities.getBackground(),mIvThemeImg,imgWidth,imgHeight);
                //之后设置Adapter
                mAdapter.refreshItems(entities.getStories());
                //关闭刷新
                mPtrrvRefresh.setOnRefreshComplete();
                mPtrrvRefresh.onFinishLoading(true,false);
            }
        };
        mRemoteService.loadData(url,callback);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMoreItems() {

    }
    /******************************************************/

    public static ThemeBriefFragment newInstance(int id){
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID,id);

        ThemeBriefFragment fragment = new ThemeBriefFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
