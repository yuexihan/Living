package com.tencent.living;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;
import com.tencent.living.models.User;

public class UserFragment extends Fragment {
    private ListView listView;
    private RecordItemAdapter listViewAdapter;
    private ImageButton settingsbutton;
    public static User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.user_frag_layout, container, false);
        listView = (ListView)view.findViewById(R.id.listViewForUser);
        //设置适配器
        listViewAdapter = new RecordItemAdapter(this.getContext(),3);
        listView.setAdapter(listViewAdapter);
        settingsbutton = (ImageButton) view.findViewById(R.id.settingsbtn);
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

        /* 测试数据 */
        for (int i = 0; i < 5 ;i++) {
            Record record = new Record();
            record.setUserName("RaylHuang" + i);
            record.setEmoDegree(i);
            record.setContent("this is test " + i);
            record.setTime("2018-4-30 2:19");
            //捏造几个评论
            for (int j = 0; j < i; j++){
                Comment c = new Comment();
                c.setCommentContent("haha"  + j);
                c.setCommentFrom("User" + i + j);
                if (j != 0)
                    c.setCommentTo("User" + j + i);
                c.setTime(record.getTime());
                record.getComments().add(c);
            }
            record.setUpCount(i);
            record.setCommentCount(i * 100);
            listViewAdapter.addItem(record);
            listViewAdapter.notifyDataSetChanged();
        }
        return view;
    }
}
