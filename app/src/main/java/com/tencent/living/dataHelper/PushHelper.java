package com.tencent.living.dataHelper;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.living.models.Post;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;
import com.tencent.living.tools.Md5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PushHelper {
    private HttpURLConnection connection;

    public static void pushMessage(int to_id) {

        LivingServerAgent lsa = new LivingServerAgent();
//        String timestamp = "1525632987";
        String timestamp = (Calendar.getInstance().getTimeInMillis()/1000) + "";
        lsa.setAction(LivingServerAgent.ACTION_SELF_RECORD);
        lsa.setHttpsMethod(LivingServerAgent.HTTP_METHOD_GET);
        lsa.putParam("access_id","2100283906");
        lsa.putParam("account",to_id + "");
        lsa.putParam("cal_type",0 + "");
        lsa.putParam("message","{\"newMessage\":\"1\"}");
        lsa.putParam("message_type",2 + "");
        lsa.putParam("timestamp",timestamp);
        String signStr = lsa.GET_HOST_PREFIX
                + "access_id=2100283906account="
                + to_id
                + "cal_type=0message="
                + "{\"newMessage\":\"1\"}"
                + "message_type=2timestamp=" + timestamp +
                "4de21fee879e212c401205c712487d2e";

        lsa.putParam("sign",Md5.md5(signStr));
        Log.d("mySign",Md5.md5(signStr));
        lsa.execPush(new TypeToken<ResultData<Post>>() {
        }.getType());
    }


    private static String getResponseBody(InputStream is) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void bindPushAccount(Context context, int user_id){
            XGPushManager.bindAccount(context, user_id+"");
        }
}
