package com.duke.yinyangli.utils.generateImage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duke.yinyangli.R;


/**
 * Created by HomgWu on 2017/11/29.
 */

public class SharePicModel extends GenerateModel {

    private View mSharePicView;

    public SharePicModel(ViewGroup rootView) {
        super(rootView);
    }

    @Override
    protected void startPrepare(GeneratePictureManager.OnGenerateListener listener) throws Exception {
        mSharePicView = LayoutInflater.from(mContext).inflate(R.layout.share_picture, mRootView, false);
//        mTitleAvatarIv = mSharePicView.findViewById(R.id.invitation_share_link_pic_avatar_iv);
//        RoundedBitmapDrawable circularBitmapDrawable =
//                RoundedBitmapDrawableFactory.create(mContext.getResources(), BitmapFactory.decodeResource(mContext.getResources(), mAvatarResId));
//        circularBitmapDrawable.setCircular(true);
//        mTitleAvatarIv.setImageDrawable(circularBitmapDrawable);
        prepared(listener);
    }

    @Override
    public View getView() {
        return mSharePicView;
    }

}
