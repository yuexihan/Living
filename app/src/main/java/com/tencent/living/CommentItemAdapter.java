package com.tencent.living;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tencent.living.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentItemAdapter extends BaseAdapter {
    private List<CommentDetailPlan> data = new ArrayList<>();
    private Context context;
    public CommentItemAdapter(Context context) {
        this.context = context;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return data.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public CommentDetailPlan addItem(Comment comment){
        CommentDetailPlan cdp = new CommentDetailPlan(context, comment);
        data.add(cdp);
        return cdp;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        return data.get(position).getView();
    }
    public void clear(){
        data.clear();
    }
}
