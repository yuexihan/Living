package com.tencent.living;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tencent.living.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListViewAdapter extends BaseAdapter {
    private List<MessageDetailPlan> list = new ArrayList<>();
    private Context context;
    public MessageListViewAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    public void addItem(Message message){
        list.add(new MessageDetailPlan(context, message));
    }
    @Override
    public MessageDetailPlan getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void removeItem(int position) {list.remove(position);}
    public void clear(){list.clear();}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return list.get(position).getView();
    }
}


