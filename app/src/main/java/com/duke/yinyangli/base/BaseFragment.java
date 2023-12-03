package com.duke.yinyangli.base;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duke.yinyangli.R;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.generateImage.GeneratePictureManager;
import com.duke.yinyangli.utils.generateImage.OnSharePicListener;
import com.duke.yinyangli.utils.generateImage.SharePicModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    public TextView title;
    public View left;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView(view);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        if (requestButterKnife()) {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null && requestButterKnife()) {
            unbinder.unbind();
            unbinder = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(BaseEvent event) {
        Bundle bundle = event.getBundle();
        LogUtils.d("onReceive message event:" + event.getCode() + ", " + event.getBundle());
        if (event.getCode() == Event.CODE_UPDATE_VERSION) {
            if (bundle != null) {
            }
        }
    }

    public abstract int getLayoutId();
    public abstract long getFragmentId();
    public abstract void initData();

    public void initView(View view) {
        title = view.findViewById(R.id.title);
        left = view.findViewById(R.id.left);
        if (left != null) {
            left.setVisibility(GONE);
        }
    }
    public boolean requestButterKnife() {
        return true;
    }

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
