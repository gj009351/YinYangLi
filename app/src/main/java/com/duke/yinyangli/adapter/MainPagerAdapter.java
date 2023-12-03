package com.duke.yinyangli.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseFragment;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.fragment.CalendarFragment;
import com.duke.yinyangli.fragment.ChooseFragment;
import com.duke.yinyangli.fragment.SettingFragment;
import com.duke.yinyangli.utils.SettingUtil;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentStateAdapter {

    private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();
    private ChooseFragment mChooseFragment;
    private SettingFragment mSettingFragment;
    private CalendarFragment mCalendarFragment;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        loadFragment();
    }

    @Override
    public long getItemId(int position) {
        BaseFragment fragment = mFragments.get(position);
        return fragment.getFragmentId();
    }

    @NonNull
    @Override
    public BaseFragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments != null ? mFragments.size() :  0;
    }

    public void loadFragment() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        } else {
            mFragments.clear();
        }

        if (mCalendarFragment == null) {
            mCalendarFragment = new CalendarFragment();
        }
        mFragments.add(mCalendarFragment);

        if (SettingUtil.hasTabChoose()) {
            if (mChooseFragment == null) {
                mChooseFragment = new ChooseFragment();
            }
            mFragments.add(mChooseFragment);
        }

        if (SettingUtil.hasTabSetting()) {
            if (mSettingFragment == null) {
                mSettingFragment = new SettingFragment();
            }
            mFragments.add(mSettingFragment);
        }
        notifyDataSetChanged();
    }

    public int getPositionByTag(String tag) {
        if (Constants.FRAGMENT.TAG_MAIN.equals(tag)) {
            return 0;
        } else if (Constants.FRAGMENT.TAG_CHOOSE.equals(tag) && mChooseFragment != null
                && mFragments.contains(mChooseFragment)) {
            return mFragments.indexOf(mChooseFragment);
        } else if (Constants.FRAGMENT.TAG_SETTING.equals(tag) && mSettingFragment != null
                && mFragments.contains(mSettingFragment)) {
            return mFragments.indexOf(mSettingFragment);
        }
        return 0;
    }
}
