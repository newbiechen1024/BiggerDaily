package com.newbiechen.zhihudailydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.zhihudailydemo.R;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeBriefFragment extends BaseFragment{
    private static final String BUNDLE_NAME = "name";
    private PullToRefreshRecyclerView mPtrrRefresh;
    private ImageView mIvThemeImg;
    private TextView mTvThemeBrief;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_brief,container,false);
        return view;
    }

    @Override
    protected void initView() {
        mPtrrRefresh = getViewById(R.id.ptrrv_refresh);
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
        mPtrrRefresh.addHeaderView(header);
        mIvThemeImg = (ImageView) header.findViewById(R.id.header_iv_img);
        mTvThemeBrief = (TextView) header.findViewById(R.id.header_tv_theme_brief);
    }

    private void setUpAdapter(){

    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    /******************************************************/

    public static ThemeBriefFragment newInstance(String name){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME,name);

        ThemeBriefFragment fragment = new ThemeBriefFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
