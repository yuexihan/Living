package com.tencent.living;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class RecordFragment extends Fragment {
    //用于存储拍到图片
    private File newImageFile;

    //用于心情识别的时候弹出正在检测
    private ProgressDialog emotionProgressDialog;

    //界面组件
    private RadioGroup radioGroup;
    private ImageButton camera_button;
    private SeekBar degreeBar;
    private TextView degreeText;
    private ImageButton selfPubButton;
    private ImageButton pubPubButton;

    //点击相机图片按钮时的回调函数
    private View.OnClickListener camera_but_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String[] items = { getString(R.string.camera_dialog_item1), getString(R.string.camera_dialog_item2) };
            final AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(getActivity());
            listDialog.setTitle(getString(R.string.camera_dialog_title));
            listDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which) {
                        case 0:
                            startCamera();
                            break;
                        case 1:
                            startGallery();
                            break;
                    }
                }
            });
            listDialog.show();
        }
    };
    //心情时候结束后的函数
    private FaceDetect.FaceDetectDoneAction faceDetectDoneAction = new FaceDetect.FaceDetectDoneAction(){
       public void onDetectDone(Face face[]){
            //这里要根据检测结果做一些操作
           String result = "";
           int[] emo = new int[2];
           if (face == null || face.length == 0){
               emo[0] = -1;
           }else{
               emo = getRealEmotionString(face[0].faceAttributes.emotion);
               RadioButton targetEmoButton = (RadioButton)radioGroup.getChildAt(emo[0]);
               //帮用户选择心情
               targetEmoButton.setChecked(true);
               degreeBar.setProgress(emo[1]);
               degreeText.setText(getString(R.string.emotion_value) + emo[1]);
           }

           //显示一个对话框提醒用户检测结果
           final EmotionDetectResultDialog resultDialog =
                   new EmotionDetectResultDialog(RecordFragment.this.getActivity());
           resultDialog.setEmotionVaule(emo[0], emo[1]);
           resultDialog.show();
       }
    };

    //心情强度条被滑动时候的回调函数
    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            degreeText.setText(getString(R.string.emotion_value) + progress);
        }
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    //点击发布给自己按钮时被调用
    private View.OnClickListener selfPubListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //@TODO 这里要写发布给自己的逻辑
            Toast.makeText(getContext(),"发布给自己", 3000).show();
        }
    };

    //点击发布到广场按钮时被调用
    private View.OnClickListener pubPubListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //@TODO 这里要写发布到广场的逻辑
            Toast.makeText(getContext(),"发布到广场", 3000).show();
        }
    };
    private void startCamera(){
        File dir = new File(Environment.getExternalStorageDirectory(),"pictures");
        if(dir.exists()){
            dir.mkdirs();
        }
        newImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
        if(!newImageFile.exists()){
            try {
                newImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return ;
            }
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newImageFile));
        it.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(it, MainActivity.CAMERA_REQUEST_CODE);
    }
    private void startGallery(){
        Intent choosePicture = new Intent(Intent.ACTION_PICK);
        choosePicture.setType("image/*");
        startActivityForResult(choosePicture, MainActivity.GALLERY_REQUEST_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.record_frag_layout, container, false);
        camera_button = (ImageButton)view.findViewById(R.id.camera_but);
        radioGroup = (RadioGroup)view.findViewById(R.id.emoGroup);
        degreeBar = (SeekBar)view.findViewById(R.id.degreeBar);
        degreeText = (TextView)view.findViewById(R.id.degreeText);
        selfPubButton = (ImageButton)view.findViewById(R.id.self_pub);
        pubPubButton = (ImageButton)view.findViewById(R.id.pub_pub);

        camera_button.setOnClickListener(camera_but_lis);
        emotionProgressDialog = new ProgressDialog(this.getActivity());
        degreeBar.setOnSeekBarChangeListener(seekBarListener);
        degreeText.setText(getString(R.string.emotion_value) + degreeBar.getProgress());
        selfPubButton.setOnClickListener(selfPubListener);
        pubPubButton.setOnClickListener(pubPubListener);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return ;
        switch(requestCode){
            //如果是摄像机拍的照片，是不会将uri放在data里的
            //为了进行统一处理。我们这里自己放进去
            case MainActivity.CAMERA_REQUEST_CODE:
                data.setData(Uri.fromFile(newImageFile));
                /* pass ↓ */
            case MainActivity.GALLERY_REQUEST_CODE:
                onGetPictureReturn(data);
                break;
        }
    }

    /*
    无论是从相册拍摄的图片还是从相册原则的图片都是放这个函数处理
     */
    private void onGetPictureReturn(Intent data){
        if (data.getData() == null && data.getExtras() == null)
            return ;
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Bitmap mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                data.getData(), getActivity().getContentResolver());
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

        if (mBitmap != null)
            camera_button.setImageBitmap(mBitmap);

        // Start a background task to detect faces in the image.
        FaceDetect faceDetect = new FaceDetect();
        faceDetect.setDetectDoneAction(faceDetectDoneAction);
        faceDetect.setProgressDialog(emotionProgressDialog);
        faceDetect.execute(new ByteArrayInputStream(output.toByteArray()));
    }


    //从Emotion的各种表情可能性中获得最佳可能性的表情
    //返回一个int表示表情，返回一个int表示数值
    //表情类型0 = 开心，1 = 愤怒， 2 = 伤心，3 = 平静
    public static int[] getRealEmotionString(Emotion emotion)
    {
        int emotionType = 0;
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue)
        {
            emotionValue = emotion.anger;
            emotionType = 1;
        }
        if (emotion.contempt > emotionValue)
        {
            emotionValue = emotion.contempt;
            emotionType = 1;
        }
        if (emotion.disgust > emotionValue)
        {
            emotionValue = emotion.disgust;
            emotionType = 1;
        }
        if (emotion.fear > emotionValue)
        {
            emotionValue = emotion.fear;
            emotionType = 2;
        }
        if (emotion.happiness > emotionValue)
        {
            emotionValue = emotion.happiness;
            emotionType = 0;
        }
        if (emotion.neutral > emotionValue)
        {
            emotionValue = emotion.neutral;
            emotionType = 3;
        }
        if (emotion.sadness > emotionValue)
        {
            emotionValue = emotion.sadness;
            emotionType = 2;
        }
        if (emotion.surprise > emotionValue)
        {
            emotionValue = emotion.surprise;
            emotionType = 0;
        }
        return new int[]{emotionType, (int)(emotionValue * 100)};
    }
}
