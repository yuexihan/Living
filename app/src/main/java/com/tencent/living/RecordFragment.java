package com.tencent.living;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.tencent.living.dataHelper.RecordHelper;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.dataHelper.ImageHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class RecordFragment extends Fragment {
    public static final int PUB_TO_SELF = 1;
    public static final int PUB_TO_PUB = 2;

    //用于存储拍到图片
    private File newImageFile;
    private Uri uri;
    //用于心情识别的时候弹出正在检测
    private ProgressDialog emotionProgressDialog;

    //界面组件
    private RadioGroup radioGroup;
    private ImageButton camera_button;
    private SeekBar degreeBar;
    private TextView degreeText;
    private ImageButton selfPubButton;
    private ImageButton pubPubButton;
    private EditText contentText;
    private ProgressBar pb;
    private ScrollView sv;

    //点击相机图片按钮时的回调函数
    private View.OnClickListener camera_but_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String[] items = {getString(R.string.camera_dialog_item1), getString(R.string.camera_dialog_item2)};
            final AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(getActivity());
            listDialog.setTitle(getString(R.string.camera_dialog_title));
            listDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
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
    private FaceDetect.FaceDetectDoneAction faceDetectDoneAction = new FaceDetect.FaceDetectDoneAction() {
        public void onDetectDone(Face face[]) {
            //这里要根据检测结果做一些操作
            String result = "";
            int[] emo = new int[2];
            if (face == null || face.length == 0) {
                emo[0] = -1;
            } else {
                emo = getRealEmotionString(face[0].faceAttributes.emotion);
                RadioButton targetEmoButton = (RadioButton) radioGroup.getChildAt(emo[0]);
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

    private int getCurrentCheckEmotion() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rbut_happy:
                return 0;
            case R.id.rbut_anger:
                return 1;
            case R.id.rbut_sad:
                return 2;
            case R.id.rbut_calm:
                return 3;
        }
        return 0;
    }

    /**
     * 数据线程处理完数据之后，会发送消息给handler来做界面处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isOk = data.getBoolean("isOk");
            if (isOk) {
                //跳转到我的
                Toast.makeText(RecordFragment.this.getActivity(), "发布成功", 2000).show();
            } else
                Toast.makeText(RecordFragment.this.getActivity(), R.string.pub_record_fail, 2000).show();

            pb.setVisibility(View.GONE);
            selfPubButton.setVisibility(View.VISIBLE);
            pubPubButton.setVisibility(View.VISIBLE);
        }
    };

    private boolean doPubRecord(int visiable) {
        String content = contentText.getText().toString();
        int emotion = getCurrentCheckEmotion();
        int emo_val = degreeBar.getProgress();
        ResultData<Post> ret = RecordHelper.postRecord(content, emotion, emo_val, visiable);
        if (ret == null || !ret.isOk())
            return false;
        return true;
    }

    private void tryToPubRecord(int visiable) {
        if (contentText.getText().toString().length() == 0 && visiable == PUB_TO_PUB) {
            Toast.makeText(RecordFragment.this.getActivity(), R.string.pub_record_empty, Toast.LENGTH_LONG).show();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        selfPubButton.setVisibility(View.GONE);
        pubPubButton.setVisibility(View.GONE);
        final int isPrivate = visiable;
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                if (doPubRecord(isPrivate))
                    bundle.putBoolean("isOk", true);
                else
                    bundle.putBoolean("isOk", false);
                msg.setData(bundle);//bundle传值，耗时，效率低
                handler.sendMessage(msg);//发送message信息
            }
        }.start();
    }

    //点击发布给自己按钮时被调用
    private View.OnClickListener selfPubListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tryToPubRecord(PUB_TO_SELF);
        }
    };

    //点击发布到广场按钮时被调用
    private View.OnClickListener pubPubListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tryToPubRecord(PUB_TO_PUB);
        }
    };

    private void startCamera() {
        File dir = new File(Environment.getExternalStorageDirectory(), "pictures");
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }
        try {
            if (dir.exists()) {
                dir.mkdirs();
            }
            newImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!newImageFile.exists())
                newImageFile.createNewFile();
            // 默认前置
            intent.putExtra("camerasensortype", 2);
            //intent.putExtra("autofocus", true);

            if (android.os.Build.VERSION.SDK_INT < 24) {
                // 从文件中创建uri
                uri = Uri.fromFile(newImageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                // 兼容Android 7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, newImageFile.getAbsolutePath());
                uri = getActivity().getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            startActivityForResult(intent, MainActivity.CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGallery() {
        Intent choosePicture = new Intent(Intent.ACTION_PICK);
        choosePicture.setType("image/*");
        startActivityForResult(choosePicture, MainActivity.GALLERY_REQUEST_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_frag_layout, container, false);
        camera_button = view.findViewById(R.id.camera_but);
        radioGroup = view.findViewById(R.id.emoGroup);
        degreeBar = view.findViewById(R.id.degreeBar);
        degreeText = view.findViewById(R.id.degreeText);
        selfPubButton = view.findViewById(R.id.self_pub);
        pubPubButton = view.findViewById(R.id.pub_pub);
        contentText = view.findViewById(R.id.pub_input);
        pb = view.findViewById(R.id.pubProgress);
        sv = view.findViewById(R.id.scrollView);

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
            return;
        switch (requestCode) {
            //如果是摄像机拍的照片，是不会将uri放在data里的
            //为了进行统一处理。我们这里自己放进去
            case MainActivity.CAMERA_REQUEST_CODE:
                /* pass ↓ */
            case MainActivity.GALLERY_REQUEST_CODE:
                if (data != null && data.getData() != null)
                    uri = data.getData();
                onGetPictureReturn(uri);
                break;
        }
    }

    /*
    无论是从相册拍摄的图片还是从相册原则的图片都是放这个函数处理
     */
    private void onGetPictureReturn(Uri uri) {
        if (uri == null)
            return;
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Bitmap mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                uri, getActivity().getContentResolver());
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
    public static int[] getRealEmotionString(Emotion emotion) {
        int emotionType = 0;
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue) {
            emotionValue = emotion.anger;
            emotionType = 1;
        }
        if (emotion.contempt > emotionValue) {
            emotionValue = emotion.contempt;
            emotionType = 1;
        }
        if (emotion.disgust > emotionValue) {
            emotionValue = emotion.disgust;
            emotionType = 1;
        }
        if (emotion.fear > emotionValue) {
            emotionValue = emotion.fear;
            emotionType = 2;
        }
        if (emotion.happiness > emotionValue) {
            emotionValue = emotion.happiness;
            emotionType = 0;
        }
        if (emotion.neutral > emotionValue) {
            emotionValue = emotion.neutral;
            emotionType = 3;
        }
        if (emotion.sadness > emotionValue) {
            emotionValue = emotion.sadness;
            emotionType = 2;
        }
        if (emotion.surprise > emotionValue) {
            emotionValue = emotion.surprise;
            emotionType = 0;
        }
        return new int[]{emotionType, (int) (emotionValue * 100)};
    }
}
