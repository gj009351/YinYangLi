package com.duke.yinyangli.base;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.bean.VersionResponse;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.utils.AppUtils;
import com.duke.yinyangli.utils.FileUtils;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.generateImage.GenerateModel;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.OnSharePicListener;
import com.duke.yinyangli.utils.generateImage.ShareLunarModel;
import com.duke.yinyangli.utils.generateImage.SharePicModel;
import com.gyf.immersionbar.ImmersionBar;
import com.haibin.calendarview.library.Article;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public abstract class BaseActivity extends AppCompatActivity implements OnSharePicListener
        , View.OnClickListener
        , GeneratePictureManager.OnGenerateListener {

    private Unbinder unbinder;
    public TextView title;
    public ImageView left;
    public MyHandler mHandler;
    private SimpleDialog mDialog;
    private Dialog progressDialog;
    private Solar mSolar;

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
        if (left != null) {
            left.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left) {
            onBackPressed();
        }
    }


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = DialogUtils.progress(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(BaseEvent event) {
        Bundle bundle = event.getBundle();
        LogUtils.d("onReceive message event:" + event.getCode() + ", " + event.getBundle());
        if (event.getCode() == Event.CODE_UPDATE_VERSION) {
            if (bundle != null) {
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

    public void startShare() {
        showProgressDialog();
        GeneratePictureManager.getInstance().generate(getShareLunarModel(), this);
    }

    @Override
    public void onGenerateFinished(Throwable throwable, String savePath, Bitmap bitmap) {
        if (throwable != null || bitmap == null) {
            dismissProgressDialog();
            showToast("操作失败");
        } else {
            Luban.with(this)
                    .load(savePath)
                    .ignoreBy(100)
                    .setTargetDir(FileUtils.getPhotoDirectoryString(BaseActivity.this))
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            showProgressDialog();
                        }

                        @Override
                        public void onSuccess(File file) {
                            dismissProgressDialog();
                            share(file.getAbsolutePath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgressDialog();
                            showToast("操作失败");
                        }
                    }).launch();
        }
    }

    public void share(String filePath) {
        File shareFile = new File(filePath);
        if (!shareFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(MyApplication.getInstance().getApplicationContext()
                    , getPackageName()+".fileprovider", shareFile);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        }
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, "分享图片");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser);
        }
    }

    public GenerateModel getShareLunarModel() {
        SharePicModel sharePicModel = new SharePicModel((ViewGroup) getWindow().getDecorView());
        sharePicModel.setSharePicListener(this);
        sharePicModel.setShareXingZuo(getShareXingZuo());
        sharePicModel.setShareType(getShareType());
        return sharePicModel;
    }

    @Override
    public View getShareContentView() {
        return null;
    }

    public int getShareType() {
        return -1;
    }

    public String getShareXingZuo() {
        return "";
    }

    public void showToast(int textResId) {
        ToastUtil.show(this, textResId);
    }

    public void showToast(String text) {
        ToastUtil.show(this, text);
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
