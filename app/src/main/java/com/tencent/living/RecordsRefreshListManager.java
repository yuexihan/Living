package com.tencent.living;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

public class RecordsRefreshListManager implements SwipeRefreshLayout.OnRefreshListener{

    //用于展示数据的listView;
    private ListView listView;

    //用于显示reacord的适配器
    private RecordItemAdapter adapter;

    //用于拉下刷新的layout
    private SwipeRefreshLayout layout;

    //要显示在listView中record的一些显示
    private int targetEmotion = -1; //-1表示全部
    private String user = null; //null 表示全部

    public RecordItemAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecordItemAdapter adapter) {
        this.adapter = adapter;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public int getTargetEmotion() {
        return targetEmotion;
    }

    public void setTargetEmotion(int targetEmotion) {
        this.targetEmotion = targetEmotion;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public RecordsRefreshListManager(SwipeRefreshLayout layout, ListView list, int maxCommentsLine){
        listView = list;
        this.layout = layout;
        adapter = new RecordItemAdapter(list.getContext(),maxCommentsLine);
        listView.setAdapter(adapter);
        layout.setOnRefreshListener(this);
    }



    public void updateData(){

        Toast.makeText(listView.getContext(), "更新数据", 3000).show();

        //@TODO 在这里刷新数据,注意targetEmotion和user限制
        adapter.clear();
        /* 测试数据 */
        for (int i = 0; i < 20 ;i++) {

            if ((i % 4) != targetEmotion && targetEmotion != -1)
                continue;

            Record record = new Record();
            record.setUserName("RaylHuang" + i);
            record.setEmoDegree(i);
            record.setContent("this is test " + i);
            record.setTime("2018-4-30 2:19");
            record.setEmoType(i%4);
            record.setImageType(i%9);

            //捏造几个评论
            for (int j = 0; j < i % 8; j++){
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
            adapter.addItem(record);
        }
    }

    @Override
    public void onRefresh() {
        updateData();
        if (layout.isRefreshing())
            layout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }
}
