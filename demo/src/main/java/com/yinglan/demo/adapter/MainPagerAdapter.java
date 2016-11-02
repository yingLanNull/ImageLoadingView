package com.yinglan.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yinglan.demo.LargerActivity;
import com.yinglan.demo.application.MyApp;
import com.yinglan.demo.model.Address;
import com.yinglan.demo.model.Constant;
import com.yinglan.imageloadingview.ImageLoadingView;

import java.util.ArrayList;

/**
 * @function viewpageradapter
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class MainPagerAdapter extends PagerAdapter {

    private ArrayList<Address> mAllAddressList;
    private Context mContext;

    public MainPagerAdapter(Context context) {
        mContext = context;
        mAllAddressList = new ArrayList<>();
        initGirlUrl();
    }

    private void initGirlUrl() {
        mAllAddressList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setImageUrl(Constant.ImageUrl[i]);
            mAllAddressList.add(address);
        }
    }

    @Override
    public int getCount() {
        return mAllAddressList.size();
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        if (null != object) {
            container.removeView((View) object);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView;
        final Address address = mAllAddressList.get(position);

        FrameLayout frameLayout = new FrameLayout(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);

        final ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        frameLayout.addView(imageView);//为imageview控件增加一个固定的父布局

        final ImageLoadingView countDownIndicator = new ImageLoadingView(mContext);
        countDownIndicator.setTargetView(imageView);

        ImageLoader.getInstance().displayImage(address.getImageUrl(), imageView, MyApp.defaultOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                countDownIndicator.loadFaild();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                countDownIndicator.loadCompleted();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                countDownIndicator.setProgress(current / (total * 1.00));//防止int相除取整型
            }
        });

        itemView = frameLayout;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("uri", address.getImageUrl());
                Intent intent = new Intent(mContext, LargerActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        container.addView(itemView);
        return itemView;
    }
}
