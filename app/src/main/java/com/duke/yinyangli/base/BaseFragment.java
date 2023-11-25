package com.duke.yinyangli.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duke.yinyangli.R;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.OnSharePicListener;
import com.duke.yinyangli.utils.generateImage.SharePicModel;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public abstract int getLayoutId();
    public abstract void initView(View view);

    @Override
    public void onClick(View v) {
    }

    public boolean isSafe() {
        return getActivity() != null && getActivity() instanceof BaseActivity && ((BaseActivity) getActivity()).isSafe();
    }

    public void dismissProgressDialog() {
        if (isSafe()) {
            ((BaseActivity) getActivity()).dismissProgressDialog();
        }
    }

    public void showProgressDialog() {
        if (isSafe()) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    public void startShare() {
        if (isSafe()) {
            ((BaseActivity) getActivity()).startShare();
        }
    }

    public void showToast(int textResId) {
        if (isSafe()) {
            ((BaseActivity) getActivity()).showToast(textResId);
        }
    }

    public void showToast(String text) {
        if (isSafe()) {
            ((BaseActivity) getActivity()).showToast(text);
        }
    }

}
