package com.tencent.living.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.living.R;

public class FloatEditorActivity extends Activity {
    private EditText input;
    private String to;
    private int toID;
    private int emotionID;
    private TextView send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fast_reply_floating_layout_2);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        input = findViewById(R.id.et_content);
        send = findViewById(R.id.tv_submit);

        final Intent intent = getIntent();
        to = intent.getStringExtra("to");
        toID = intent.getIntExtra("toID", 0);
        emotionID = intent.getIntExtra("emotionID", 0);
        if (toID != 0)
            input.setHint(getString(R.string.ground_respond_text) + to +":");

        //点击发送
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().length() == 0)
                    return ;
                Intent intent = getIntent();
                intent.putExtra("to", to);
                intent.putExtra("toID",toID);
                intent.putExtra("content", input.getText().toString());
                intent.putExtra("emotionID",emotionID);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
