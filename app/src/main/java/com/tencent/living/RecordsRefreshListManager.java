package com.tencent.living;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.living.dataHelper.LivingServerAgent;
import com.tencent.living.dataHelper.RecordHelper;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import java.util.ArrayList;
import java.util.List;

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

    public static final int COMMENT_LINES_NO_LIMIT = RecordDetailPlan.COMMENT_LINES_NO_LIMIT;

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
            int target = data.getInt("target");
            if (data.getBoolean("isOk")) {
                if (target == 0) //刷新数据
                    adapter.clear();
                //清除多余记录
                int giveUp = adapter.getCount() % LivingServerAgent.DATA_DATA_PER_PAGE;
                for (int i = 0; i < giveUp; i++)
                    if (adapter.getCount() != 0)
                        adapter.removeItem(adapter.getCount() - 1);
                //加入新记录
                if (newRecords != null) {
                    for (int i = 0; i < newRecords.size(); i++)
                        adapter.addItem(newRecords.get(i));
                }
                curPage = adapter.getCount() / LivingServerAgent.DATA_DATA_PER_PAGE;
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
    public boolean updateData(int curPage) {
        ResultData<ArrayList<Record>> res;
        if (user == null)
            res = RecordHelper.getRecordsInGround(targetEmotion, curPage);
        else
            res = RecordHelper.getRecordsByUserId(curPage);
        if (res == null || !res.isOk())
            return false;
        if (user != null && res.getData() != null) {
            //发现后端传回来的数据里面没有用户信息，我们自己补上
            for (int i = 0; i < res.getData().size(); i++){
                Record r = res.getData().get(i);
                r.setAvatar(Living.user.getAvatar());
                r.setNickname(Living.user.getNickname());
            }

        }
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
        final int page = target == 0 ? 0 : curPage;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("target", _target); //区别是刷新数据还是拉取更多数据
                bundle.putBoolean("isOk", updateData(page));
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
            getRecords(1);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }


    /**
     * 某个心情状态被添加了一个评论
     */
    public void addComment(int emotionID, String to, int toID, String content){
        List<RecordDetailPlan> plist = adapter.getData();
        for (int i = 0 ; i < plist.size(); i++)
            if(plist.get(i).getRecord().getEmotion_id() == emotionID) {
                plist.get(i).addComment(content, to, toID);
                break;
            }
    }
}
