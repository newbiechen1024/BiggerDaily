package com.newbiechen.zhihudailydemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.entity.ThemeEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeMenuAdapter extends BaseAdapter{

    //设置MENU,注意该List无法添加数据
    private final List<ThemeEntity> mThemesList = new ArrayList<>();

    private Context mContext;

    public ThemeMenuAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThemesList.size();
    }

    @Override
    public ThemeEntity getItem(int position) {
        //解决添加了Header,position返回 -1的问题
        if (position >= 0){
            return mThemesList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_theme_menu,parent,false);
            holder.tvName = (TextView) convertView.findViewById(R.id.menu_tv_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(getItem(position).getName());
        return convertView;
    }

    class ViewHolder{
        private TextView tvName;
    }

    /***********************************公共的方法*******************************************/
    public void addItem(ThemeEntity entity){
        mThemesList.add(entity);
        notifyDataSetChanged();
    }

    public void addItems(List<ThemeEntity> entities){
        mThemesList.addAll(entities);
        notifyDataSetChanged();
    }

    public void refreshItems(List<ThemeEntity> entities){
        mThemesList.clear();
        //添加首页的按钮
        ThemeEntity entity = new ThemeEntity();
        entity.setName("首页");
        entity.setId(0);
        mThemesList.add(entity);
        //添加首页以下的按钮
        addItems(entities);
    }
}
