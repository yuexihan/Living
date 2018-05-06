package com.tencent.living;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;

import org.w3c.dom.Text;

import java.util.Set;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends Activity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        //设置信息
        setVal();

        Button logout_button = findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                //直接到主界面
                new Thread() {
                    public void run() {
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isOk", doLogout());
                        msg.setData(bundle);//bundle传值，耗时，效率低
                        handler.sendMessage(msg);//发送message信息
                    }
                }.start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data.getBoolean("isOk")) {
                //saveUserToConfig();
                SharedPreferences settings = getSharedPreferences(Living.config, 0);
                SharedPreferences.Editor editor = settings.edit();
                // 清空登陆数据
                editor.putString("phone", "");
                editor.putString("pwd", "");
                // Commit the edits!
                editor.commit();
//                Intent intent = new Intent();
//                intent.setClass(SettingsActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            } else{
                Toast.makeText(SettingsActivity.this, R.string.logout_failed, Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean doLogout() {
        //检测logout
        ResultData<Post> res = UserHelper.postLogout();
        return res != null && res.isOk();
    }

    private void setVal(){
        User user = Living.user;
        TextView t = findViewById(R.id.nicknameval);
        t.setText(user.getNickname());

        t = findViewById(R.id.versionval);
        String appVersion = "0.0";
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        t.setText(appVersion);

    }
}
