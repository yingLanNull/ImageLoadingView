# ImageLoadingView
## Abstract 摘要
类似微信和微博图片或视频加载时的轻量级loading

## Gif 动画
![1](https://github.com/yingLanNull/ImageLoadingView/blob/master/Show/1.gif)
![2](https://github.com/yingLanNull/ImageLoadingView/blob/master/Show/2.gif)

## Demo 下载APK体验
[Download Demo](https://github.com/yingLanNull/ImageLoadingView/raw/master/Show/demo-debug.apk)

## Usage 使用方法
### Step 1
#### Gradle 配置
```
dependencies {
    compile 'com.yinglan.imageloadingview:library:0.1.0'
}
```

### Step 2

#### In layout
```
	    <com.yinglan.imageloadingview.ImageLoadingView
                android:id="@+id/loadingview"
                android:layout_centerInParent="true"
                android:layout_width="50dp"
                android:layout_height="50dp" />

```

### or

#### In Java Code
```
	{
	   ImageLoadingView loadingView = new ImageLoadingView(this);
       loadingView.setTargetView(imageView);
    }

```
### Step 3
```
提供方法:
	    loadingView.setProgress(float f); //0.0 ~ 1.0;
	    loadingView.loadCompleted();
	    loadingView.loadCompleted(ImageLoadingView.ViewType.IMAGE);
	    loadingView.loadCompleted(ImageLoadingView.ViewType.VIDEO);
        loadingView.loadFaild();
        loadingView.setInsideCircleColor(int color);
        loadingView.setOutsideCircleColor(int color);
```

## FAQ 注意

```
	在为动态生成的控件添加ImageLoadingView时,需要注意先为该控件添加可靠父布局,再调用setTargetView(imageView).
	本demo中是使用imageloader来实现的图片加载进度监听回调
```

## LICENSE 开源许可

    Apache License Version 2.0

