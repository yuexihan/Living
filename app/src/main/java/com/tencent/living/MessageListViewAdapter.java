package com.tencent.living;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tencent.living.models.Message;

import java.util.List;

import static com.tencent.living.Living.profileID;

public class MessageListViewAdapter extends BaseAdapter {

    public List<Message> list;
    public LayoutInflater inflater;

    public MessageListViewAdapter(MessageFragment messageFragment, List<Message> list1) {
    }

    public MessageListViewAdapter(Context context, List<Message> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Message getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateView(List<Message> nowList)
    {
        this.list = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        Message message = list.get(position);
        if(convertView == null)
        {
            view = inflater.inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.headImage = (ImageView)view.findViewById(R.id.headImage);
            holder.content = (TextView)view.findViewById(R.id.newsContent);
            holder.praiseImage = (ImageView)view.findViewById(R.id.praiseImage);
            holder.praiseNum = (TextView)view.findViewById(R.id.praiseText);
            view.setTag(holder);//为了复用holder
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        holder.headImage.setImageResource(profileID[message.getPoster()]);
        holder.content.setText(message.getContentString());
        if(message.getType() == 1){
            holder.praiseImage.setImageResource(R.drawable.heart);
            holder.praiseNum.setText("+1");
        }else{
            holder.praiseImage.setImageResource(0);  //
            holder.praiseNum.setText("");
        }
        return view;
    }



    static class ViewHolder
    {
        ImageView headImage;
        TextView content;
        ImageView praiseImage;
        TextView praiseNum;
    }
}


