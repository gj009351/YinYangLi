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
import com.duke.yinyangli.utils.CompressEngine;
import com.duke.yinyangli.utils.CropEngine;
import com.duke.yinyangli.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

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
            if (Constants.SP_KEY.USER_INFO_AVATAR.equals(item.getId())) {
                showAvatarPicker();
            } else if (Constants.SP_KEY.USER_INFO_NAME.equals(item.getId())) {
                showNameDialog(item.getValue());
            }
        }
    }

    private void showAvatarPicker() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())//相册 媒体类型 SelectMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                //.openCamera()//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
//                .theme()// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .setImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .setCompressEngine(new CompressEngine()) // 自定义图片压缩引擎
                .setCropEngine(new CropEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(true, 40)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                .isDirectReturnSingle(false)//PictureConfig.SINGLE模式下是否直接返回
                .isDisplayCamera(false)//列表是否显示拍照按钮
                .isSelectZoomAnim(true)//图片选择缩放效果
                .setImageSpanCount(4)//列表每行显示个数
                .setFilterMinFileSize(50) // 过滤最小的文件
                .isGif(false)//是否显示gif
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result != null && result.size() > 0) {
                            MMKV.defaultMMKV().encode(Constants.SP_KEY.USER_INFO_AVATAR, result.get(0).getCutPath());
                            mAdapter.reloadAvatar();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
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
