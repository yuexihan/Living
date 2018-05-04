package com.tencent.living;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.InputStream;

public class FaceDetect extends AsyncTask<InputStream, String, Face[]> {

    public static final int EMOTION_HAPPY = 0;
    public static final int EMOTION_ANGER = 1;
    public static final int EMOTION_SAD = 2;
    public static final int EMOTION_CALM = 3;

    // Progress dialog popped up when communicating with server.
    private ProgressDialog mProgressDialog;
    // 当检测结束之后会调用action中的onDetectDone
    private FaceDetectDoneAction action;

    //设置检测完成回调函数
    public void setDetectDoneAction(FaceDetectDoneAction action){
        this.action = action;
    }

    //设置用于检测时显示的等待对话框
    public void setProgressDialog(ProgressDialog mProgressDialog){
        this.mProgressDialog = mProgressDialog;
    }

    public FaceDetect(){
    }
    @Override
    protected Face[] doInBackground(InputStream... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = Living.getFaceServiceClient();
            try {
                publishProgress(mProgressDialog.getContext().getString(R.string.emotion_detecing));
                // Start detection.
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        new FaceServiceClient.FaceAttributeType[] {
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender,
                                FaceServiceClient.FaceAttributeType.Smile,
                                FaceServiceClient.FaceAttributeType.Glasses,
                                FaceServiceClient.FaceAttributeType.FacialHair,
                                FaceServiceClient.FaceAttributeType.Emotion,
                                FaceServiceClient.FaceAttributeType.HeadPose,
                                FaceServiceClient.FaceAttributeType.Accessories,
                                FaceServiceClient.FaceAttributeType.Blur,
                                FaceServiceClient.FaceAttributeType.Exposure,
                                FaceServiceClient.FaceAttributeType.Hair,
                                FaceServiceClient.FaceAttributeType.Makeup,
                                FaceServiceClient.FaceAttributeType.Noise,
                                FaceServiceClient.FaceAttributeType.Occlusion
                        });
            } catch (Exception e) {
                publishProgress(e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            if (mProgressDialog != null)
                mProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            if (mProgressDialog != null)
                mProgressDialog.setMessage(progress[0]);
        }
        @Override
        protected void onPostExecute(Face[] result) {
            // Show the result on screen when detection is done.
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            action.onDetectDone(result);
        }
    public interface FaceDetectDoneAction {
        void onDetectDone(Face face[]);
    }
}
