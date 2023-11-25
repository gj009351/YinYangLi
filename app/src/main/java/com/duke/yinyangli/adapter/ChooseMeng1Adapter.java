package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

public class ChooseMeng1Adapter extends RecyclerView.Adapter<ChooseMeng1Adapter.ViewHolder> {

    private Context mContext;
    private List<Article> mData = new ArrayList<>();
    private OnItemClickListener mListener;

    public ChooseMeng1Adapter(Context context) {
        mContext = context;
    }

    public void setData(List<Article> articles) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (articles != null && !articles.isEmpty()) {
            mData.addAll(articles);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_choose_meng_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mData.get(position);

        holder.titleView.setText(article.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position, article);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.item_title);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Article article);
    }
}
