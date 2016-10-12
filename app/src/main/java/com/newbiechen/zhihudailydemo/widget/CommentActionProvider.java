package com.newbiechen.zhihudailydemo.widget;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newbiechen.zhihudailydemo.R;

/**
 * Created by PC on 2016/10/12.
 */

public class CommentActionProvider extends android.support.v4.view.ActionProvider {

    private Context mContext;
    private TextView mTvCommentCount;

    public CommentActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(
                android.support.design.R.dimen.abc_action_bar_default_height_material);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
        //创建View
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.provider_comment,null,false);
        view.setLayoutParams(layoutParams);
        initView(view);
        return view;
    }

    private void initView(View view){
        mTvCommentCount = (TextView) view.findViewById(R.id.comment_tv_count);
    }

    /**************************公共的方法*************************/
    public void setCommentCount(int commentCount) {
        mTvCommentCount.setText(commentCount+"");
    }
}
