package com.yinglan.demo;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinglan.demo.adapter.MainPagerAdapter;
import com.yinglan.imageloadingview.ImageLoadingView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @function main
 * @auther: Created by yinglan
 * @time: 16/8/22
 */
public class MainActivity extends AppCompatActivity {

    private int REFRESH_INTERVAL_SEC = 10;//间隔时间,也是倒计时的总时长
    private int REFRESH_PROGRESS_INTERVAL_SEC = 60;//刷新时间
    private final int REFRESH_PROGRESS = 1;
    private final int UPDATE_PROGRESS = 2;
    private double mProgress;
    private double mProgressStep;//执行次数
    private ViewPager viewPager;
    private ImageLoadingView loadingView;
    private Timer mTimer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    loadingView.setProgress(mProgress);
                    break;
                case UPDATE_PROGRESS:
                    loadingView.loadCompleted(ImageLoadingView.ViewType.VIDEO);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainPagerAdapter(this));
        ImageView imageView = (ImageView) findViewById(R.id.icon);

        /**代码中动态使用 setting*/
        loadingView = new ImageLoadingView(this);
        loadingView.setTargetView(imageView);

        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProgress == 0) {
                    mTimer.purge();
                    mTimer.schedule(new TimerTasks(), 0, REFRESH_PROGRESS_INTERVAL_SEC);
                }
            }
        });
    }

    private void initData() {
        mProgressStep = REFRESH_PROGRESS_INTERVAL_SEC / (double) (REFRESH_INTERVAL_SEC * 1000);
        mTimer = new Timer();
        mTimer.schedule(new TimerTasks(), 0, REFRESH_PROGRESS_INTERVAL_SEC);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //imageloader的进度回调需要在开启硬盘缓存时才有效,此处为了显现测试效果,就在activity不可见的时候清空了缓存,请尽量在wifi下测试
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }


    class TimerTasks extends TimerTask {

        @Override
        public void run() {
            //执行需要完成的方法
            mProgress += mProgressStep;
            if (mProgress >= 1) {
                mProgress = 0;
                this.cancel();
                mHandler.sendEmptyMessage(UPDATE_PROGRESS);

            } else {
                //刷新倒计时
                mHandler.sendEmptyMessage(REFRESH_PROGRESS);

            }
        }
    }

}
