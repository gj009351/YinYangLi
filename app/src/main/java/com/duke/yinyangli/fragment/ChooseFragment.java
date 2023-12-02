package com.duke.yinyangli.fragment;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.activity.ChooseActivity;
import com.duke.yinyangli.adapter.ChooseAdapter;
import com.duke.yinyangli.base.BaseFragment;
import com.duke.yinyangli.utils.LogUtils;

import butterknife.BindView;

public class ChooseFragment extends BaseFragment {

    private ChooseAdapter mChooseAdapter;

    @Override
    public void initView(View view) {
        TextView title = view.findViewById(R.id.title);
        View left = view.findViewById(R.id.left);
        if (left != null) {
            left.setVisibility(GONE);
        }
        title.setText(R.string.select);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChooseAdapter(getActivity()));
        recyclerView.setAdapter(mChooseAdapter = new ChooseAdapter(getActivity()));
    }

    @Override
    public void initData() {
        LogUtils.d("ChooseFragment:initData");
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ChooseActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_choose;
    }

}
