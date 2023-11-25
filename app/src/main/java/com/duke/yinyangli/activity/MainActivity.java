package com.duke.yinyangli.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.HomeSettingAdapter;
import com.duke.yinyangli.adapter.MainInfoAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.ShareLunarModel;
import com.duke.yinyangli.view.FloatViewBall;
import com.haibin.calendarview.group.GroupItemDecoration;
import com.haibin.calendarview.library.Article;
import com.haibin.calendarview.library.Calendar;
import com.haibin.calendarview.library.CalendarLayout;
import com.haibin.calendarview.library.CalendarUtil;
import com.haibin.calendarview.library.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.mmkv.MMKV;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements
        EasyPermissions.PermissionCallbacks,
        View.OnClickListener {

    private static final int RC_PERMISSIONS = 1001;

    @BindView(R.id.float_view)
    FloatViewBall floatViewBall;
    @BindView(R.id.fab)
    ImageView fab;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public boolean requestButterKnife() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();

        initBall();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requiresPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void requiresPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            ActivityCompat.requestPermissions(this, perms, RC_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtils.d("onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//            EasyPermissions.requestPermissions(this, getString(R.string.apply_for_permission_content), RC_PERMISSIONS, perms.toArray(new String[perms.size()]));
        }
    }

    private void initBall() {
        floatViewBall.setVisibility(View.VISIBLE);
        int left = MMKV.defaultMMKV().decodeInt(Constants.SP_KEY.MAIN_LEFT, 0);
        int top = MMKV.defaultMMKV().decodeInt(Constants.SP_KEY.MAIN_TOP, 0);
        int right = MMKV.defaultMMKV().decodeInt(Constants.SP_KEY.MAIN_RIGHT, 0);
        int bottom = MMKV.defaultMMKV().decodeInt(Constants.SP_KEY.MAIN_BOTTOM, 0);
        LogUtils.e("ball margin:" + left + ", " + top + ", " + right + ", " + bottom);
        fab.setLeft(left);
        fab.setTop(top);
        fab.setRight(right);
        fab.setBottom(bottom);
        floatViewBall.bringToFront();
    }

    @Override
    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                ChooseActivity.start(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
