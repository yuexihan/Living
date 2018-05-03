package com.tencent.living;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class EmotionDetectResultDialog extends Dialog {
    private Context context;
    private TextView emoDegreeText;
    private SeekBar emoDegree;
    private ImageView emoImage;
    private TextView emoName;
    private int emoType = -1;
    private int emoValue = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context,R.layout.emotion_detect_result_dialog,null);
        setContentView(view);
        emoDegreeText = (TextView)view.findViewById(R.id.emoDegreeText);
        emoDegree = (SeekBar)view.findViewById(R.id.emoDegree);
        emoImage = (ImageView)view.findViewById(R.id.emoImage);
        emoName = (TextView)view.findViewById(R.id.emoName);
        initEmotionToView();
    }

    public EmotionDetectResultDialog(Context context) {
        super(context);
        this.context = context;
    }

    public EmotionDetectResultDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected EmotionDetectResultDialog(Context context, boolean cancelable
            , OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void setEmotionVaule(int emoType, int value){
        this.emoType = emoType;
        this.emoValue = value;
    }
    private void initEmotionToView(){
        switch(emoType){
            case -1:
                emoImage.setImageResource(R.drawable.all);
                emoName.setText(R.string.emotion_detect_fail);
                return ;
            case FaceDetect.EMOTION_HAPPY:
                emoImage.setImageResource(R.drawable.happy);
                emoName.setText(R.string.record_rbut_happy);
                break;
            case FaceDetect.EMOTION_ANGER:
                emoImage.setImageResource(R.drawable.anger);
                emoName.setText(R.string.rbut_anger);
                break;
            case FaceDetect.EMOTION_SAD:
                emoImage.setImageResource(R.drawable.sad);
                emoName.setText(R.string.rbut_sad);
                break;
            case FaceDetect.EMOTION_CALM:
                emoImage.setImageResource(R.drawable.calm);
                emoName.setText(R.string.rbut_calm);
                break;
        }
        emoDegreeText.setText(context.getString(R.string.emotion_value) + emoValue);
    }
}
