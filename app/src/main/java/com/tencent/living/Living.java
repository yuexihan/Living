package com.tencent.living;

import android.app.Application;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.tencent.living.models.User;

public class Living  extends Application{
    //当前正在登录的用户信息，在LoginActivity的doLogin中被初始化
    public static User user;
    public static String token;
    //配置文件名字
    public static String config = ".MyConfig";

    //头像表
    public static final int profileID[] = {
            R.drawable.profile0,
            R.drawable.profile1,
            R.drawable.profile2,
            R.drawable.profile3,
            R.drawable.profile4,
            R.drawable.profile5,
            R.drawable.profile6,
            R.drawable.profile7,
            R.drawable.profile8
    };
    //昵称表
    public static final String[] nickNames = {
            "佩奇",
            "伏地魔",
            "刘备",
            "麦兜",
            "葫芦娃",
            "奥特曼",
            "孙悟空",
            "哈利波特",
            "马老师",
            "PDD",
            "盲僧",
            "亚瑟王",
            "孙尚香",
            "甄姬",
            "女娲",
            "大乔",
            "小乔",
            "小猪",
            "雷神",
            "马里奥",
            "哥斯拉",
            "金刚",
            "猎空",
            "春丽",
            "斯巴达",
            "哈莉",
            "高达",
            "忍者神龟"
    };


    @Override
    public void onCreate() {
        super.onCreate();
        sFaceServiceClient = new FaceServiceRestClient(getString(R.string.emotion_endpoint), getString(R.string.emotion_key));
    }


    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;
}
