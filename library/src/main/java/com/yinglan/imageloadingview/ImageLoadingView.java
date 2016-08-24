/*
 *
 *  * Copyright (C) 2015 yinglan sufly0001@gmail.com
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */
package com.yinglan.imageloadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @function ImageLoadingView
 * @auther: Created by yinglan
 * @time: 16/8/24
 */
public class ImageLoadingView extends View {

    private final String TAG = ImageLoadingView.class.getSimpleName();
    private Paint mPaint1;
    private Paint mPaint2;
    private Context mContext;
    private double percent = 0.083;
    private float interval;
    private float radius;
    private int type = 1;

    public ImageLoadingView(Context context) {
        this(context, null);
    }

    public ImageLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public static class ViewType {
        public final static int VIDEO = 0;
        public final static int IMAGE = 1;
    }

    private void initView(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (null == attrs) {
            if (!(getLayoutParams() instanceof FrameLayout.LayoutParams)) {
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(
                                dip2Px(50),
                                dip2Px(50),
                                Gravity.CENTER);
                setLayoutParams(layoutParams);
            }
        }
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(Color.WHITE);
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = getWidth() >= getHeight() ? getHeight() : getWidth();
        interval = (float) (radius * 0.06);
        mPaint2.setStrokeWidth(interval / 3);
        RectF localRect = new RectF(getWidth() / 2 - radius / 2 + interval, getHeight() / 2 - radius / 2 + interval, getWidth() / 2 + radius / 2 - interval, getHeight() / 2 + radius / 2 - interval);
        float f1 = (float) (percent * 360);
        if (this.type == ViewType.VIDEO && percent == 1.0) {
            Path path = new Path();
            path.moveTo(getWidth() / 2 - radius * 0.7f / 6, (float) (getHeight() / 2 + 1.732 * radius / 6));// 此点为多边形的起点
            path.lineTo(getWidth() / 2 + radius / 3, getHeight() / 2);
            path.lineTo(getWidth() / 2 - radius * 0.7f / 6, (float) (getHeight() / 2 - 1.732 * radius / 6));
            path.close();
            canvas.drawPath(path, mPaint1);
        } else {
            canvas.drawArc(localRect, -90, f1, true, mPaint1);
        }
        canvas.save();
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius / 2 - interval / 3, mPaint2);
        canvas.restore();
    }

    public void setProgress(double progress) {
        if (progress == 0) {
            progress = 0.083;
        } else if ((progress < 0 || progress >= 1) && ViewType.IMAGE == type) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        this.percent = progress;
        invalidate();//重新执行onDraw方法,重新绘制图形
    }

    public void loadCompleted() {
        setVisibility(GONE);
    }

    public void loadCompleted(int type) {
        this.type = type;
        setProgress(1.0);
    }

    public void loadFaild() {
        setProgress(1.0);
        setVisibility(GONE);
    }

    public void setOutsideCircleColor(int color) {
        mPaint2.setColor(color);
    }

    public void setInsideCircleColor(int color) {
        mPaint1.setColor(color);
    }

    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);

        } else if (target.getParent() instanceof ViewGroup) {
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

            badgeContainer.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(target);

            badgeContainer.addView(this);
        } else if (target.getParent() == null) {

        }

    }

    /*
     * converts dip to px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}

