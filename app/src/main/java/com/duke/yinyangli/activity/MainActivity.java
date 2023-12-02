package com.duke.yinyangli.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.fragment.CalendarFragment;
import com.duke.yinyangli.fragment.ChooseFragment;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.SettingUtil;
import com.duke.yinyangli.view.FloatViewBall;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

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
    @BindView(R.id.tab_container)
    View tabContainer;
    @BindView(R.id.tab_main)
    View tabMain;
    @BindView(R.id.tab_choose)
    View tabChoose;
    @BindView(R.id.tab_book)
    View tabBook;
    @BindView(R.id.tab_setting)
    View tabSetting;

    private FragmentTransaction mTransaction;
    private String mCurrentTab;
    private CalendarFragment mCalendarFragment;
    private ChooseFragment mChooseFragment;

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

        mCurrentTab = Constants.FRAGMENT.TAG_MAIN;
        initBall();
        initFragment();
        initTab();
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

    private void initTab() {
        tabChoose.setVisibility(SettingUtil.hasTabChoose() ? View.VISIBLE : View.GONE);
        tabBook.setVisibility(SettingUtil.hasTabBook() ? View.VISIBLE : View.GONE);
        tabSetting.setVisibility(SettingUtil.hasTabSetting() ? View.VISIBLE : View.GONE);
        tabContainer.setVisibility(SettingUtil.hasTabChoose() || SettingUtil.hasTabBook()
                || SettingUtil.hasTabSetting() ? View.VISIBLE : View.GONE);
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
    @OnClick({R.id.fab, R.id.tab_main, R.id.tab_choose, R.id.tab_book, R.id.tab_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                ChooseActivity.start(this);
                break;
            case R.id.tab_main:
                showFragment(Constants.FRAGMENT.TAG_MAIN);
                break;
            case R.id.tab_choose:
                showFragment(Constants.FRAGMENT.TAG_CHOOSE);
                break;
            case R.id.tab_book:
                showFragment(Constants.FRAGMENT.TAG_BOOK);
                break;
            case R.id.tab_setting:
                showFragment(Constants.FRAGMENT.TAG_SETTING);
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

    private void initFragment() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        if (mCalendarFragment == null) {
            mCalendarFragment = new CalendarFragment();
        }
        mTransaction.add(R.id.fragment_container, mCalendarFragment);
        if (SettingUtil.hasFabChoose()) {
            if (mChooseFragment == null) {
                mChooseFragment = new ChooseFragment();
            }
            mTransaction.add(R.id.fragment_container, mChooseFragment);
        }
        mTransaction.commit();
        showFragment(mCurrentTab);
    }

    private void hideFragment(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (!tag.equals(Constants.FRAGMENT.TAG_MAIN)) {
                mTransaction.hide(mCalendarFragment);
            }
            if (!tag.equals(Constants.FRAGMENT.TAG_CHOOSE)) {
                mTransaction.hide(mChooseFragment);
            }
        }
    }

    private void showFragment(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            mTransaction = getSupportFragmentManager().beginTransaction();
            hideFragment(tag);
            Fragment fragment = null;
            if (tag.equals(Constants.FRAGMENT.TAG_MAIN)) {
                if (mCalendarFragment == null) {
                    mCalendarFragment = new CalendarFragment();
                }
                fragment = mCalendarFragment;
            }
            if (tag.equals(Constants.FRAGMENT.TAG_CHOOSE)) {
                if (mChooseFragment == null) {
                    mChooseFragment = new ChooseFragment();
                }
                fragment = mChooseFragment;
            }
            if (fragment != null) {
                mTransaction.show(fragment);
            }
            mTransaction.commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onReceiveEvent(BaseEvent event) {
        if (event.getCode() == Event.CODE_CHANGE_SETTING_MAIN) {
            if (SettingUtil.hasTabChoose()) {
                if (mChooseFragment == null) {
                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mChooseFragment = new ChooseFragment();
                    mTransaction.add(R.id.fragment_container, mChooseFragment);
                    mTransaction.commit();
                }
                tabChoose.setVisibility(View.VISIBLE);
            } else {
                if (Constants.FRAGMENT.TAG_CHOOSE.equals(mCurrentTab)) {
                    showFragment(Constants.FRAGMENT.TAG_MAIN);
                }
                tabChoose.setVisibility(View.GONE);
            }
            tabContainer.setVisibility(SettingUtil.hasTabChoose() || SettingUtil.hasTabBook()
                    || SettingUtil.hasTabSetting() ? View.VISIBLE : View.GONE);
        } else {
            super.onReceiveEvent(event);
        }
    }

}
