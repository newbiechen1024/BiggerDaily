package com.newbiechen.zhihudailydemo.adapter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.entity.StoriesEntity;
import com.newbiechen.zhihudailydemo.entity.ThemeBriefEntity;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeBriefAdapter extends BaseAdapter<StoriesEntity,ThemeBriefAdapter.ThemeBriefViewHolder> {

    private Context mContext;

    public ThemeBriefAdapter(Context context){
        mContext = context;
    }

    @Override
    public ThemeBriefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_theme_brief,parent,false);
        return new ThemeBriefViewHolder(view);
    }

    @Override
    public void setUpViewHolder(ThemeBriefViewHolder holder, int position) {
        StoriesEntity entity = getItem(position);


        if (ZhiHuApplication.isNightMode){
            holder.linearStory.setBackground(
                    mContext.getResources().getDrawable(R.drawable.night_story_brief_shadow));
        }
        else {
            holder.linearStory.setBackground(
                    mContext.getResources().getDrawable(R.drawable.story_brief_shadow)
            );
        }

        if (entity.getImages() != null){
            holder.ivIcon.setVisibility(View.VISIBLE);
            //异步加载图片
            ImageLoader.getInstance(mContext)
                    .bindImageFromUrl(entity.getImages().get(0),
                            holder.ivIcon);
        }
        else {
            holder.ivIcon.setVisibility(View.GONE);
        }
    }

    class ThemeBriefViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView ivIcon;
        LinearLayout linearStory;

        public ThemeBriefViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.brief_tv_title);
            ivIcon = (ImageView) itemView.findViewById(R.id.brief_iv_icon);
            linearStory = (LinearLayout) itemView.findViewById(R.id.brief_linear_story);
        }
    }
}
