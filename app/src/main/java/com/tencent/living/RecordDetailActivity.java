package com.tencent.living;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.living.models.Record;


public class RecordDetailActivity extends Activity {
    private RecordDetailPlan recordDetailPlan;
    private Record record;
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
                recordDetailPlan.setRecord(record);
                recordDetailPlan.resetCompsContent();
            } else{
                Toast.makeText(RecordDetailActivity.this, R.string.get_record_failed, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void pullRecord(){

    }
}
