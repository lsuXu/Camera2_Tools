package com.xu.tool.camera.impl;

import android.content.Context;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;

import com.xu.tool.camera.interfaces.CameraPreviewData;
import com.xu.tool.camera.interfaces.PreviewDataCallBack;
import com.xu.tool.camera.utils.CameraUtils;

import java.util.Arrays;
import java.util.List;


/**
 * Created by 12852 on 2018/8/29.
 */

public class CameraPreviewDataImpl extends CameraTemplateImpl implements CameraPreviewData {

    private ImageReader imageReader ;

    private PreviewDataCallBack previewDataCallBack ;

    public CameraPreviewDataImpl(Context context) {
        super(context);
        format = ImageFormat.YUV_420_888;
    }

    @Override
    public synchronized void startPreview() {
        Size fitSize = CameraUtils.getFitPreviewSize(context,cameraId, format,maxSize);
        imageReader = ImageReader.newInstance(fitSize.getWidth(),fitSize.getHeight(),format,2);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                if(image != null && previewDataCallBack != null){
                    previewDataCallBack.previewData(image);
                }
                image.close();
            }
        },null);
        super.startPreview();
    }

    @Override
    public synchronized void stopPreview() {
        super.stopPreview();
        if(imageReader != null){
            imageReader.close();
            imageReader = null ;
        }
    }


    //预设将要输出的所有界面
    @Override
    public List<Surface> getTotalSurfaceList() {
        return Arrays.asList(imageReader.getSurface());
    }

    //当前需要输出数据的界面
    @Override
    public List<Surface> getSurfaceList() {
        return Arrays.asList(imageReader.getSurface());
    }

    @Override
    public void setPreviewDataCallBack(PreviewDataCallBack previewCallBack) {
        this.previewDataCallBack = previewCallBack ;
    }
}
