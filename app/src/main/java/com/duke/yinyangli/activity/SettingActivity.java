package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.SettingAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseSettingItem;
import com.duke.yinyangli.bean.StringSettingItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.dialog.EditNameDialog;

import butterknife.BindView;

public class SettingActivity extends BaseActivity implements OnItemClickListener {

    private static final int REQUEST_LIST_CODE = 325;

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
    public void initView() {
        super.initView();
        title.setText(R.string.setting);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new SettingAdapter());
        mAdapter.loadSetting();
        mAdapter.setOnItemClickListener(this);
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
        DialogUtils.showNameDialog(this, text, new EditNameDialog.OnClickListener() {
            @Override
            public void onConfirm(String name) {
                mAdapter.reloadName();
            }
        });
    }

}
