package com.tencent.living;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        listView = (ListView)view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.id_swipe_ly);
        radioGroup = (RadioGroup)view.findViewById(R.id.emoGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        refreshListManager = new RecordsRefreshListManager(swipeRefreshLayout, listView, MAX_COMMENTS_LINE);
        setRefreshTarget();
        refreshListManager.updateData();
        return view;
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

    /**
     * 用于处理Activity的会跳
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }
}
