package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Living;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;
import com.tencent.living.tools.FontManager;


public class LoginActivity extends Activity implements View.OnClickListener {
    private Button login_btn;
    private Button register_btn;
    private EditText et_phone;
    private EditText et_pwd;
    private ProgressBar pb;

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
        pb = findViewById(R.id.progressBar);

        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
//        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        loadUserFromConfig();
        if (et_phone.getText().toString().length() >= 1)
            onClick(null);
    }

    /**
     * 保存当前登录的用户信息到配置文件用于自动登录
     */
    private void saveUserToConfig(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Living.config, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("phone", et_phone.getText().toString());
        editor.putString("pwd", et_pwd.getText().toString());
        // Commit the edits!
        editor.commit();
    }

    /**
     * 从配置文件中读取用户信息用于自动登录
     */
    private void loadUserFromConfig(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Living.config, 0);
        et_phone.setText(settings.getString("phone",""));
        et_pwd.setText(settings.getString("pwd",""));
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data.getBoolean("isOk")) {
                saveUserToConfig();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else{
                login_btn.setVisibility(View.VISIBLE);
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
            }
        }
    };


    /**
     * 真正的登录操作，成功返回true 失败返回false
     */
    private boolean doLogin() {
        //检测登录
        String user = et_phone.getText().toString();
        String pwd = et_pwd.getText().toString();
        ResultData<Post> res = UserHelper.postLogin(user, pwd);

        if (res != null && res.isOk()) {
            //初始化全局User;
            Living.token = res.getData().getToken();
            ResultData<User> userRes = UserHelper.getUserInfo();
            if (userRes.isOk()) {
                Living.user = userRes.getData();
                return true;
            }
        }
        return false;
    }

    /**
     * 当登录按钮被点击时候调用
     *
     * @param view
     */
    public void onClick(View view) {
        login_btn.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isOk", doLogin());
                msg.setData(bundle);//bundle传值，耗时，效率低
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            String pwd = data.getStringExtra("pwd");
            if (phone == null || pwd == null)
                return;
            if (phone.length() > 1) {
                et_phone.setText(phone);
                et_pwd.setText(pwd);
                onClick(null);
            }
        }
    }
}
