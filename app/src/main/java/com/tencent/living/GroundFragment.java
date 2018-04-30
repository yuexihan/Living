package com.tencent.living;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroundFragment extends Fragment {
    private ListView listView;
    private RecordItemAdapter listViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ground_frag_layout, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        //设置适配器
        listViewAdapter = new RecordItemAdapter(this.getContext());
        listView.setAdapter(listViewAdapter);


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
