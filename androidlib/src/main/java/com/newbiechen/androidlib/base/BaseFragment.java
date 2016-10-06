package com.newbiechen.androidlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.androidlib.net.RemoteService;

/**
 * Created by PC on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {
    protected View view ;

    protected RemoteService mRemoteService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = onCreateContentView(inflater,container,savedInstanceState);
        initView();
        mRemoteService = RemoteService.getInstance(getContext());
        initWidget();
        initClick();
        processLogic(savedInstanceState);
        return view;
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    protected abstract void initView();
    protected abstract void initWidget();
    protected abstract void initClick();
    protected abstract void processLogic(Bundle savedInstanceState);

/****************************************公共方法*****************************************************/

    public <VT extends View> VT getViewById(int id){
        return (VT) view.findViewById(id);
    }

    public void startActivity(Class<? extends BaseActivity> activity){
        Intent intent = new Intent(getContext(),activity);
        startActivity(intent);
    }
}
