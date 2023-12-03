package com.duke.yinyangli.fragment;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.duke.yinyangli.R;
import com.duke.yinyangli.activity.AboutActivity;
import com.duke.yinyangli.activity.ChooseActivity;
import com.duke.yinyangli.activity.MainActivity;
import com.duke.yinyangli.activity.SettingActivity;
import com.duke.yinyangli.adapter.MainInfoAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.base.BaseFragment;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.ShareLunarModel;
import com.haibin.calendarview.group.GroupItemDecoration;
import com.haibin.calendarview.library.Article;
import com.haibin.calendarview.library.Calendar;
import com.haibin.calendarview.library.CalendarLayout;
import com.haibin.calendarview.library.CalendarUtil;
import com.haibin.calendarview.library.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CalendarFragment extends BaseFragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener {


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



    private int mYear;
    private Lunar mCurrentLunar;
    private MainInfoAdapter mAdapter;
    private BasePopupView mSettingWindow;
    private Calendar mSelectCalendar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_calendar;
    }

    @Override
    public void initView(View view) {
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
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(mAdapter = new MainInfoAdapter(getContext()));
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

    @Override
    @OnClick({R.id.action_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_settings:
                showSettingWindow(view);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onReceiveEvent(BaseEvent event) {
        if (event.getCode() == Event.CODE_CHANGE_SETTING_MAIN) {
            mCalendarLayout.expand();
            mAdapter.resetData();
        } else {
            super.onReceiveEvent(event);
        }
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

    private void showSettingWindow(View view) {
        if (mSettingWindow == null) {
            mSettingWindow = new XPopup.Builder(getActivity())
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
                                        SettingActivity.start(getActivity());
                                    } else {
                                        AboutActivity.start(getActivity());
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
        DialogUtils.showDatePicker(getActivity(), new OnTimeSelectListener() {
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

    @Override
    public void startShare() {
        if (isSafe()) {
            showProgressDialog();
            ShareLunarModel shareLunarModel = new ShareLunarModel((ViewGroup) getActivity().getWindow().getDecorView());
            shareLunarModel.setDate(mSelectCalendar);
            shareLunarModel.setLunar(mCurrentLunar);
            GeneratePictureManager.getInstance().generate(shareLunarModel, (BaseActivity)getActivity());
        }
    }

    @Override
    public long getFragmentId() {
        return Constants.FRAGMENT.ID_CALENDAR;
    }

}
