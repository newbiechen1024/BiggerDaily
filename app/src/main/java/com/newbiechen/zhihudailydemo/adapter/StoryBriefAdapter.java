package com.newbiechen.zhihudailydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.entity.StoriesBean;
import com.newbiechen.zhihudailydemo.entity.StoryList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class StoryBriefAdapter extends RecyclerView.Adapter<StoryBriefAdapter.StoryBriefViewHolder> {
    private final List<StoryList> mStoryList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;
    public StoryBriefAdapter (Context context){
        mContext = context;
    }

    @Override
    public StoryBriefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_story_list,parent,false);
        return new StoryBriefViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryBriefViewHolder holder, int position) {
        holder.tvDate.setText(mStoryList.get(position).getDate());
        List<StoriesBean> storiesBean = mStoryList.get(position).getStories();
        addStoryBrief2List((ViewGroup) holder.itemView,storiesBean);
    }

    private void addStoryBrief2List(ViewGroup viewGroup,List<StoriesBean> storiesBean){
        for (StoriesBean bean : storiesBean){
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_story_brief,viewGroup,false);
            TextView title = (TextView) view.findViewById(R.id.brief_tv_title);
            title.setText(bean.getTitle());
            viewGroup.addView(view);
            //添加监听器
            if (mListener != null){
                mListener.onItemClick(bean.getId());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    class StoryBriefViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        public StoryBriefViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.list_tv_date);
        }
    }

    /**
     * 点击回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(int id);
    }

    /********************public**********************************/
    public void addStoryList(StoryList storyList){
        mStoryList.add(storyList);
        notifyDataSetChanged();
    }

    public void refreshStoryList(StoryList storyList){
        mStoryList.clear();
        addStoryList(storyList);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
