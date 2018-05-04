package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.tools.FontManager;

public class LoginActivity extends Activity {

    private TextView userphone,password;
    private String user,pwd;
    //RETURN CODE
    public static final int SUCCESSFUL_CODE = 0;
    public static final int FAILED_CODE = -1;
    public static final int COMMENT_EDIT_REQUEST_CODE = 3;
    private UserHelper userHelper = new UserHelper();
    private int return_code;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icon_mobile), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.icon_lock), iconFont);

        Button login_btn = findViewById(R.id.login_button);
        final Button register_btn = findViewById(R.id.register_button);
        EditText et_phone = findViewById(R.id.phone_input);
        EditText et_pwd = findViewById(R.id.pwd_input);

        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                userphone = findViewById(R.id.phone_input);
                password = findViewById(R.id.pwd_input);
                user = userphone.getText().toString();
                pwd = password.getText().toString();
                //ToDo 与用户数据库做比较确认登入
                ResultData<Post> postdata = userHelper.postLogin(user, pwd);
                return_code = postdata.getRet_code();
                msg = postdata.getMessage();

                Log.i("postData", String.valueOf(return_code));
                if (return_code == SUCCESSFUL_CODE){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
                else if (return_code == FAILED_CODE){
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

    }
}
