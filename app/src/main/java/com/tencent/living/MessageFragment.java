package com.tencent.living;

import android.os.Bundle;
import android.os.Handler;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.living.dataHelper.CommentHelper;
import com.tencent.living.dataHelper.LivingServerAgent;
import com.tencent.living.dataHelper.RecordHelper;
import com.tencent.living.models.Comment;
import com.tencent.living.models.Message;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements AbsListView.OnScrollListener , SwipeRefreshLayout.OnRefreshListener{
    private View loadmoreView;
    private ListView listView;
    private int last_index;
    private int total_index;
    private boolean isLoading = false;//表示是否正处于加载状态
    private MessageListViewAdapter adapter;
    private List<Message> newMesg;
    private SwipeRefreshLayout layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_frag_layout, container, false);
        loadmoreView = inflater.inflate(R.layout.message_load_more, null);//获得刷新视图
        listView = view.findViewById(R.id.message_frag_layout);
        layout = view.findViewById(R.id.layout);
        adapter = new MessageListViewAdapter(getActivity());
        loadmoreView.setVisibility(View.INVISIBLE);//设置刷新视图默认情况下是不可见的
        listView.setOnScrollListener(this);
        listView.addFooterView(loadmoreView, null, false);
        listView.setAdapter(adapter);
        layout.setOnRefreshListener(this);
        isLoading = false;
        startPullData(0);
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int target = data.getInt("target");
            if (data.getBoolean("isOk")) {
                if (target == 0) //刷新数据
                    adapter.clear();
                loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
                // 清除多余记录
                int giveUp = adapter.getCount() % LivingServerAgent.DATA_DATA_PER_PAGE;
                if (newMesg != null) {
                    for (int i = giveUp; i < newMesg.size(); i++)
                        adapter.addItem(newMesg.get(i));
                }
                adapter.notifyDataSetChanged();
                layout.setRefreshing(false);
                isLoading = false;//设置正在刷新标志位false
            }else{
                Toast.makeText(listView.getContext(), R.string.mesg_pull_failed, 2000).show();
            }
        }
    };
    /**
     * 起线程拉数据
     */
    public void startPullData(int curPage){
        final int _curPage = curPage;
        new Thread() {
            public void run() {
                android.os.Message msg = android.os.Message.obtain();
                Bundle bundle = new Bundle();
                ResultData<ArrayList<Message>> ret
                        = CommentHelper.getMessagesByUserId(_curPage);
                newMesg = ret.getData();
                bundle.putBoolean("isOk", (ret != null && ret.isOk()));
                msg.setData(bundle);
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        last_index = firstVisibleItem + visibleItemCount;
        total_index = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
            if (!isLoading) {
                //不处于加载状态的话对其进行加载
                isLoading = true;
                loadmoreView.setVisibility(View.VISIBLE);
                startPullData(adapter.getCount() / LivingServerAgent.DATA_DATA_PER_PAGE );
            }
        }
    }

    @Override
    public void onRefresh() {
        if (isLoading)
            return ;
        isLoading = true;
        startPullData(0);
    }
}
