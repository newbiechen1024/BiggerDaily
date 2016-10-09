package com.newbiechen.zhihudailydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.zhihudailydemo.entity.ThemeBriefEntity;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeBriefAdapter extends BaseAdapter<ThemeBriefEntity,ThemeBriefAdapter.ThemeBriefViewHolder> {


    @Override
    public void setUpViewHolder(ThemeBriefViewHolder holder, int position) {

    }

    @Override
    public ThemeBriefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    class ThemeBriefViewHolder extends RecyclerView.ViewHolder{

        public ThemeBriefViewHolder(View itemView) {
            super(itemView);
        }
    }
}
