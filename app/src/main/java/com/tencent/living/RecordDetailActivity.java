package com.tencent.living;

import android.app.Activity;
import android.os.Bundle;

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

    private void pullRecord(){

    }
}
