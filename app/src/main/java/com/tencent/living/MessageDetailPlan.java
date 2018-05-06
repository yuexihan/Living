package com.tencent.living;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.living.models.Living;
import com.tencent.living.models.Message;

public class MessageDetailPlan {
    private Message message;
    private View view;
    private Context context;

    private ImageView profile;
    private TextView newsContent;
    private ImageView praiseImage;
    private TextView praiseText;

    private void findAllComp(){
        profile = view.findViewById(R.id.headImage);
        newsContent = view.findViewById(R.id.newsContent);
        praiseImage = view.findViewById(R.id.praiseImage);
        praiseText = view.findViewById(R.id.praiseText);
    }

    private void resetCompsContent(){
        profile.setImageResource(
                Living.profileID[
                        Integer.parseInt(message.getAvatar())
                        ]
        );
        switch (message.getType()){
            case 1: //点赞
                praiseImage.setVisibility(View.VISIBLE);
                praiseText.setVisibility(View.VISIBLE);
                newsContent.setText(message.getNickname() + context.getString(R.string.mesg_like));
                return ;
            case 2://心情回复
                newsContent.setText(message.getNickname()
                + context.getString(R.string.mesg_resp_emotion) + message.getComment());
                break;
            case 3://评论回复
                newsContent.setText(message.getNickname()
                + context.getString(R.string.mesg_resp_comment) + message.getComment());
                break;
        }
        praiseImage.setVisibility(View.INVISIBLE);
        praiseText.setVisibility(View.INVISIBLE);
    }

    public MessageDetailPlan(Context context, final Message message){
        this.message = message;
        this.context = context;
        view = LayoutInflater.from(this.context).inflate(R.layout.message_item, null);
        findAllComp();
        resetCompsContent();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("emotion_id", message.getEmotion_id());
                intent.setClass(MessageDetailPlan.this.context, RecordDetailActivity.class);
                MainActivity.messageFragment.startActivity(intent);
            }
        });
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
