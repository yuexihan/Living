package com.tencent.living;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.tencent.living.dataHelper.UserHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;

import java.util.Random;

import static android.os.SystemClock.sleep;

public class RegisterActivity extends Activity {
    private RadioGroup rgroup;
    private TextView nickName;
    private EditText phone;
    private EditText pwd1;
    private EditText pwd2;
    private Button register_btn;
    private ImageButton dice_button;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rgroup =  findViewById(R.id.choose_profile_RG);
        nickName = findViewById(R.id.nickname);
        pwd1 = findViewById(R.id.passwd);
        pwd2 =  findViewById(R.id.passwd2);
        phone = findViewById(R.id.phone);
        pb = findViewById(R.id.progressBar);
        register_btn = findViewById(R.id.confirm_button);
        dice_button = findViewById(R.id.dice_button);
        register_btn.setOnClickListener(finishAction);
        dice_button.setOnClickListener(nickChangeAction);
        String nickStr = Living.nickNames[Math.abs(new Random().nextInt()) % Living.nickNames.length];
        nickName.setText(nickStr);
    }

    /**
     * 当点击昵称时候被调用
     */
    private View.OnClickListener nickChangeAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //onScaleAnimation(); // 骰子放大效果
            Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.button_shake);
            shake.reset();
            shake.setFillAfter(true);
            dice_button.startAnimation(shake);
            String nickStr = Living.nickNames[Math.abs(new Random().nextInt()) % Living.nickNames.length];
            nickName.setText(nickStr);
//            dice_button.clearAnimation();
        }
    };

    /**
     * 真正的登录操作，成功返回true 失败返回false
     */
    private boolean doRegister() {
        String image = "0";
        switch (rgroup.getCheckedRadioButtonId()) {
            case R.id.b0:
                image = "0";
                break;
            case R.id.b1:
                image = "1";
                break;
            case R.id.b2:
                image = "2";
                break;
        }
        ResultData<Post> res = UserHelper.postRegister(
                phone.getText().toString(),
                nickName.getText().toString(),
                pwd1.getText().toString(),
                image
        );
        if (res == null || !res.isOk()){
            return false;
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isOk = data.getBoolean("isOk");

            if (isOk) {
                //这里我们直接返回登录页面并让其自动登录。
                //如果在这里直接跳入主页面，则会导致登录页面依然存在。
                //这样的话如果在主页面点击返回依旧会返回到登录页面，从逻辑上讲不通
                Intent intent = RegisterActivity.this.getIntent();
                intent.putExtra("phone", phone.getText().toString());
                intent.putExtra("pwd", pwd1.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                register_btn.setVisibility(View.VISIBLE);
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(R.string.register_fail)
                        , Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * 当点击完成时候被调用
     */
    private View.OnClickListener finishAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //检测两次密码是否一致
            if (!pwd1.getText().toString().equals(pwd2.getText().toString())){
                Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(R.string.pwd_check_err)
                        ,Toast.LENGTH_LONG).show();
                return ;
            }
            //起线程操作
            register_btn.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
            new Thread() {

                public void run() {
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    if (doRegister())
                        bundle.putBoolean("isOk", true);
                    else
                        bundle.putBoolean("isOk", false);
                    msg.setData(bundle);
                    handler.sendMessage(msg);//发送message信息
                }
            }.start();

        }
    };

    private void onScaleAnimation(){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(dice_button,"scaleX",1.0f,1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(dice_button,"scaleY",1.0f,1.5f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(900);
        set.playTogether(animatorX,animatorY);
        set.start();
    }
}
