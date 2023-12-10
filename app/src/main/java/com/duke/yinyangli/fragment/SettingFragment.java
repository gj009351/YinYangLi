package com.duke.yinyangli.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.duke.yinyangli.R;
import com.duke.yinyangli.activity.SettingActivity;
import com.duke.yinyangli.adapter.SettingAdapter;
import com.duke.yinyangli.base.BaseFragment;
import com.duke.yinyangli.base.BaseSettingItem;
import com.duke.yinyangli.bean.StringSettingItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.dialog.EditNameDialog;

import butterknife.BindView;

public class SettingFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SettingAdapter mAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        title.setText(R.string.setting);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter = new SettingAdapter());
        mAdapter.loadSetting();
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        BaseSettingItem selectItem = mAdapter.getItem(position);
        if (selectItem == null) {
            return;
        }
        if (selectItem instanceof StringSettingItem) {
            StringSettingItem item = (StringSettingItem) selectItem;
            if (Constants.SP_KEY.USER_INFO_NAME.equals(item.getId())) {
                showNameDialog(item.getValue());
            }
        }
    }

    private void showNameDialog(String text) {
        DialogUtils.showNameDialog(getActivity(), text, new EditNameDialog.OnClickListener() {
            @Override
            public void onConfirm(String name) {
                mAdapter.reloadName();
            }
        });
    }

    @Override
    public long getFragmentId() {
        return Constants.FRAGMENT.ID_SETTING;
    }
}
