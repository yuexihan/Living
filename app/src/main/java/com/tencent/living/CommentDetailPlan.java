package com.tencent.living;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.living.models.Comment;

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
    //private TextView commentTime;
    private TextView respondText;
    private OnCommentClickListener oclistern;

    public CommentDetailPlan(Context context, Comment comment){
        this.context = context;
        this.comment = comment;
        view = LayoutInflater.from(this.context).inflate(R.layout.comment_item_layout, null);
        commentFrom = view.findViewById(R.id.commentFrom);
        commentTo = view.findViewById(R.id.commentTo);
        commentContent = view.findViewById(R.id.commentContent);
        //commentTime = view.findViewById(R.id.commentTime);
        respondText = view.findViewById(R.id.respondText) ;
        //初始化评论内容
        if (comment.getPoster_nickname() != null)
            commentFrom.setText(comment.getPoster_nickname());
        if (comment.getRspto_nickname() != null)
            commentTo.setText(comment.getRspto_nickname());
        if (comment.getComment() != null)
            commentContent.setText(comment.getComment());
        //commentTime.setText(comment.getCreate_time());
        if (comment.getRspto() == 0) {
            respondText.setText("");
        }
        view.setOnClickListener(clickListener);
    }
    //通过获得View可以让这个界面给其他组件复用
    public View getView(){
        return view;
    }

    public Comment getData(){
        return comment;
    }

    public void setOnCommentClick(OnCommentClickListener oclistern){
        this.oclistern = oclistern;
    }
    private View.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v) {
            if (oclistern == null)
                return ;
            oclistern.onCommentClick(comment);
        }
    };
    public interface OnCommentClickListener{
        void onCommentClick(Comment comment);
    }
}
