package com.tencent.living;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*
这个类专门负责显示某一条状态。
 */
public class RecordDetailPlan  {
    private CircleImageView profile;
    private TextView userName;
    private TextView time;
    private TextView content;
    private TextProgressBar emoDegree;
    private ImageButton upButton;
    private TextView upCount;
    private ImageButton commentButton;
    private TextView commentCount;
    private Button moreCommentBut;
    private View view;
    private Context context;
    private Record record;
    private LinearLayout linearLayout;

    //当前已经显示出来的评论
    private List<CommentDetailPlan> commentItems = new ArrayList<>();

    public static final int MAX_VISIABLE_COMMENT_ITEM = 3;

    private void findAllComp(){
        profile = (CircleImageView)view.findViewById(R.id.profileImage);
        userName = (TextView)view.findViewById(R.id.userName);
        time = (TextView)view.findViewById(R.id.time);
        content = (TextView)view.findViewById(R.id.content);
        emoDegree = (TextProgressBar)view.findViewById(R.id.emoBar);
        upButton = (ImageButton)view.findViewById(R.id.upButton);
        upCount = (TextView)view.findViewById(R.id.upCount);
        commentButton = (ImageButton)view.findViewById(R.id.commentButton);
        commentCount = (TextView)view.findViewById(R.id.commentCount);
        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
    }

    private void resetCompsContent(){
        //@TODO 这里需要设置头像
        //profile.set  ...................
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

        //我们最多只显示MAX_VISIABLE_COMMENT_ITEM条评论，如果评论多于三条，则会显示出查看更多评论的按钮
        List<Comment> comments = record.getComments();
        for (int i = 0 ; i < comments.size(); i++) {
            CommentDetailPlan cdp = new CommentDetailPlan(this.context, comments.get(i));
            linearLayout.addView(cdp.getView());
            commentItems.add(cdp);
        }
        //如果大于三条就需要为其添加一个查看更多按钮
        if (comments.size() >= MAX_VISIABLE_COMMENT_ITEM){
            moreCommentBut = new Button(this.context);
            moreCommentBut.setText(view.getContext().getString(R.string.ground_more_comment_but));
            moreCommentBut.setTextColor(Color.BLUE);
            moreCommentBut.getBackground().setAlpha(0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            moreCommentBut.setLayoutParams(lp);
            linearLayout.addView(moreCommentBut);

        }
    }

    public RecordDetailPlan(Context context, Record record){
        this.context = context;
        this.record = record;
        view = LayoutInflater.from(this.context).inflate(R.layout.record_item_detail_layout, null);
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

}
