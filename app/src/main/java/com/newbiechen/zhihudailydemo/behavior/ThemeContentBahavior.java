package com.newbiechen.zhihudailydemo.behavior;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by PC on 2016/10/12.
 */

public class ThemeContentBahavior extends CoordinatorLayout.Behavior<ImageView> {
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
