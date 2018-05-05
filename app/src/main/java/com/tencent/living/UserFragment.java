package com.tencent.living;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;


public class UserFragment extends Fragment {
    private ListView listView;
    private RecordItemAdapter listViewAdapter;
    private ImageButton settingsbutton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.user_frag_layout, container, false);
        listView = view.findViewById(R.id.listViewForUser);
        //设置适配器
        listViewAdapter = new RecordItemAdapter(this.getContext(),3);
        listView.setAdapter(listViewAdapter);
        settingsbutton = view.findViewById(R.id.settingsbtn);
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 设置要跳转的页面
                intent.setClass(getActivity(), SettingsActivity.class);
                // 开始Activity
                startActivity(intent);
            }
        });
        return view;
    }
}
