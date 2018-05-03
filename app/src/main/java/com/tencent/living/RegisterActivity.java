package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_btn = findViewById(R.id.confirm_button);
        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                //ToDo 用户数据库写入新成员
            }
        });

        //ToDo 随机昵称的生成
    }
}
