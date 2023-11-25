package com.duke.yinyangli.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseViewHolder;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.ArticleViewHolder> {

    private RequestManager mLoader;
    private Context context;
    protected LayoutInflater mInflater;
    private List<Article> mData;

    public MainItemAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mLoader = Glide.with(context.getApplicationContext());
    }

    public void refreshData(List<Article> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.updateView(context, mLoader, position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    static class ArticleViewHolder extends BaseViewHolder {

        private TextView mTextTitle, mTextContent;
        private ImageView mImageView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
        }

        public void updateView(Context context, RequestManager loader, int position, Article item) {
            mTextTitle.setText(item.getTitle());
            mTextContent.setText(item.getContent());
            loader.load(!TextUtils.isEmpty(item.getImgUrl()) ? item.getImgUrl() : item.getImgRes())
                    .centerCrop()
                    .into(mImageView);
        }
    }
}
