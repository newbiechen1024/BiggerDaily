package com.newbiechen.zhihudailydemo.entity;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.newbiechen.androidlib.utils.IOUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by PC on 2016/11/11.
 */

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 4324181343414L;
    private static final String FILE_NAME = "cache.txt";

    public boolean isOauthWeiBo = false;
    public boolean isOauthQQ = false;
    public String userName;
    public String userImageUrl;

    public UserInfo (String userName,String userImageUrl){
        this.userName = userName;
        this.userImageUrl = userImageUrl;
    }

    public static void saveUserInfo(Context context,UserInfo userInfo){
        File file = context.getDir(FILE_NAME,Context.MODE_PRIVATE);
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(stream);
        }
    }

    public static UserInfo readUserInfo(Context context){
        File file = context.getDir(FILE_NAME,Context.MODE_PRIVATE);
        ObjectInputStream stream = null;
        UserInfo userInfo = new UserInfo("请登录","");
        try {
            stream = new ObjectInputStream(new FileInputStream(file));
            userInfo = (UserInfo) stream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("UserInfo","未初始化过UserInfo");
        }finally {
            IOUtils.closeStream(stream);
        }
        return userInfo;
    }
}
