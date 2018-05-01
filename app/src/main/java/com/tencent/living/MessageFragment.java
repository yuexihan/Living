package com.tencent.living;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.tencent.living.Data.MessageListItem;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements AbsListView.OnScrollListener{

    public View loadmoreView;
    public LayoutInflater inflater;
    public ListView listView;
    public int last_index;
    public int total_index;
    public boolean isLoading = false;//表示是否正处于加载状态
    public MessageListViewAdapter adapter;
    public List<MessageListItem> list1 = new ArrayList<MessageListItem>();
    public List<MessageListItem> list2 = new ArrayList<MessageListItem>();

    public String data1 = "{\"error\":false,\"results\":[{\"content\":\"asoon点赞了你的状态\",\"headimage\":\"head\",\"praise\":true},{\"content\":\"BBBBBBBBBB\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"CCCCCCCCCC\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"felixliwang评价了你的状态:在第19行获得了加载更多的刷新视图并且第20行设置该视图是可见的，因为我们模拟的是要加载两页的数据，这样的话第一页加载结束之后需要显示加载更多的视图\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"EEEEEEEEEE\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"FFFFFFFFFF\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"GGGGGGGGGG\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"HHHHHHHHHH\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"IIIIIIIIII\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"JJJJJJJJJJ\",\"headimage\":\"head\",\"praise\":false}]}";
    public String data2 = "{\"error\":false,\"results\":[{\"content\":\"aaaaaaaaaa\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"bbbbbbbbbb\",\"headimage\":\"head\",\"praise\":true},{\"content\":\"cccccccccc\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"dddddddddd\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"eeeeeeeeee\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"ffffffffff\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"gggggggggg\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"hhhhhhhhhh\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"iiiiiiiiii\",\"headimage\":\"head\",\"praise\":false},{\"content\":\"jjjjjjjjjj\",\"headimage\":\"head\",\"praise\":false}]}";

    MessageItemApi itemApi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.message_frag_layout, container, false);
        loadmoreView = inflater.inflate(R.layout.message_load_more, null);//获得刷新视图
        loadmoreView.setVisibility(View.VISIBLE);//设置刷新视图默认情况下是不可见的
        listView = (ListView) view.findViewById(R.id.message_frag_layout);
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new MessageListViewAdapter(getActivity(), list1);
        listView.setOnScrollListener((AbsListView.OnScrollListener) this);
        listView.addFooterView(loadmoreView,null,false);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * 初始化我们需要加载的数据
     * @param
     * @param
     */
    public void initList() throws JSONException {
        itemApi = new MessageItemApi();
        list1 = itemApi.parseData(data1);
        list2 = itemApi.parseData(data1);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        last_index = firstVisibleItem+visibleItemCount;
        total_index = totalItemCount;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
        {
            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
            if(!isLoading)
            {
                //不处于加载状态的话对其进行加载
                isLoading = true;
                //设置刷新界面可见
                loadmoreView.setVisibility(View.VISIBLE);
                onLoad();
            }
        }
    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try {
            //模拟耗时操作
            //实际为网络请求
            Thread.sleep(500);
            List<MessageListItem> newList = new ArrayList<MessageListItem>();
            newList = itemApi.parseData(data2);
            list2.addAll(newList);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(adapter == null)
        {
            adapter = new MessageListViewAdapter(this, list1);
            listView.setAdapter(adapter);
        }else
        {
            adapter.updateView(list2);

        }
        isLoading = false;
//        loadComplete();//刷新结束
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
        isLoading = false;//设置正在刷新标志位false
//        MessageFragment.this.invalidateOptionsMenu();
        listView.removeFooterView(loadmoreView);//如果是最后一页的话，则将其从ListView中移出
    }




}
