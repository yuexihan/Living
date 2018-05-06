package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.living.models.Living;
import com.tencent.living.tools.FontManager;

public class UserFragment extends Fragment {
    private ListView listView;
    private View userView;
    private SwipeRefreshLayout layout;
    private TextView settings_button;
    private ImageView profile;
    private TextView name;
    private RecordsRefreshListManager refreshListManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.user_frag_layout, container, false);
        userView = inflater.inflate(R.layout.user_profile_layout, null);//用户状态
        listView = view.findViewById(R.id.listViewForUser);
        listView.addHeaderView(userView);
        layout = view.findViewById(R.id.refreshLayout);
        profile = view.findViewById(R.id.profile);
        name = view.findViewById(R.id.name);
        profile.setImageResource(Living.profileID[Integer.parseInt(Living.user.getAvatar())]);
        name.setText(Living.user.getNickname());
        refreshListManager = new RecordsRefreshListManager(layout, listView, 3);
        refreshListManager.setUser(Living.user.getNickname());

        settings_button = view.findViewById(R.id.icon_setting);
        if (getActivity() != null){
            //获取assets文件夹里的字体文件,getAssets()是context中的方法，fragment不可用
            Typeface font = Typeface.createFromAsset( getActivity().getAssets(), FontManager.FONTAWESOME);
            //给指定的TextView加载字体
            settings_button.setTypeface(font);
        }
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 设置要跳转的页面
                intent.setClass(getActivity(), SettingsActivity.class);
                // 开始Activity
                getActivity().startActivityForResult(intent,MainActivity.LOGOUT_REQUEST_CODE);
//                startActivity(intent);
            }
        });
        return view;
    }
    public void refreashData(){
        refreshListManager.onRefresh();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK)
            return ;
        switch(requestCode){
            //评论编辑框返回
            case MainActivity.COMMENT_EDIT_REQUEST_CODE:
                int emoID = data.getIntExtra("emotionID", 0);
                String content = data.getStringExtra("content");
                String to = data.getStringExtra("to");
                int toID = data.getIntExtra("toID", 0);
                if (refreshListManager!= null)
                    refreshListManager.addComment(emoID, to, toID, content);
                break;
            case MainActivity.LOGOUT_REQUEST_CODE:
                Intent intent = new Intent();
                intent.setClass(this.getActivity(),LoginActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                break;
        }
    }
}
