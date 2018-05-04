package com.tencent.living;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;
import com.tencent.living.tools.FontManager;

public class LoginActivity extends Activity {

    private TextView username, password;
    private String user, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icon_mobile), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.icon_lock), iconFont);

        Button login_btn = findViewById(R.id.login_button);
        Button register_btn = findViewById(R.id.register_button);
        EditText et_phone = findViewById(R.id.phone_input);
        EditText et_pwd = findViewById(R.id.pwd_input);

        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = findViewById(R.id.phone_input);
                password = findViewById(R.id.pwd_input);
                user = username.getText().toString();
                pwd = password.getText().toString();
                //检测登录
                ResultData<Post> res = UserHelper.postLogin(user, pwd);
                if (res.isOk()) {
                    //初始化全局User;
                    ResultData<User> userRes = UserHelper.getUserInfo(res.getData().getToken());
                    if (userRes.isOk()){
                        Living.user = userRes.getData();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    Toast.makeText(LoginActivity.this,"123", 2000).show();
                    return ;
                }
                Toast.makeText(LoginActivity.this,R.string.login_failed, 2000).show();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
