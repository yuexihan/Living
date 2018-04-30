package com.tencent.living;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.living.Data.Comment;
/*
用于显示一条评论
 */
public class CommentDetailPlan {
    private View view;
    private Context context;
    private Comment comment;
    private TextView commentContent;
    private TextView commentFrom;
    private TextView commentTo;
    private TextView commentTime;
    private TextView respondText;
    public CommentDetailPlan(Context context, Comment comment){
        this.context = context;
        this.comment = comment;
        view = LayoutInflater.from(this.context).inflate(R.layout.comment_item_layout, null);
        commentFrom = (TextView)view.findViewById(R.id.commentFrom);
        commentTo = (TextView)view.findViewById(R.id.commentTo);
        commentContent = (TextView)view.findViewById(R.id.commentContent);
        commentTime = (TextView)view.findViewById(R.id.commentTime);
        respondText = (TextView)view.findViewById(R.id.respondText) ;
        //初始化评论内容
        commentFrom.setText(comment.getCommentFrom());
        commentTo.setText(comment.getCommentTo());
        commentContent.setText(comment.getCommentContent());
        commentTime.setText(comment.getTime());
        if (comment.getCommentTo() == null ||
                comment.getCommentTo().equals(""))
            respondText.setText("");
    }
    //通过获得View可以让这个界面给其他组件复用
    public View getView(){
        return view;
    }

    public Comment getData(){
        return comment;
    }
}
