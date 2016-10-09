package com.newbiechen.zhihudailydemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newbiechen.zhihudailydemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 2016/10/9.
 */

public class ThemeMenuAdapter extends BaseAdapter{
    private static final String [] THEMES = {
            "首页","日常心理学","用户推荐日报","电影日报",
            "不许无聊","设计日报","大公司日报","财经日报",
            "互联网安全","开始游戏","音乐日报","动漫日报",
            "体育日报"
    };

    //设置MENU,注意该List无法添加数据
    private final List<String> mThemesList = Arrays.asList(THEMES);

    private Context mContext;

    public ThemeMenuAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThemesList.size();
    }

    @Override
    public String getItem(int position) {
        return mThemesList.get(position);
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
                    .inflate(R.layout.list_theme_menu,parent);
            holder.tvName.setText(getItem(position));
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.tvName.setText(getItem(position));
        }
        return convertView;
    }

    class ViewHolder{
        private TextView tvName;
    }
}
