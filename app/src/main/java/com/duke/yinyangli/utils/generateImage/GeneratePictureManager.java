package com.duke.yinyangli.utils.generateImage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by HomgWu on 2017/11/29.
 */

public class GeneratePictureManager {
    private static GeneratePictureManager sManager;
    private HandlerThread mHandlerThread;
    private Handler mWorkHandler;
    private Handler mMainHandler;


    private GeneratePictureManager() {
        mHandlerThread = new HandlerThread(GeneratePictureManager.class.getSimpleName());
        mHandlerThread.start();
        mWorkHandler = new Handler(mHandlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public static GeneratePictureManager getInstance() {
        if (sManager == null) {
            synchronized (GeneratePictureManager.class) {
                if (sManager == null) {
                    sManager = new GeneratePictureManager();
                }
            }
        }
        return sManager;
    }

    public void generate(GenerateModel generateModel, OnGenerateListener listener) {
        try {
            generateModel.startPrepare(listener);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                postResult(() -> {
                    listener.onGenerateFinished(e, null, null);
                });
            }
        }
    }

    private void postResult(Runnable runnable) {
        mMainHandler.post(runnable);
    }

    public void prepared(GenerateModel generateModel, OnGenerateListener listener) {
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                View view = generateModel.getView();
                Exception exception = null;
                Bitmap bitmap = null;
                String savePath = null;
                try {
                    bitmap = createBitmap(view);
                    savePath = generateModel.getSavePath();
                    if (!TextUtils.isEmpty(savePath)) {
                        if (!BitmapUtil.saveImage(bitmap, savePath)) {
                            exception = new RuntimeException("pic save failed");
                        }
                    }
                } catch (Exception e) {
                    exception = e;
                    e.printStackTrace();
                }
                if (listener != null) {
                    final Exception exception1 = exception;
                    final Bitmap bitmap1 = bitmap;
                    String finalSavePath = savePath;
                    mMainHandler.post(() -> {
                        listener.onGenerateFinished(exception1, finalSavePath, bitmap1);
                    });
                }
            }
        });
    }

    /**
     * 生成Bitmap
     */
    private Bitmap createBitmap(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().width, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, View.MeasureSpec.AT_MOST);
        view.measure(0, 0);
        int measureWidth = view.getMeasuredWidth();
        int measureHeight = view.getMeasuredHeight();
        view.layout(0, 0, measureWidth, measureHeight);
        int width = view.getWidth();
        int height = view.getHeight();
        if (width > 1080) {
            width = 1080;
        }
        if (height > 2440) {
            height = width * 16 / 9;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public interface OnGenerateListener {

        void onGenerateFinished(Throwable throwable, String savePath, Bitmap bitmap);

    }
}
