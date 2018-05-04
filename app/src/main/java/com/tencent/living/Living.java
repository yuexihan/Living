package com.tencent.living;

import android.app.Application;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.tencent.living.models.User;

public class Living  extends Application{
    public static User user;
    public static final int profileID[] = {
            R.drawable.profile0,
            R.drawable.profile1,
            R.drawable.profile2,
            R.drawable.profile3,
            R.drawable.profile4,
            R.drawable.profile5,
            R.drawable.profile6,
            R.drawable.profile7,
            R.drawable.profile8
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sFaceServiceClient = new FaceServiceRestClient(getString(R.string.emotion_endpoint), getString(R.string.emotion_key));
    }


    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;
}
