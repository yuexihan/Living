package com.tencent.living;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

public class GroundFragment extends Fragment {
    private ListView listView;
    private RecordsRefreshListManager refreshListManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup radioGroup;
    private static final int MAX_COMMENTS_LINE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ground_frag_layout, container, false);
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.id_swipe_ly);
        radioGroup = view.findViewById(R.id.emoGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        refreshListManager = new RecordsRefreshListManager(swipeRefreshLayout, listView, MAX_COMMENTS_LINE);
        return view;
    }

    public void refreashData(){
        setRefreshTarget();
        refreshListManager.onRefresh();
    }
    private void setRefreshTarget(){
        refreshListManager.setUser(null);
        int checkID = radioGroup.getCheckedRadioButtonId();
        switch (checkID){
            case R.id.all:
                refreshListManager.setTargetEmotion(-1);
                break;
            case R.id.item_happy:
                refreshListManager.setTargetEmotion(0);
                break;
            case R.id.item_anger:
                refreshListManager.setTargetEmotion(1);
                break;
            case R.id.item_sad:
                refreshListManager.setTargetEmotion(2);
                break;
            case R.id.item_calm:
                refreshListManager.setTargetEmotion(3);
                break;
        }
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (refreshListManager != null){
                switch (checkedId){
                    case R.id.rbut_all:
                        refreshListManager.setTargetEmotion(-1);
                        break;
                    case R.id.rbut_happy:
                        refreshListManager.setTargetEmotion(0);
                        break;
                    case R.id.rbut_anger:
                        refreshListManager.setTargetEmotion(1);
                        break;
                    case R.id.rbut_sad:
                        refreshListManager.setTargetEmotion(2);
                        break;
                    case R.id.rbut_calm:
                        refreshListManager.setTargetEmotion(3);
                        break;
                }
                setRefreshTarget();
                refreshListManager.onRefresh();
            }
        }
    };

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
                if (refreshListManager != null)
                    refreshListManager.addComment(emoID, to, toID, content);
                break;
        }
    }
}
