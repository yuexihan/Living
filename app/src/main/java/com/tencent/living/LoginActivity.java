package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.living.tools.FontManager;

public class LoginActivity extends Activity {

    private TextView username,password;
    private String user,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icon_mobile), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.icon_lock), iconFont);

        Button login_btn = (Button) findViewById(R.id.login_button);
        Button register_btn = (Button) findViewById(R.id.register_button);
        EditText et_phone = findViewById(R.id.phone_input);
        EditText et_pwd = findViewById(R.id.pwd_input);

        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                username = (TextView) findViewById(R.id.phone_input);
                password = (TextView) findViewById(R.id.pwd_input);
                user = username.getText().toString();
                pwd = password.getText().toString();
//                onlineDB db = new onlineDB();
//                if ( !db.connect() ){
//                    Toast.makeText(LoginActivity.this, "无法连接服务器，请稍后重试", Toast.LENGTH_SHORT).show();
//                }else {
//                    if (db.userCheck(user, pwd)) {
//                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("mode","online");
//                        bundle.putString("username",user);
//                        Intent intent = new Intent();
//                        intent.setClass(LoginActivity.this,MainActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        LoginActivity.this.finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "登陆失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
//                    }
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
