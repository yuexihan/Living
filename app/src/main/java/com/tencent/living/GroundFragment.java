package com.tencent.living;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ground_frag_layout, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.id_swipe_ly);
        radioGroup = (RadioGroup)view.findViewById(R.id.emoGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        refreshListManager = new RecordsRefreshListManager(swipeRefreshLayout, listView);
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
            setRefreshTarget();
            refreshListManager.onRefresh();
        }
    };
}
