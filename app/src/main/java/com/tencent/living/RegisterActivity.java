package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.living.dataHelper.UserHelper;

public class RegisterActivity extends Activity {

    private TextView userphone,password;
    private String user,pwd;
    //RETURN CODE
    public static final int SUCCESSFUL_CODE = 0;
    public static final int FAILED_CODE = 1;
    public static final int COMMENT_EDIT_REQUEST_CODE = 3;
    private UserHelper userHelper = new UserHelper();
    private int return_code;
    private String msg;
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
