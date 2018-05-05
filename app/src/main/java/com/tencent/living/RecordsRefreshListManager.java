package com.tencent.living;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.living.dataHelper.RecordHelper;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import java.util.ArrayList;

public class RecordsRefreshListManager implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    //用于展示数据的listView;
    private ListView listView;

    //用于显示reacord的适配器
    private RecordItemAdapter adapter;

    //用于拉下刷新的layout
    private SwipeRefreshLayout layout;
    private int curPage = 0;
    //要显示在listView中record的一些显示
    private int targetEmotion = -1; //-1表示全部
    private String user = null; //null 表示全部

    //用于实现上拉到底自动加载新数据
    private View footer;
    private int totalItem;
    private int lastItem;
    private boolean isLoading = false;
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

    public RecordsRefreshListManager(SwipeRefreshLayout layout, ListView list, int maxCommentsLine) {
        listView = list;
        this.layout = layout;
        adapter = new RecordItemAdapter(list.getContext(), maxCommentsLine);
        listView.setAdapter(adapter);
        layout.setOnRefreshListener(this);
        footer = View.inflate(list.getContext(), R.layout.load_more_footer, null);
        footer.setVisibility(View.GONE);
        listView.addFooterView(footer);
        listView.setOnScrollListener(this);
    }


    /**
     * 数据获得完毕后再这里处理界面，加载更多和更新数据都会在这里处理
     */
    private ArrayList<Record> newRecords;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isOk = data.getBoolean("isOk");
            int target = data.getInt("target");
            if (isOk) {
                if (target == 0)//刷新数据
                    adapter.clear();
                if (newRecords != null) {
                    for (int i = 0; i < newRecords.size(); i++)
                        adapter.addItem(newRecords.get(i));
                }
                adapter.notifyDataSetChanged();
            } else
                Toast.makeText(listView.getContext(), R.string.pub_record_fail, 2000).show();
            layout.setRefreshing(false);
            adapter.notifyDataSetChanged();
            isLoading = false;
        }
    };

    /**
     * 清除当前LIST中所有心情状态，重新请求
     */
    public boolean updateData() {
        ResultData<ArrayList<Record>> res;
        if (user == null)
            res = RecordHelper.getRecordsInGround(targetEmotion, 0);
        else
            res = RecordHelper.getRecordsByUserId(0);
        if (res == null || !res.isOk())
            return false;
        newRecords = res.getData();
        return true;
    }

    @Override
    public void onRefresh() {
        getRecords(0);
    }

    /**
     * 获得更多心情状态,0表示更新，1表示增加
     */
    private void getRecords(int target) {
        if (isLoading)
            return;
        isLoading = true;
        final int _target = target;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("target", _target); //区别是刷新数据还是拉取更多数据
                if (updateData())
                    bundle.putBoolean("isOk", true);
                else
                    bundle.putBoolean("isOk", false);
                msg.setData(bundle);//bundle传值，耗时，效率低
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    /**
     * 底下是滚动条拉动事件，用于实现上拉更新数据
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.setVisibility(View.VISIBLE);
                getRecords(1);
                footer.setVisibility(View.INVISIBLE);
                isLoading = false;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }
}
