package com.newbiechen.zhihudailydemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by PC on 2016/9/9.
 * 1、通过判断是否达到倒数第四个item，然后进行加载
 *
 */
public class AutoLoadingRecyclerView extends RecyclerView {
    private static final int COUNT = 4;

    //掌管Item显示和加载。
    private LinearLayoutManager mManager;
    private OnLoadMoreListener mLoadMoreListener;
    //是否允许下拉加载
    private boolean isLoading = true;
    private boolean isFinish = true;

    public AutoLoadingRecyclerView(Context context) {
        this(context,null);
    }

    public AutoLoadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AutoLoadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 绑定滑动监听
     */
    private void init(){
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                checkLoadMore(dx,dy);
            }
        });
    }

    private void checkLoadMore(int dx, int dy){
        if(mManager == null){
            //固定为LinearManager。
            mManager = (LinearLayoutManager) getLayoutManager();
        }
        int lastVisibleItemPos = mManager.findLastVisibleItemPosition();
        int itemCount = mManager.getItemCount();
        if (itemCount >= 4 &&
                lastVisibleItemPos == (itemCount-COUNT)&&
                dy > 0 && isLoading && isFinish){
            //启动加载更多的回调
            mLoadMoreListener.onLoadMore();
            isFinish = false;
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    /***********************公共的方法*************************************/
    /**
     * 设置加载更多的回调监听
     * @param listener
     */
    public void setLoaderMoreListener(OnLoadMoreListener listener){
        mLoadMoreListener = listener;
    }

    /**
     * 是否开启加载更多
     * @param loading
     */
    public void setLoading(boolean loading){
        isLoading = loading;
    }
    /**
     * 设置是否加载完成。
     */
    public void setLoadMoreFinish(boolean finish){
        isFinish = finish;
    }
}
