package com.tencent.living;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tencent.living.models.Message;

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
    public List<Message> list1 = new ArrayList<Message>();
    public List<Message> list2 = new ArrayList<Message>();



    public String data1 = "{\"error\":false,\"results\":\n" +
            "[{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"压力测试，就是  被测试的系统，在一定的访问压力下，看程序运行是否稳定/服务器运行是否稳定（资源占用情况）\",\"avatar\":\"0\",\"poster\":0,\"type\":2,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"一二三四五六七八\",\"avatar\":\"0\",\"poster\":1,\"type\":3,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":2,\"type\":1,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":3,\"type\":1,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":4,\"type\":1,\"nickname\":\"felixliwang\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"aaaaa\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"aaaaa\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"aaaaa\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"aaaaa\"},\n" +
            "{\"emotion_id\":0,\"comment\":\"\",\"avatar\":\"0\",\"poster\":0,\"type\":1,\"nickname\":\"aaaaa\"}]}";
    public String data2 = data1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.message_frag_layout, container, false);
        loadmoreView = inflater.inflate(R.layout.message_load_more, null);//获得刷新视图

        listView = (ListView) view.findViewById(R.id.message_frag_layout);
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new MessageListViewAdapter(getActivity(), list1);

        if(list1.size()<10){
            loadmoreView.setVisibility(View.INVISIBLE);//设置刷新视图默认情况下是不可见的
        }else{
            loadmoreView.setVisibility(View.VISIBLE);
        }

        listView.setOnScrollListener((AbsListView.OnScrollListener) this);
        listView.addFooterView(loadmoreView,null,false);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("emotion_id", list1.get(arg2).getEmotion_id());
//                Intent intent = new Intent();
//                intent.putExtras(bundle);
//                intent.setClass(MessageFragment.this, MessageContent.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    /**
     * 初始化我们需要加载的数据
     * @param
     * @param
     */
    public void initList() throws JSONException {
        list1 = MessageItemApi.parseData(data1);
        list2 = MessageItemApi.parseData(data1);
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
            List<Message> newList = new ArrayList<Message>();
            newList = MessageItemApi.parseData(data2);
            if(newList == null){
                loadComplete();

            }else{
                list2.addAll(newList);
                if(adapter == null)
                {
                    adapter = new MessageListViewAdapter(this, list1);
                    listView.setAdapter(adapter);
                }else
                {
                    adapter.updateView(list2);

                }
                isLoading = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

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
