package com.newbiechen.zhihudailydemo.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import static android.view.View.GONE;

/**
 * Created by PC on 2016/10/18.
 */

public class ThemeContentBehavior extends CoordinatorLayout.Behavior<View> {
    private float mImgHeight;

    public ThemeContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        mImgHeight = dependency.getHeight();
        float offset = dependency.getY();
        float fraction = Math.abs(offset) / mImgHeight;
        float alpha = 1 - fraction;
        child.setAlpha(alpha);
        if (alpha == 0){
            child.setVisibility(GONE);
        }
        else if (alpha != 0 && child.getVisibility() == View.GONE){
            child.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
