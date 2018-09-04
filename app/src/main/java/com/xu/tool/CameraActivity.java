package com.xu.tool;

import android.Manifest;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.xu.tool.camera.CameraComponent;
import com.xu.tool.camera.interfaces.PreviewDataCallBack;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private Button btnTakePhoto ;
    private Button btnSwitchCameraMode ;
    private TextureView textureView ;

    private int cameraType = CameraComponent.CAMERA_NORMAL;

    private CameraComponent cameraComponent ;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_take_photo:
                    if(cameraComponent != null){
                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "photo.jpg";
                        cameraComponent.takePhoto(filePath);
                    }
                    break;
                case R.id.btn_switch_camera_mode:
                    if(cameraType == CameraComponent.CAMERA_NORMAL){
                        openNoPreviewCamera();
                    }else{
                        openPreviewCameraView();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //授权
        checkPermission();
        setContentView(R.layout.activity_camera);
        initView();
        openPreviewCameraView();
    }

    //初始化页面
    private void initView(){
        textureView = findViewById(R.id.tv_preview);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnSwitchCameraMode = findViewById(R.id.btn_switch_camera_mode);
        btnTakePhoto.setOnClickListener(onClickListener);
        btnSwitchCameraMode.setOnClickListener(onClickListener);
    }

    //打开有界面预览的相机
    private void openPreviewCameraView(){
        cameraType = CameraComponent.CAMERA_NORMAL ;
        if(cameraComponent == null){
            cameraComponent = CameraComponent.getInstance();
        }
        cameraComponent.setCameraType(cameraType,this.getApplicationContext());
        cameraComponent.setCameraId("0");
        cameraComponent.setSurfaceView(textureView);
        cameraComponent.startPreview();
    }

    private void openNoPreviewCamera(){
        cameraType = CameraComponent.CAMERA_PREVIEW ;
        if(cameraComponent != null){
            cameraComponent = CameraComponent.getInstance();
        }
        cameraComponent.setCameraType(cameraType,CameraActivity.this.getApplicationContext());
        cameraComponent.setCameraId("0");
        //设置相机预览数据回调
        cameraComponent.setPreviewDataCallBack(new PreviewDataCallBack() {
            @Override
            public void previewData(Image image) {
                //TODO 预览数据处理
                Log.i(TAG,"image data =" + image);
            }

            @Override
            public void errorCallBack(Exception e) {

            }

            @Override
            public void surfaceSizeChanged(Size size) {

            }
        });
        cameraComponent.startPreview();
    }

    //权限申请
    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    , 1);
        }
    }
}
