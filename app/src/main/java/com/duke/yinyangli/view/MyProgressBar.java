package com.duke.yinyangli.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.appcompat.widget.AppCompatImageView;

import com.duke.yinyangli.R;

public class MyProgressBar extends AppCompatImageView {
    private RotateAnimation mRotateAnimation;
    private Paint paint = null;
    private Drawable defaultDrawable = null;
    private int width = 0;
    private int height = 0;

    public MyProgressBar(Context context) {
        super(context);
        init();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        this.setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        defaultDrawable = this.getBackground();
        if (defaultDrawable == null) {
            defaultDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
        }
        if (width == 0) {
            width = this.getWidth();
        }
        if (height == 0) {
            height = this.getHeight();
        }
        defaultDrawable.setBounds(0, 0, width, height);
        defaultDrawable.draw(canvas);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
        mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setDuration(2000);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setFillAfter(true);
        startAnimation(mRotateAnimation);
    }

    public void stop() {
        clearAnimation();
    }

    public void hide() {
        clearAnimation();
        this.setVisibility(View.GONE);
    }
}