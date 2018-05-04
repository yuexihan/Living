package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.Random;

public class RegisterActivity extends Activity{
    private RadioGroup rgroup;
    private TextView nickName;
    private Button register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rgroup = (RadioGroup)findViewById(R.id.choose_profile_RG);
        nickName = (TextView)findViewById(R.id.nickname);
        register_btn = findViewById(R.id.confirm_button);
        register_btn.setOnClickListener(finishAction);
        nickName.setOnClickListener(nickChangeAction);
        String nickStr = Living.nickNames[Math.abs(new Random().nextInt()) % Living.nickNames.length];
        nickName.setText(nickStr);
    }


    /**
     * 当点击昵称时候被调用
     */
    private View.OnClickListener nickChangeAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nickStr = Living.nickNames[Math.abs(new Random().nextInt()) % Living.nickNames.length];
            nickName.setText(nickStr);
        }
    };

    /**
     * 当点击完成时候被调用
     */
    private View.OnClickListener finishAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nickname = nickName.getText().toString();
            String image = "0";
            switch(rgroup.getCheckedRadioButtonId()){
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

            //注册
           // ResultData<Post> res = UserHelper.postRegister()


            //直接到主界面
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
