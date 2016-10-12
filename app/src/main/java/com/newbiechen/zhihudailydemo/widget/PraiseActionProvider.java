package com.newbiechen.zhihudailydemo.widget;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newbiechen.zhihudailydemo.R;

/**
 * Created by PC on 2016/10/12.
 */

public class PraiseActionProvider extends android.support.v4.view.ActionProvider {
    private Context mContext;

    private ImageView mIvIcon;
    private TextView mTvPraiseCount;

    private boolean isSelect;
    public PraiseActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(
                android.support.design.R.dimen.abc_action_bar_default_height_material);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.provider_praise,null,false);
        view.setLayoutParams(layoutParams);
        initView(view);
        return view;
    }

    private void initView(View view){
        mIvIcon = (ImageView) view.findViewById(R.id.praise_iv_icon);
        mTvPraiseCount = (TextView) view.findViewById(R.id.praise_tv_praise_count);
    }

    /*************************Public Method********************************/

    public void setPraiseCount(int count){
        mTvPraiseCount.setText(count+"");
    }

    /**
     * 设置选中状态
     * @param select
     */
    public void setIsSelect(boolean select){
        isSelect = select;
        //并且如果为true则将图片设置为选中装填
        if (isSelect == true){
            mIvIcon.setImageResource(R.mipmap.praised);
        }
    }

    /**
     * 改变点击的状态
     */
    public void changePraise(){
        //如果未选中则表示为选中
        if (isSelect == false){
            //通过提交网络
            mIvIcon.setImageResource(R.mipmap.praised);
            isSelect = true;
        }
        else {
            mIvIcon.setImageResource(R.mipmap.praise);
            isSelect = false;
        }
    }
}

