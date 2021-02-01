package com.duke.yinyangli.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    private int mDegree;
    private int mDuration;

    public MyProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.progressBar);
            defaultDrawable = ta.getDrawable(R.styleable.progressBar_bar_src);
            mDegree = ta.getInteger(R.styleable.progressBar_bar_degree, 360);
            mDuration = ta.getInteger(R.styleable.progressBar_bar_duration, 2000);
            ta.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        this.setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if (defaultDrawable == null) {
            defaultDrawable = this.getBackground();
            if (defaultDrawable == null) {
                defaultDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
            }
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
        mRotateAnimation = new RotateAnimation(0, mDegree * 1000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setDuration(mDuration * 1000);
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