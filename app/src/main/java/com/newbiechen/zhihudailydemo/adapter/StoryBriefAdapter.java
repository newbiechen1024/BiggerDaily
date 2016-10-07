package com.newbiechen.zhihudailydemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newbiechen.androidlib.base.BaseAdapter;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.entity.StoriesBean;
import com.newbiechen.zhihudailydemo.entity.StoryBriefEntity;

import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class StoryBriefAdapter extends BaseAdapter<StoryBriefEntity,StoryBriefAdapter.StoryBriefViewHolder>{
    private static final String TAG = "StoryBriefAdapter";
    public static final String PREFERENCE_READ = "read";

    private Context mContext;

    public StoryBriefAdapter(Context context){
        mContext = context;
    }

    @Override
    public StoryBriefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_story_brief,parent,false);
        return new StoryBriefViewHolder(view);
    }

    @Override
    public void setUpViewHolder(StoryBriefViewHolder holder, int position) {
        StoryBriefEntity entity = mItemList.get(position);
        if (entity.getType() == StoryBriefEntity.TYPE_DATE){
            //隐藏StoryBrief显示卡片
            holder.rlStory.setVisibility(View.GONE);
            holder.tvDate.setVisibility(View.VISIBLE);
            //添加数据
            holder.tvDate.setText(entity.getDate());
        }
        else if (entity.getType() == StoryBriefEntity.TYPE_STORY_BRIEF){
            //隐藏日期
            holder.tvDate.setVisibility(View.GONE);
            holder.rlStory.setVisibility(View.VISIBLE);
            //添加数据
            StoriesBean bean = entity.getStoriesBean();
            holder.tvTitle.setText(bean.getTitle());

            //添加图片
            int imageWidth = (int) mContext.getResources().
                    getDimension(R.dimen.story_brief_icon_width);
            int imageHeight = (int) mContext.getResources().
                    getDimension(R.dimen.story_brief_icon_height);
            //异步加载数据
            ImageLoader.getInstance(mContext).bindImageFromUrl(
                    bean.getImages().get(0),holder.ivIcon,
                    imageWidth,imageHeight
            );
        }
    }

    public class StoryBriefViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        TextView tvDate;
        RelativeLayout rlStory;
        TextView tvTitle;
        ImageView ivIcon;
        ImageView ivMoreImg;
        public StoryBriefViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvDate = getViewById(R.id.brief_tv_date);
            rlStory = getViewById(R.id.brief_rl_story);
            tvTitle = getViewById(R.id.brief_tv_title);
            ivIcon = getViewById(R.id.brief_iv_icon);
            ivMoreImg = getViewById(R.id.brief_iv_more_img);
        }

        public <T extends View> T getViewById(int id){
            return (T) itemView.findViewById(id);
        }
    }
}
