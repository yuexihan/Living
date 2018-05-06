package com.tencent.living.dataHelper;

import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.Living;
import com.tencent.living.models.Comment;
import com.tencent.living.models.Message;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;

import java.util.ArrayList;


public class CommentHelper {
    public static ResultData<Post> postComment(int emotion_id, int rspto, String content){
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_POST_COMMENT);
        lsa.putParam("token", Living.token);
        lsa.putData("emotion_id",emotion_id );
        lsa.putData("comment", content);
        lsa.putData("rspto", rspto);
        return lsa.execAndGetResult(new TypeToken<ResultData<Post>>() {
        }.getType());
    }

    public static ResultData<ArrayList<Comment>> getCommentsByEmotionId(int emotion_id, int pageno){
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_GET_COMMENT);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("token", Living.token);
        lsa.putParam("emotion_id",emotion_id + "");
        lsa.putParam("pageno", pageno +"");
        return lsa.execAndGetResult(new TypeToken<ResultData<ArrayList<Comment>>>() {
        }.getType());
    }

    public static ResultData<Post> postLike(int emotion_id){
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_POST_LIKE);
        lsa.putParam("token", Living.token);
        lsa.putData("emotion_id",emotion_id);
        return lsa.execAndGetResult(new TypeToken<ResultData<Post>>() {
        }.getType());
    }

    public static ResultData<ArrayList<Message>> getMessagesByUserId(int pageno){
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_GET_MESSAGE);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("token", Living.token);
        lsa.putParam("pageno",pageno + "");
        return lsa.execAndGetResult(new TypeToken<ResultData<ArrayList<Message>>>() {
        }.getType());
    }
}

