package com.tencent.living;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.File;
import java.io.IOException;

public class RecordFragment extends Fragment {
    private ImageButton camera_button;
    private File newImageFile;//用于存储拍到图片
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
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
        startActivityForResult(it, CAMERA_REQUEST_CODE);
    }
    private void startGallery(){
        Intent choosePicture = new Intent(Intent.ACTION_PICK);
        choosePicture.setType("image/*");
        startActivityForResult(choosePicture, GALLERY_REQUEST_CODE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.record_frag_layout, container, false);
        camera_button = (ImageButton)view.findViewById(R.id.camera_but);
        camera_button.setOnClickListener(camera_but_lis);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return ;
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                //如果是摄像机拍的照片，是不会将uri放在data里的
                //为了进行统一处理。我们这里自己放进去
                data.setData(Uri.fromFile(newImageFile));
                /* pass ↓ */
            case GALLERY_REQUEST_CODE:
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
        Uri uri = data.getData();
        camera_button.setImageURI(uri);
        /**
         * @TODO 这里进行人脸识别处理
         */
    }
}
