package com.duke.yinyangli.utils.generateImage;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.FileUtils;
import com.duke.yinyangli.utils.ImageUtils;
import com.duke.yinyangli.utils.UserInfo;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.haibin.calendarview.library.Article;


/**
 * Created by HomgWu on 2017/11/29.
 */

public class SharePicModel extends GenerateModel {

    private View mSharePicView;
    private int mArticleType;
    private OnSharePicListener mListener;
    private String mXingZuo;

    public SharePicModel(ViewGroup rootView) {
        super(rootView);
    }

    @Override
    protected void startPrepare(GeneratePictureManager.OnGenerateListener listener) throws Exception {
        mSharePicView = LayoutInflater.from(mContext).inflate(R.layout.share_picture, mRootView, false);

        ImageView imageView = mSharePicView.findViewById(R.id.image_right);
//            ImageUtils.setBlur(mSharePicView.getContext(), mSharePicView, R.mipmap.share_chenggu);
        if (Constants.TYPE.TYPE_CAO == mArticleType || Constants.TYPE.TYPE_QIAN == mArticleType) {
            mSharePicView.setBackgroundResource(R.mipmap.suangua);
            mSharePicView.getBackground().setColorFilter(getColor(R.color.white_8ff0f0f0), PorterDuff.Mode.SRC_OVER);
        } else if (Constants.TYPE.TYPE_CHENGGU == mArticleType) {
            mSharePicView.setBackgroundResource(R.mipmap.share_chenggu);
        } else if (Constants.TYPE.TYPE_BAZI == mArticleType) {
            mSharePicView.setBackgroundColor(getColor(R.color.white));
            imageView.setImageResource(R.mipmap.bazi);
        } else if (Constants.TYPE.TYPE_XINGMING == mArticleType) {
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        } else if (Constants.TYPE.TYPE_XINGZUOMINGYUN == mArticleType) {
            ImageUtil.setXingZuoImage(imageView, mXingZuo);
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        } else if (Constants.TYPE.TYPE_XINGZUOPEIDUI == mArticleType) {
            imageView.setImageResource(R.mipmap.peidui);
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        } else if (Constants.TYPE.TYPE_SHENGXIAOPEIDUI == mArticleType) {
            imageView.setImageResource(R.mipmap.peidui);
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        } else if (Constants.TYPE.TYPE_ZHUGESHENSUAN == mArticleType) {
            imageView.setImageResource(R.mipmap.zhugeshensuan);
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        } else if (Constants.TYPE.TYPE_ZHOUGONGJIEMENG == mArticleType) {
            imageView.setImageResource(R.mipmap.zhougongjiemeng);
            mSharePicView.setBackgroundColor(getColor(R.color.white));
        }

        ImageView userIcon = mSharePicView.findViewById(R.id.imgUserIcon);
        Glide.with(MyApplication.getInstance().getApplicationContext()).load(UserInfo.getUserIcon()).placeholder(R.mipmap.usericon).into(userIcon);

        TextView name = mSharePicView.findViewById(R.id.tvUserName);
        name.setText(UserInfo.getUserName());

        String description = UserInfo.getUserDescription();
        TextView descriptionView = mSharePicView.findViewById(R.id.tvUserDes);
        if (StringUtils.isEmpty(description)) {
            descriptionView.setVisibility(View.GONE);
        } else {
            descriptionView.setVisibility(View.VISIBLE);
            descriptionView.setText(description);
        }
        if (mListener != null) {
            LinearLayout llContent = mSharePicView.findViewById(R.id.llContent);
            llContent.addView(mListener.getShareContentView());
        } else {
            throw new Exception("error:no content view");
        }

        ImageView qrcode = mSharePicView.findViewById(R.id.imgQrCode);
        prepared(listener);
    }

    @Override
    public View getView() {
        return mSharePicView;
    }

    @Override
    public String getSavePath() {
        return FileUtils.getTimeNameImage(getView().getContext());
    }

    public interface OnSharePicListener {
        View getShareContentView();
    }

    public void setSharePicListener(OnSharePicListener listener) {
        mListener = listener;
    }

    public void setShareType(int type) {
        mArticleType = type;
    }

    public void setShareXingZuo(String xingZuo) {
        mXingZuo = xingZuo;
    }

    private int getColor(int colorResId) {
        return getView().getResources().getColor(colorResId);
    }

    private int getXingZuoBackground() {
        switch (mXingZuo) {
            case "水瓶座":
                return R.mipmap.share_bg_shuiping;
            case "白羊座":
                return R.mipmap.share_bg_baiyang;
            case "金牛座":
                return R.mipmap.share_bg_jinniu;
            case "双子座":
                return R.mipmap.share_bg_shuangzi;
            case "巨蟹座":
                return R.mipmap.share_bg_juxie;
            case "狮子座":
                return R.mipmap.share_bg_shizi;
            case "处女座":
                return R.mipmap.share_bg_chunv;
            case "天秤座":
                return R.mipmap.share_bg_tiancheng;
            case "天蝎座":
                return R.mipmap.share_bg_tianxie;
            case "射手座":
                return R.mipmap.share_bg_sheshou;
            case "摩羯座":
                return R.mipmap.share_bg_mojie;
            case "双鱼座":
                return R.mipmap.share_bg_shuangyu;
            default:
                return R.mipmap.share_bg_jinniu;
        }
    }

}
