package com.tencent.living;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

public class RecordsRefreshListManager implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    //用于展示数据的listView;
    private ListView listView;

    //用于显示reacord的适配器
    private RecordItemAdapter adapter;

    //用于拉下刷新的layout
    private SwipeRefreshLayout layout;

    //要显示在listView中record的一些显示
    private int targetEmotion = -1; //-1表示全部
    private String user = null; //null 表示全部

    //用于实现上拉到底自动加载新数据
    private View footer;
    private int totalItem;
    private int lastItem;
    private boolean isLoading;
    private LayoutInflater inflater;


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
        footer = View.inflate(list.getContext(),R.layout.load_more_footer,null );
        footer.setVisibility(View.GONE);
        listView.addFooterView(footer);
        listView.setOnScrollListener(this);
    }


    /**
     * 清除当前LIST中所有得心情状态，重新请求
     */
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

    /**
     * 获得更多心情状态
     */
    private void getMoreRecord(){

        //@TODO 这里添加获得更多心情数据的请求
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
    /**
     * 底下是滚动条拉动事件，用于实现上拉更新数据
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(this.totalItem == lastItem&&scrollState == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading=true;
                footer.setVisibility(View.VISIBLE);
                onLoadMore();
                footer.setVisibility(View.INVISIBLE);
                isLoading = false;
            }
        }
    }
    private void onLoadMore(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMoreRecord();
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem+visibleItemCount;
        this.totalItem = totalItemCount;
    }
}
