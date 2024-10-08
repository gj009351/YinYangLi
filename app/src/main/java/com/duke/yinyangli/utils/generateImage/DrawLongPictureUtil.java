package com.duke.yinyangli.utils.generateImage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.duke.yinyangli.R;
import com.duke.yinyangli.utils.ImageUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author 工藤
 * @email 18883840501@163.com
 * cc.shinichi.drawlongpicturedemo.util
 * create at 2018/8/27  17:47
 * description:
 */
public class DrawLongPictureUtil extends LinearLayout {

    private final String TAG = "DrawLongPictureUtil";
    private Context context;
    private SharedPreferences sp;
    private Listener listener;

    private View rootView;
    private LinearLayout llTopView;
    private LinearLayout llContent;
    private LinearLayout llBottomView;

    private TextView tvUserName;
    private TextView tvUserDes;
    private TextView tvContent;
    private ImageView imgQrCode;

    // 长图的宽度，默认为屏幕宽度
    private int longPictureWidth;
    // 最终压缩后的长图宽度
    private int finalCompressLongPictureWidth;
    // 长图两边的间距
    private int picMargin;

    // 被认定为长图的长宽比
    private int maxSingleImageRatio = 3;
    private int widthTop = 0;
    private int heightTop = 0;

    private int widthContent = 0;
    private int heightContent = 0;

    private int widthBottom = 0;
    private int heightBottom = 0;

    public DrawLongPictureUtil(Context context) {
        super(context);
        init(context);
    }

    public DrawLongPictureUtil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawLongPictureUtil(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void removeListener() {
        this.listener = null;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;
        this.sp = context.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        longPictureWidth = ScreenUtils.getScreenWidth();
        picMargin = 40;
        rootView = LayoutInflater.from(context).inflate(R.layout.share_picture, this, false);
        initView();
    }

    private void initView() {
        llTopView = rootView.findViewById(R.id.llTopView);
        llContent = rootView.findViewById(R.id.llContent);
        llBottomView = rootView.findViewById(R.id.llBottomView);

        tvUserName = rootView.findViewById(R.id.tvUserName);
        tvUserDes = rootView.findViewById(R.id.tvUserDes);
        tvContent = rootView.findViewById(R.id.tvUserDes);
        imgQrCode = rootView.findViewById(R.id.imgQrCode);

        layoutView(llTopView);
        layoutView(llContent);
        layoutView(llBottomView);

        widthTop = llTopView.getMeasuredWidth();
        heightTop = llTopView.getMeasuredHeight();

        widthContent = llContent.getMeasuredWidth();
        // 文字由于高度可变，所以这里不需要测量高度，后面会手动测量

        widthBottom = llBottomView.getMeasuredWidth();
        heightBottom = llBottomView.getMeasuredHeight();

        Log.d(TAG, "drawLongPicture layout top view = " + widthTop + " × " + heightTop);
        Log.d(TAG, "drawLongPicture layout llContent view = " + widthContent + " × " + heightContent);
        Log.d(TAG, "drawLongPicture layout bottom view = " + widthBottom + " × " + heightBottom);
    }

    /**
     * 手动测量view宽高
     */
    private void layoutView(View v) {
        int width = ScreenUtils.getScreenWidth();
        int height = ScreenUtils.getScreenHeight();

        v.layout(0, 0, width, height);
        int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int measuredHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public void startDraw() {
        // 需要先下载全部需要用到的图片（用户头像、图片等），下载完成后再进行长图的绘制操作
        draw();
    }

    private Bitmap getLinearLayoutBitmap(LinearLayout linearLayout, int w, int h) {
        Bitmap originBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(originBitmap);
        linearLayout.draw(canvas);
        return ImageUtils.resizeImage(originBitmap, longPictureWidth, h);
    }

    private Bitmap getSingleBitmap(String path) {
        int[] wh = ImageUtils.getWidthHeight(path);
        final int w = wh[0];
        final int h = wh[1];
        wh[0] = (longPictureWidth - (picMargin) * 2);
        wh[1] = (wh[0]) * h / w;
        Bitmap bitmap = null;
        try {
            // 长图，只截取中间一部分
            float imgRatio = h / w;
            if (imgRatio > maxSingleImageRatio) {
                wh[1] = wh[0] * maxSingleImageRatio;
                Log.d(TAG, "getSingleBitmap w h > maxSingleImageRatio = " + Arrays.toString(wh));
            }
            bitmap = Glide.with(context).asBitmap().load(path).centerCrop().into(wh[0], wh[1]).get();
            Log.d(TAG, "getSingleBitmap glide bitmap w h = " + bitmap.getWidth() + " , " + bitmap.getHeight());
            return bitmap;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            Log.d(TAG, "getRoundedCornerBitmap w h = " + output.getWidth() + " × " + output.getHeight());
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private void draw() {
        // 先绘制中间部分的文字，计算出文字所需的高度
//        String content = shareInfo.getContent();
        String content = "";
        TextPaint contentPaint = tvContent.getPaint();
        //contentPaint.setColor();
        //contentPaint.setTextSize();
        StaticLayout staticLayout =
            new StaticLayout(content, contentPaint, (ScreenUtils.getScreenWidth() - picMargin * 2),
                Layout.Alignment.ALIGN_NORMAL, 1.2F, 0, false);
        heightContent = staticLayout.getHeight();

        // 计算出最终生成的长图的高度 = 上、中、图片总高度、下等个个部分加起来
        int allBitmapHeight = heightTop + heightContent + heightBottom;

        // 创建空白画布
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmapAll;
        try {
            bitmapAll = Bitmap.createBitmap(longPictureWidth, allBitmapHeight, config);
        } catch (Exception e) {
            e.printStackTrace();
            config = Bitmap.Config.RGB_565;
            bitmapAll = Bitmap.createBitmap(longPictureWidth, allBitmapHeight, config);
        }
        Canvas canvas = new Canvas(bitmapAll);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        // 绘制top view
        canvas.drawBitmap(getLinearLayoutBitmap(llTopView, widthTop, heightTop), 0, 0, paint);
        canvas.save();

        // 绘制content view
        canvas.translate(SizeUtils.dp2px(20), heightTop);
        staticLayout.draw(canvas);

        // 绘制图片view
        canvas.restore();

        // 绘制bottom view
        canvas.drawBitmap(getLinearLayoutBitmap(llBottomView, widthBottom, heightBottom), 0,
            (heightTop + heightContent), paint);

        // 生成最终的文件，并压缩大小，这里使用的是：implementation 'com.github.nanchen2251:CompressHelper:1.0.5'
        try {
            String path = ImageUtils.saveBitmapBackPath(bitmapAll);
            float imageRatio = ImageUtils.getImageRatio(path);
            if (imageRatio >= 10) {
                finalCompressLongPictureWidth = 750;
            } else if (imageRatio >= 5 && imageRatio < 10) {
                finalCompressLongPictureWidth = 900;
            } else {
                finalCompressLongPictureWidth = longPictureWidth;
            }
            String result = null;
            // 由于长图一般比较大，所以压缩时应注意OOM的问题，这里并不处理OOM问题，请自行解决。
            /*try {
                result = new CompressHelper.Builder(context).setMaxWidth(finalCompressLongPictureWidth)
                    .setMaxHeight(Integer.MAX_VALUE) // 默认最大高度为960
                    .setQuality(80)    // 默认压缩质量为80
                    .setFileName("长图_" + System.currentTimeMillis()) // 设置你需要修改的文件名
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                            + "/长图分享/")
                    .build()
                    .compressToFile(new File(path))
                    .getAbsolutePath();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();

                finalCompressLongPictureWidth = finalCompressLongPictureWidth / 2;
                result = new CompressHelper.Builder(context).setMaxWidth(finalCompressLongPictureWidth)
                    .setMaxHeight(Integer.MAX_VALUE) // 默认最大高度为960
                    .setQuality(50)    // 默认压缩质量为80
                    .setFileName("长图_" + System.currentTimeMillis()) // 设置你需要修改的文件名
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                            + "/长图分享/")
                    .build()
                    .compressToFile(new File(path))
                    .getAbsolutePath();
            }*/
            Log.d(TAG, "最终生成的长图路径为：" + result);
            if (listener != null) {
                listener.onSuccess(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail();
            }
        }
    }

    public interface Listener {

        /**
         * 生成长图成功的回调
         *
         * @param path 长图路径
         */
        void onSuccess(String path);

        /**
         * 生成长图失败的回调
         */
        void onFail();
    }
}