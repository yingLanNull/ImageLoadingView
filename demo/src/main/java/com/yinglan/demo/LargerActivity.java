package com.yinglan.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yinglan.demo.application.MyApp;
import com.yinglan.imageloadingview.ImageLoadingView;

/**
 * @function two
 * @auther: Created by yinglan
 * @time: 16/8/22
 */
public class LargerActivity extends AppCompatActivity {

    private ImageView imageview;
    private ImageLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_larger);
        imageview = (ImageView) findViewById(R.id.image);

        /**在layout中的使用*/
        loadingView = (ImageLoadingView) findViewById(R.id.loadingview);
        /**设置颜色*/
        loadingView.setInsideCircleColor(Color.GREEN);
        loadingView.setOutsideCircleColor(Color.BLUE);

        initData();
    }

    private void initData() {
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String uri = bundle.getString("uri");

        ImageLoader.getInstance().displayImage(uri, imageview, MyApp.defaultOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loadingView.loadFaild();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                loadingView.loadCompleted(ImageLoadingView.ViewType.IMAGE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                loadingView.setProgress(current / (total * 1.00));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }
}
