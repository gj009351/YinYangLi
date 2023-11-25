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
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener,
        EasyPermissions.PermissionCallbacks,
        View.OnClickListener {

    private static final int RC_PERMISSIONS = 1001;

    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.action_settings_holder)
    View settingHolder;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.rl_tool)
    RelativeLayout mRelativeTool;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.float_view)
    FloatViewBall floatViewBall;
    @BindView(R.id.fab)
    ImageView fab;

    private int mYear;
    private Lunar mCurrentLunar;
    private MainInfoAdapter mAdapter;
    private BasePopupView mSettingWindow;
    private HomeSettingAdapter mSettingAdapter;
    private boolean mInBack;
    private boolean mGoToOther;
    private Calendar mSelectCalendar;

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
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(mAdapter = new MainInfoAdapter(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInBack = false;
        requiresPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoToOther) {
            mInBack = false;
            mGoToOther = false;
        } else {
            mInBack = true;
        }
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
    public void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int currentDay = mCalendarView.getCurDay();
        mSelectCalendar = mCalendarView.getSelectedCalendar();
        mCurrentLunar = Lunar.fromDate(mCalendarView.getSelectedCalendar().getDate());

        onSetCurrentLunar();
        onSetMonthJiXiong(year, month);
    }

    private void onSetMonthJiXiong(int year, int month) {
        Map<String, Calendar> map = new HashMap<>();
        int monthDaysCount = CalendarUtil.getMonthDaysCount(year, month);
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        String ji;
        Lunar tempLunar;
        int redColor = 0xFFD81B60;
        int greenColor = 0xFF40db25;
        int color;
        for (int i = 0; i < monthDaysCount; i++) {
            tempCalendar.set(year, month, i);
            tempLunar = Lunar.fromDate(tempCalendar.getTime());
            ji = tempLunar.getDayTianShenLuck();
            color = "吉".equals(ji) ? greenColor : redColor;
            map.put(getSchemeCalendar(year, month, i + 1, color, ji).toString(),
                    getSchemeCalendar(year, month, i + 1, color, ji));
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        return calendar;
    }

    @Override
    @OnClick({R.id.fab, R.id.action_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                ChooseActivity.start(this);
                break;
            case R.id.action_settings:
                showSettingWindow(view);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mSelectCalendar = calendar;
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        mCurrentLunar = Lunar.fromDate(calendar.getDate());
        onSetCurrentLunar();


        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
    }

    private void onSetCurrentLunar() {
        mAdapter.setLunar(mCurrentLunar);
    }

    @Override
    public void onYearChange(int year) {
        Log.e("onYearChange", "  -- " + year);
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + ", " + month);
        onSetMonthJiXiong(year, month);
    }

    private void showSettingWindow(View view) {
        if (mSettingWindow == null) {
            mSettingWindow = new XPopup.Builder(this)
                    .atView(settingHolder)
                    // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .asAttachList(getResources().getStringArray(R.array.setting_list)
                            , new int[]{}
                            , new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    mSettingWindow.dismiss();
                                    if (getResources().getString(R.string.date_scroll).equals(text)) {
                                        showSelectDatePicker();
                                    } else if (getResources().getString(R.string.share).equals(text)) {
                                        startShare();
                                    } else if (getResources().getString(R.string.setting).equals(text)) {
                                        SettingActivity.start(MainActivity.this);
                                        mGoToOther = true;
                                    } else {
                                        AboutActivity.start(MainActivity.this);
                                        mGoToOther = true;
                                    }
                                }
                            }
                            , R.layout.home_setting_popup_list
                            , R.layout.item_home_setting_list)
                    .show();
        } else {
            mSettingWindow.show();
        }
    }

    private void showSelectDatePicker() {
        DialogUtils.showDatePicker(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                final java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(date);
                mCalendarView.scrollToCalendar(calendar.get(java.util.Calendar.YEAR)
                        , calendar.get(java.util.Calendar.MONTH) + 1
                        , calendar.get(java.util.Calendar.DAY_OF_MONTH));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onReceiveEvent(BaseEvent event) {
        if (event.getCode() == Event.CODE_CHANGE_SETTING_MAIN) {
            mCalendarLayout.expand();
            mAdapter.resetData();
        } else {
            super.onReceiveEvent(event);
        }
    }

    @Override
    public void startShare() {
        showProgressDialog();
        ShareLunarModel shareLunarModel = new ShareLunarModel((ViewGroup) getWindow().getDecorView());
        shareLunarModel.setDate(mSelectCalendar);
        shareLunarModel.setLunar(mCurrentLunar);
        GeneratePictureManager.getInstance().generate(shareLunarModel, this);
    }

    @Override
    public void share(String filePath) {
        super.share(filePath);
//        new XPopup.Builder(this)
//                .asImageViewer(null, filePath, new SmartGlideImageLoader())
//                .show();
    }
}
