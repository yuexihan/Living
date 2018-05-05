package com.tencent.living.dataHelper;

import com.google.gson.reflect.TypeToken;
import com.tencent.living.Living;
import com.tencent.living.models.Post;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;
import java.util.ArrayList;

public class RecordHelper {
    public static ResultData<Post> postRecord(String content, int label_id, int strong, int visiable) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_POST_RECORD);
        lsa.putParam("token", Living.token);
        lsa.putData("content",content + "");
        lsa.putData("lable_id", label_id);
        lsa.putData("visiable", visiable );
        lsa.putData("strong", strong );
        return lsa.execAndGetResult(new TypeToken<ResultData<Post>>() {
        }.getType());
    }

    public static ResultData<ArrayList<Record>> getRecordsByUserId(int pageno) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_SELF_RECORD);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("token", Living.token);
        lsa.putParam("pageno",pageno + "");
        return lsa.execAndGetResult(new TypeToken<ResultData<ArrayList<Record>>>() {
        }.getType());
    }

    public static ResultData<ArrayList<Record>> getRecordsInGround(int label_id, int pageno) {
        LivingServerAgent lsa = new LivingServerAgent();
        lsa.setAction(LivingServerAgent.ACTION_ALL_RECORD);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("token", Living.token);
        lsa.putParam("pageno",pageno + "");
        if (label_id != -1)
            lsa.putParam("label_id",label_id + "");
        return lsa.execAndGetResult(new TypeToken<ResultData<ArrayList<Record>>>() {
        }.getType());
    }
}
