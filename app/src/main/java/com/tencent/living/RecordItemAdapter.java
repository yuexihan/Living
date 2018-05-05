package com.tencent.living;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tencent.living.models.Record;

import java.util.ArrayList;
import java.util.List;
public class RecordItemAdapter extends BaseAdapter {
    private List<RecordDetailPlan> data = new ArrayList<>();
    private Context context;
    private int commentsLineLimit;

    public RecordItemAdapter(Context context, int commentsLineLimit) {
        this.commentsLineLimit = commentsLineLimit;
        this.context = context;
    }

    public int getCount() {
        return data.size();
    }
    public void removeItem(int index){
        data.remove(index);
    }
    public Object getItem(int position) {
        return data.get(position);
    }
    public List<RecordDetailPlan> getData(){return data;}

    public long getItemId(int position) {
        return position;
    }
    public RecordDetailPlan addItem(Record record){
        RecordDetailPlan rdp = new RecordDetailPlan(context, record, commentsLineLimit);
        data.add(rdp);
        return rdp;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        return data.get(position).getView();
    }
    public void clear(){
        data.clear();
    }
}
