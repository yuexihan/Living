package com.tencent.living;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.tencent.living.dataHelper.CommentHelper;
import com.tencent.living.dataHelper.LivingServerAgent;
import com.tencent.living.models.Comment;
import com.tencent.living.models.Living;
import com.tencent.living.models.Post;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;
import com.tencent.living.tools.FloatEditorActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 该类用于显示一个心情状态
 */
public class RecordDetailPlan implements AbsListView.OnScrollListener {
    private CircleImageView profile;
    private TextView userName;
    private TextView time;
    private TextView content;
    private ProgressBar emoDegree;
    private ImageView emoView;
    private ImageButton likeButton;
    private TextView likeCount;
    private ImageButton commentButton;
    private TextView commentCount;
    private TextView moreComment;
    private View view;
    private Context context;
    private Record record;
    private LinearLayout linearLayout;
    private MyListView commentsList;
    private CommentItemAdapter adapter;
    private int commentsLineLimit;


    //用于实现上拉到底自动加载新数据
    private View footer;
    private int totalItem;
    private int lastItem;
    private boolean isLoading = false;
    //如果最多显示的评论有限制那么我们就在初始化的时候拉取1页，
    //如果最多显示的评论没有限制，那么我们就最多初始化5页数据
    private int initPageNum = 3;
    private int curInitPageNum = 0;
    public static final int COMMENT_LINES_NO_LIMIT = Integer.MAX_VALUE;
    private ArrayList<Comment> newComments;

    private void findAllComp(){
        profile = view.findViewById(R.id.profileImage);
        userName = view.findViewById(R.id.userName);
        time = view.findViewById(R.id.time);
        content = view.findViewById(R.id.content);
        emoDegree = view.findViewById(R.id.emoBar);
        emoView = view.findViewById(R.id.emoView) ;
        likeButton = view.findViewById(R.id.upButton);
        likeCount = view.findViewById(R.id.upCount);
        commentButton = view.findViewById(R.id.commentButton);
        commentCount = view.findViewById(R.id.commentCount);
        linearLayout = view.findViewById(R.id.linearLayout);
        commentsList = view.findViewById(R.id.commentsList);
        moreComment =  view.findViewById(R.id.moreComments);
    }


    /**
     * 附加评论到状态下边
     */
    private void addCommentsToList(){
        if (newComments == null)
            return ;
        List<Comment> comments = record.getComments();
        for (int i = 0 ; i < newComments.size() && comments.size() + i < commentsLineLimit; i++) {
            CommentDetailPlan cdp = adapter.addItem(newComments.get(i));
            cdp.setOnCommentClick(onCommentClickListener);
            adapter.notifyDataSetChanged();
        }
        if (commentsLineLimit != COMMENT_LINES_NO_LIMIT)
            commentsList.setAutoMeasure(true);
        comments.addAll(newComments);
        if (comments.size() >= commentsLineLimit){
            moreComment.setVisibility(View.VISIBLE);
        }else
            moreComment.setVisibility(View.GONE);

        if (comments.size() > 0) {
            commentsList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据点赞数和评论数显示组件内容
     */
    private void resetLikeAndComment(){
        if (record.getLike_cnt() >= 999)
            likeCount.setText("999");
        else
            likeCount.setText(record.getLike_cnt() + "");
        if (record.getComment_cnt() >= 999){
            commentCount.setText("999");
        }else
            commentCount.setText(record.getComment_cnt() +"");
        if (record.getIs_like() == 1)
            likeButton.setImageResource(R.drawable.record_red_heart);
        else
            likeButton.setImageResource(R.drawable.record_empty_heart);
    }


    /**
     * 从服务器拉取评论
     */
    private void pullComments(int pageno){
        final int page = pageno;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                ResultData<ArrayList<Comment>> ret =
                        CommentHelper.getCommentsByEmotionId(record.getEmotion_id(), page);
                boolean isok = true;
                if (ret == null || !ret.isOk())
                    isok = false;
                newComments = ret.getData();
                bundle.putBoolean("isOk", isok);
                bundle.putInt("target", 2);
                msg.setData(bundle);
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    /**
     * 将record的内容填充到界面上去
     */
    public void resetCompsContent(){
        if (record == null)
            return ;
        //profile.set  ...................
        profile.setImageResource(Living.profileID[Integer.parseInt(record.getAvatar())]);
        //其他属性设置
        userName.setText(record.getNickname());
        time.setText(record.getCreate_time());
        content.setText(record.getContent());
        emoDegree.setProgress(record.getStrong());
        moreComment.setVisibility(View.GONE);
        resetLikeAndComment();
        //设置表情
        switch(record.getLabel_id()){
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
        likeButton.setOnClickListener(upClickListener);
        commentButton.setOnClickListener(commentClickListener);
        likeCount.setOnClickListener(upClickListener);
        commentButton.setOnClickListener(commentClickListener);
        pullComments(0);//相服务器请求第一页评论
        moreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("emotion_id", record.getEmotion_id());
                intent.setClass(RecordDetailPlan.this.context, RecordDetailActivity.class);
                MainActivity.groundFragment.startActivity(intent);
            }
        });
        // resetCommentsToList();
    }

    public RecordDetailPlan(Context context, Record record, int commentsLineLimit){
        this.commentsLineLimit = commentsLineLimit;
        this.context = context;
        this.record = record;
        view = LayoutInflater.from(this.context).inflate(R.layout.record_item_detail_layout, null);

        findAllComp();
        commentsList.setOnScrollListener(this);
        resetCompsContent();
    }

    public RecordDetailPlan(Context context, int commentsLineLimit){
        this(context, null, commentsLineLimit);
    }
    //通过获得View可以让这个界面给其他组件复用
    public View getView(){
        return view;
    }
    public Record getData(){
        return record;
    }

    /**
     * 点赞操作如果操作成功，就会执行下面的函数，用于将星星变成实的
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data == null) return ;
            if (data.getBoolean("isOk")){
                int target = data.getInt("target");
                if (target == 0) {
                    //新的点赞
                    record.setIs_like(1);
                    record.setLike_cnt(record.getLike_cnt() + 1);
                    likeButton.setImageResource(R.drawable.record_red_heart);
                }else if (target == 1){
                    //新的评论 ,重新拉一下评论
                    adapter.clear();
                    record.getComments().clear();
                    curInitPageNum = 0;
                    record.setComment_cnt(record.getComment_cnt() + 1);
                    pullComments(curInitPageNum);
                }else if (target == 2) {//从服务器拉取评论的结果
                    addCommentsToList();
                    //初始化时候我们拉取的内容可能不止1页
                    if (curInitPageNum++ != initPageNum){
                        pullComments(curInitPageNum);
                    }
                }
                resetLikeAndComment();
            }else
                Toast.makeText(context, R.string.postlike_failed, 2000).show();
        }
    };

    private boolean doPostLike(){
        ResultData<Post> ret = CommentHelper.postLike(record.getEmotion_id());
        if (ret == null || !ret.isOk())
            return false;
        return true;
    }
    /**
     * 当点赞被点击的时候
     */
    private View.OnClickListener upClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            if (record.getIs_like() == 1) //不能点赞两次
                return ;
            new Thread() {
                public void run() {
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isOk", doPostLike());
                    bundle.putInt("target", 0);
                    msg.setData(bundle);
                    handler.sendMessage(msg);//发送message信息
                }
            }.start();
        }
    };

    private void startFloatEditor(String to,int toID){
        Intent intent = new Intent(context, FloatEditorActivity.class);
        // 设置要跳转的页面
        intent.putExtra("to", to);
        intent.putExtra("toID", toID);
        intent.putExtra("emotionID", record.getEmotion_id());
        // 开始Activity
        ((Activity)context).startActivityForResult(intent ,MainActivity.COMMENT_EDIT_REQUEST_CODE);
    }

    /**
     * 当评论按钮被点击的时候
     */
    private View.OnClickListener commentClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            startFloatEditor("", 0);
        }
    };

    /**
     * 当评论列表被点击的时候的回调函数
     */
    private CommentDetailPlan.OnCommentClickListener onCommentClickListener = new CommentDetailPlan.OnCommentClickListener() {
        @Override
        public void onCommentClick(Comment comment) {
            startFloatEditor(comment.getPoster_nickname(), comment.getPoster());
        }
    };

    //向服务器发送一个新的评论
    private boolean doPostComment(int emoID, Comment comment){
        ResultData<Post> ret =
                CommentHelper.postComment(emoID, comment.getRspto(), comment.getComment());
        if (ret == null || !ret.isOk())
            return false;
        return true;
    }
    /**
     * 往这个界面所管理的record中添加一个评论
     */
    public void addComment(final String content, String to, int toID){
        final int _toID = toID;
        final String _to = to;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                Comment comment = new Comment();
                comment.setComment(content);
                comment.setRspto(_toID);
                comment.setRspto_nickname(_to);
                comment.setPoster(record.getPoster());
                comment.setPoster_nickname(record.getNickname());
                bundle.putBoolean("isOk", doPostComment(record.getEmotion_id(), comment));
                bundle.putInt("target", 1);
                msg.setData(bundle);
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }
    public Record getRecord() {
        return record;
    }
    public void setRecord(Record record){
        this.record = record;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
            if (!isLoading) {
                //不处于加载状态的话对其进行加载
                isLoading = true;
                //loadmoreView.setVisibility(View.VISIBLE);
                //清除多余记录
                int giveUp = adapter.getCount() % LivingServerAgent.DATA_DATA_PER_PAGE;
                for (int i = 0; i < giveUp; i++)
                    if (adapter.getCount() != 0)
                        adapter.removeItem(adapter.getCount() - 1);
                pullComments(adapter.getCount() / LivingServerAgent.DATA_DATA_PER_PAGE );
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }
}
