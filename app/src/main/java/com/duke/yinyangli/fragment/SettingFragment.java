package com.duke.yinyangli.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.tencent.mmkv.MMKV;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.List;

import butterknife.BindView;

public class SettingFragment extends BaseFragment implements OnItemClickListener {

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
            if (Constants.SP_KEY.USER_INFO_AVATAR.equals(item.getId())) {
                showAvatarPicker();
            } else if (Constants.SP_KEY.USER_INFO_NAME.equals(item.getId())) {
                showNameDialog(item.getValue());
            }
        }
    }

    private void showAvatarPicker() {
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(getResources().getColor(R.color.colorPrimary))
                // 返回图标ResId
                .backResId(R.mipmap.back_black)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(getResources().getColor(R.color.black_111111))
                // TitleBar背景色
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(1)
                .build();

        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            if (pathList != null && pathList.size() > 0) {
                MMKV.defaultMMKV().encode(Constants.SP_KEY.USER_INFO_AVATAR, pathList.get(0));
                mAdapter.reloadAvatar();
            }
        }
    }

    @Override
    public long getFragmentId() {
        return Constants.FRAGMENT.ID_SETTING;
    }
}
