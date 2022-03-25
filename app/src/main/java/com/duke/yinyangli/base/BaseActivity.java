package com.duke.yinyangli.base;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.duke.yinyangli.BuildConfig;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.activity.ChooseActivity;
import com.duke.yinyangli.activity.MainActivity;
import com.duke.yinyangli.bean.TimeCount;
import com.duke.yinyangli.bean.VersionResponse;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.utils.AdmobUtils;
import com.duke.yinyangli.utils.AppUtils;
import com.duke.yinyangli.utils.ChooseCostUtils;
import com.duke.yinyangli.utils.ImageUtils;
import com.duke.yinyangli.utils.JsonUtils;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.SharePicModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gyf.immersionbar.ImmersionBar;
import com.haibin.calendarview.library.Article;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends AppCompatActivity implements SharePicModel.OnSharePicListener {

    private Unbinder unbinder;
    public TextView title;
    public ImageView left;
    public ImageView right;
    public MyHandler mHandler;
    private SimpleDialog mDialog;
    private Dialog progressDialog;
    private Solar mSolar;
    private AdView mBannerAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(android.R.color.white).statusBarDarkFont(true, 0).init();

        EventBus.getDefault().register(this);
        setContentView(getLayoutId());
        if (requestButterKnife()) {
            unbinder = ButterKnife.bind(this);
        }
        mSolar = new Solar();
        initView();
        initData();
    }

    public void initData() {
    }

    public void initView() {
        title = findViewById(R.id.title);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        if (left != null) {
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        mBannerAdView = findViewById(R.id.adView);
        if (mBannerAdView != null && AdmobUtils.isInit()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mBannerAdView.loadAd(adRequest);
        }
    }

    public abstract int getLayoutId();

    public boolean requestButterKnife() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && requestButterKnife()) {
            unbinder.unbind();
            unbinder = null;
        }
        EventBus.getDefault().unregister(this);
    }


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        dismissProgressDialog();
        progressDialog = DialogUtils.progress(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(BaseEvent event) {
        Bundle bundle = event.getBundle();
        LogUtils.d("onReceive message event:" + event.getCode() + ", " + event.getBundle());
        if (event.getCode() == Event.CODE_UPDATE_VERSION) {
            if (bundle != null) {
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                if (!TextUtils.isEmpty(message)) {
                    VersionResponse response = JsonUtils.fromJson(message, VersionResponse.class);
                    LogUtils.d("receive update message:" + message + ", response:" + response);
                    if (response.getVersionCode() > BuildConfig.VERSION_CODE) {
                        showUpdateDialog(response);
                    }
                }
            }
        }
    }

    private void showUpdateDialog(VersionResponse response) {
        LogUtils.d("show update dialog");
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = SimpleDialog.init(this, response.getUpdateTitle(), response.getUpdateMessage()
                , new SimpleDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        AppUtils.openBrowser(BaseActivity.this, response.getDownloadUrl());
                    }
                })
                .showCancel(!"1".equals(response.getForceUpdate()))
                .showDialog();
    }

    public boolean isSafe() {
        return !isDestroyed() && !isFinishing();
    }

    /**
     * 测算后调用，记录每天次数+1
     * @param article
     */
    public void addTestCount(Article article) {
        ChooseCostUtils.getInstance().addCount();
    }

    public void startShare() {
//        showProgressDialog();
        SharePicModel sharePicModel = new SharePicModel((ViewGroup) getWindow().getDecorView());
        sharePicModel.setSharePicListener(this);
        GeneratePictureManager.getInstance().generate(sharePicModel, (throwable, filePath, bitmap) -> {
            if (throwable != null || bitmap == null) {
            } else {
                share(filePath);
            }
        });
    }

    private void share(String filePath) {
        File shareFile = new File(filePath);
        if (!shareFile.exists()) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(MyApplication.getInstance().getApplicationContext()
                    , getPackageName()+".fileprovider", shareFile);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        }
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, "分享图片");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser);
        }
    }

    @Override
    public View getShareContentView() {
        return null;
    }

    protected static final class MyHandler extends Handler {

        private WeakReference<BaseActivity> weakReference;

        public MyHandler(BaseActivity activity) {
            this.weakReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().onHandleMessage(msg);
            }
        }

    }

    public void onHandleMessage(Message msg) {

    }

}
