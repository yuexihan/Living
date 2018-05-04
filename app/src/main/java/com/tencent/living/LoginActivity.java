package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;
import com.tencent.living.tools.FontManager;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button login_btn;
    private Button register_btn;
    private EditText et_phone;
    private EditText et_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icon_mobile), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.icon_lock), iconFont);

        login_btn = findViewById(R.id.login_button);
        register_btn = findViewById(R.id.register_button);
        et_phone = findViewById(R.id.phone_input);
        et_pwd = findViewById(R.id.pwd_input);

        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isOk = data.getBoolean("isOk");
            login_btn.setEnabled(true);
            if (isOk){
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else
                Toast.makeText(LoginActivity.this, R.string.login_failed, 2000).show();
        }
    };


    /**
     * 真正的登录操作，成功返回true 失败返回false
     */
    private boolean doLogin(){
        //检测登录
        String user = et_phone.getText().toString();
        String pwd = et_pwd.getText().toString();
        ResultData<Post> res = UserHelper.postLogin(user, pwd);
        if (res != null && res.isOk()) {
            //初始化全局User;
              ResultData<User> userRes = UserHelper.getUserInfo(res.getData().getToken());
            if (userRes.isOk()) {
                Living.user = userRes.getData();
                return true;
            }
        }
        return false;
    }

    /**
     * 当登录按钮被点击时候调用
     * @param view
     */
    public void onClick(View view) {
        login_btn.setEnabled(false);
        new Thread(){
            public void run(){
                Message msg = Message.obtain();
                Bundle bundle= new Bundle();
                if (doLogin())
                    bundle.putBoolean("isOk", true);
                else
                    bundle.putBoolean("isOk", false);
                msg.setData(bundle);//bundle传值，耗时，效率低
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }
}
