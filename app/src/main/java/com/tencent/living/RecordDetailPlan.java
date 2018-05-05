package com.tencent.living;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 该类用于显示一个心情状态
 */
public class RecordDetailPlan {
    private CircleImageView profile;
    private TextView userName;
    private TextView time;
    private TextView content;
    private ProgressBar emoDegree;
    private ImageView emoView;
    private ImageButton upButton;
    private TextView upCount;
    private ImageButton commentButton;
    private TextView commentCount;
    private TextView moreComment;
    private View view;
    private Context context;
    private Record record;
    private LinearLayout linearLayout;
    private ListView commentsList;
    private CommentItemAdapter adapter;
    private int commentsLineLimit;
    private ImageButton backButton;

    //是否支持点击进入详情页
    private boolean isClickAble = true;
    public static final int COMMENT_LINES_NO_LIMIT = Integer.MAX_VALUE;
    private void findAllComp(){
        profile = (CircleImageView)view.findViewById(R.id.profileImage);
        userName = (TextView)view.findViewById(R.id.userName);
        time = (TextView)view.findViewById(R.id.time);
        content = (TextView)view.findViewById(R.id.content);
        emoDegree = (ProgressBar)view.findViewById(R.id.emoBar);
        emoView = (ImageView)view.findViewById(R.id.emoView) ;
        upButton = (ImageButton)view.findViewById(R.id.upButton);
        upCount = (TextView)view.findViewById(R.id.upCount);
        commentButton = (ImageButton)view.findViewById(R.id.commentButton);
        commentCount = (TextView)view.findViewById(R.id.commentCount);
        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        commentsList = (ListView)view.findViewById(R.id.commentsList);
        backButton = (ImageButton)view.findViewById(R.id.backButton);
        moreComment = (TextView) view.findViewById(R.id.moreComments);
    }

    private void setListViewHeightBasedOnChildren() {
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, commentsList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = commentsList.getLayoutParams();
        params.height = totalHeight
                + (commentsList.getDividerHeight() * (adapter.getCount() - 1));
        commentsList.setLayoutParams(params);
    }

    public void setBackButtonVisiable(boolean visiable){
        if (visiable)
            backButton.setVisibility(View.VISIBLE);
        else
            backButton.setVisibility(View.GONE);
    }

    public void setClickAble(boolean isClickAble){
        this.isClickAble = isClickAble;
    }

    private void addCommentsToList(){
        adapter.clear();
        List<Comment> comments = record.getComments();
        for (int i = 0 ; i < comments.size() && i < commentsLineLimit; i++) {
            CommentDetailPlan cdp = adapter.addItem(comments.get(i));
            cdp.setOnCommentClick(onCommentClickListener);
            adapter.notifyDataSetChanged();
        }
        if (commentsLineLimit != COMMENT_LINES_NO_LIMIT)
            setListViewHeightBasedOnChildren();

        if (comments.size() >= commentsLineLimit){
            moreComment.setVisibility(View.VISIBLE);
        }else
            moreComment.setVisibility(View.GONE);
    }

    private void resetCompsContent(){
        //profile.set  ...................
        profile.setImageResource(Living.profileID[record.getImageType()]);
        //其他属性设置
        userName.setText(record.getUserName());
        time.setText(record.getTime());
        content.setText(record.getContent());
        emoDegree.setProgress(record.getEmoDegree());
        upCount.setText(record.getUpCount() + "");
        if (record.getCommentCount() >= 999){
            commentCount.setText("999");
        }else
            commentCount.setText(record.getCommentCount() +"");
        //设置表情
        switch(record.getEmoType()){
            case 0:
                emoView.setImageResource(R.drawable.happy);
                break;
            case 1:
                emoView.setImageResource(R.drawable.anger);
                break;
            case 2:
                emoView.setImageResource(R.drawable.sad);
                break;
            case 3:
                emoView.setImageResource(R.drawable.calm);
                break;
        }
        adapter = new CommentItemAdapter(this.context);
        commentsList.setAdapter(adapter);
        upButton.setOnClickListener(upClickListener);
        commentButton.setOnClickListener(commentClickListener);

        addCommentsToList();
    }

    public RecordDetailPlan(Context context, Record record, int commentsLineLimit){
        this.commentsLineLimit = commentsLineLimit;
        this.context = context;
        this.record = record;
        view = LayoutInflater.from(this.context).inflate(R.layout.record_item_detail_layout, null);
        view.setOnClickListener(planClickListener);
        findAllComp();
        resetCompsContent();
    }
    //通过获得View可以让这个界面给其他组件复用
    public View getView(){
        return view;
    }
    public Record getData(){
        return record;
    }

    /**
     * 当该页面被点击的时候会被调用
     * @param v
     */
    private View.OnClickListener planClickListener = new  View.OnClickListener(){
        public void onClick(View v) {
            if (!isClickAble)
                return ;
            Toast.makeText(context, record.getUserName(), 3000).show();
        }
    };

    /**
     * 当点赞被点击的时候
     */
    private View.OnClickListener upClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            //@TODO 以下逻辑需要被替换成点赞的真正处理逻辑
            Toast.makeText(context, "点赞", 3000).show();
        }
    };

    private void startFloatEditor(String from, String to, String fromID, String toID){
        Intent intent = new Intent();
        // 设置要跳转的页面
        intent.setClass((Activity)context , FloatEditorActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("fromID", fromID);
        intent.putExtra("toID", toID);
        // 开始Activity
        ((Activity)context).startActivityForResult(intent ,MainActivity.COMMENT_EDIT_REQUEST_CODE);
    }

    /**
     * 当评论按钮被点击的时候
     */
    private View.OnClickListener commentClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            //@TODO 以下逻辑需要被替换成真正的评论逻辑
            Toast.makeText(context, "评论", 3000).show();
            startFloatEditor("","","","");
        }
    };

    /**
     * 当评论被点击的时候的回调函数
     */
    private CommentDetailPlan.OnCommentClickListener onCommentClickListener = new CommentDetailPlan.OnCommentClickListener() {
        @Override
        public void onCommentClick(Comment comment) {
            //@TODO 以下逻辑需要被替换成评论被点击时候的逻辑，其中可以从comment中获得被点击的评论内容
            Toast.makeText(context, comment.getCommentFrom(), 3000).show();
        }
    };
}
