package com.newbiechen.zhihudailydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newbiechen.androidlib.base.BaseFragment;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.entity.UserInfo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by PC on 2016/11/11.
 */

public class PersonInfoFragment extends BaseFragment {
    public static final String TAG = "PersonInfoFragment";
    private static final String EXTRA_USER_INFO = "user_info";

    private ImageView mIvIcon;
    private EditText mEtName;
    private TextView mTvBoundWeiBo;
    private TextView mTvBoundQQ;
    private Button mBtnExit;

    private UMShareAPI mUmShareAPI;
    private UserInfo mUserInfo;

    private boolean isEdit = false;
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_info,container,false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    protected void initView() {
        mIvIcon = getViewById(R.id.person_info_iv_icon);
        mEtName = getViewById(R.id.person_info_et_name);
        mTvBoundWeiBo = getViewById(R.id.person_info_tv_weibo);
        mTvBoundQQ = getViewById(R.id.person_info_tv_qq);
        mBtnExit = getViewById(R.id.person_info_btn_exit);
    }

    @Override
    protected void initWidget() {
        mUmShareAPI = UMShareAPI.get(getContext());
        mUserInfo = ZhiHuApplication.sUserInfo;

        mEtName.setText(mUserInfo.userName);
        setIsEdit(false);
        //初始化图片
        ImageLoader.getInstance(getContext())
                .bindImageFromUrl(mUserInfo.userImageUrl,mIvIcon);
        //设置tv的颜色
        if (mUserInfo.isOauthWeiBo){
            mTvBoundWeiBo.setText(R.string.unbound_sina_weibo);
            mTvBoundWeiBo.setTextColor(getResources().getColor(R.color.person_info_bound));

        }
        else {
            mTvBoundWeiBo.setText(R.string.bound_sina_weibo);
            mTvBoundWeiBo.setTextColor(getResources().getColor(R.color.red));
        }
        if (mUserInfo.isOauthQQ){
            mTvBoundQQ.setText(R.string.unbound_qq);
            mTvBoundQQ.setTextColor(getResources().getColor(R.color.person_info_bound));
        }
        else {
            mTvBoundQQ.setText(R.string.bound_qq);
            mTvBoundQQ.setTextColor(getResources().getColor(R.color.red));
        }
        //设置Toolbar的标题
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.person_page);
    }

    private void setIsEdit(boolean edit){
        if (edit == false){
            //设置为不可操作
            mEtName.setFocusable(false);
            mEtName.setFocusableInTouchMode(false);
            mEtName.setTextColor(getResources().getColor(R.color.person_info_name));
        }
        else {
            mEtName.setFocusable(true);
            mEtName.setFocusableInTouchMode(true);
            mEtName.requestFocus();
            mEtName.setTextColor(getResources().getColor(R.color.black));
        }
    }
    @Override
    protected void initClick() {
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解除所有绑定，并退出(清除回退栈)
                mUmShareAPI.deleteOauth(getActivity(),SHARE_MEDIA.SINA,mUmAuthListener);
                mUmShareAPI.deleteOauth(getActivity(),SHARE_MEDIA.QQ,mUmAuthListener);
            }
        });
        mTvBoundWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.isOauthWeiBo){
                    mUmShareAPI.deleteOauth(getActivity(),SHARE_MEDIA.SINA,mUmAuthListener);
                    mUserInfo.isOauthWeiBo = false;
                }
                else {
                    mUmShareAPI.doOauthVerify(getActivity(),SHARE_MEDIA.SINA,mUmAuthListener);
                }
                //最后判断是否退出
                checkIsExit();
            }
        });
    }

    private void checkIsExit(){
        if (mUserInfo.isOauthQQ == false && mUserInfo.isOauthWeiBo == false){
            mUserInfo.userName = "请登录";
            getActivity().onBackPressed();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_person_info,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.person_info_ed_user_info:
                if(isEdit == false){
                    setIsEdit(false);
                    //切换图片
                    item.setIcon(R.mipmap.profile_edit);
                    isEdit = true;
                }
                else {
                    setIsEdit(true);
                    //切换图片
                    item.setIcon(R.mipmap.profile_edit_done);
                    isEdit = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Fragment newInstance(){
        Fragment fragment = new PersonInfoFragment();
        return fragment;
    }

    private UMAuthListener mUmAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
}
