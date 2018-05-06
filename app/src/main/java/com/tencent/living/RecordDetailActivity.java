package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.living.dataHelper.RecordHelper;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import java.util.ArrayList;


public class RecordDetailActivity extends Activity {
    private RecordDetailPlan recordDetailPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordDetailPlan = new RecordDetailPlan(this, RecordDetailPlan.COMMENT_LINES_NO_LIMIT);
        setContentView(recordDetailPlan.getView());
        pullRecord();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data.getBoolean("isOk")) {
                recordDetailPlan.resetCompsContent();
            } else{
                Toast.makeText(RecordDetailActivity.this, R.string.get_record_failed, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void pullRecord(){
        final int emoID = getIntent().getIntExtra("emotion_id",0);
        if (emoID == 0)
            return ;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                ResultData<ArrayList<Record>>
                        res = RecordHelper.getOneRecord(emoID);
                if (res == null || !res.isOk())
                    bundle.putBoolean("isOk", false);
                else{
                    bundle.putBoolean("isOk", true);
                    if(res.getData() != null && res.getData().size() != 0)
                        recordDetailPlan.setRecord(res.getData().get(0));
                }
                msg.setData(bundle);//bundle传值，耗时，效率低
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return ;
        if(requestCode == MainActivity.COMMENT_EDIT_REQUEST_CODE){
            int emoID = data.getIntExtra("emotionID", 0);
            String content = data.getStringExtra("content");
            String to = data.getStringExtra("to");
            int toID = data.getIntExtra("toID", 0);
            recordDetailPlan.addComment(content, to, toID);
        }
    }
}
