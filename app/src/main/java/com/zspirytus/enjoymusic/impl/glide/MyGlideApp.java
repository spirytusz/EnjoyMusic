package com.zspirytus.enjoymusic.impl.glide;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class MyGlideApp extends AppGlideModule {

    @SuppressLint("CheckResult")
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        RequestOptions options = new RequestOptions();
        options.format(DecodeFormat.PREFER_RGB_565);
        options.disallowHardwareConfig();
        if (isLowRamDevice(activityManager)) {
            options.downsample(new HALF());
            options.skipMemoryCache(true);
        } else {
            MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
            builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        }
        builder.setDefaultRequestOptions(options);
    }

    /**
     * 判断是否低端设备
     *
     * @param activityManager ActivityManager
     * @return 是否低端设备
     */
    private boolean isLowRamDevice(ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return activityManager.isLowRamDevice() || activityManager.getLargeMemoryClass() < 512;
        } else {
            return true;
        }
    }

    /**
     * 采样率降低到1/4
     */
    private class HALF extends DownsampleStrategy {
        @Override
        public float getScaleFactor(int sourceWidth, int sourceHeight, int requestedWidth, int requestedHeight) {
            return 0.5f;
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int sourceWidth, int sourceHeight, int requestedWidth, int requestedHeight) {
            return DownsampleStrategy.SampleSizeRounding.QUALITY;
        }
    }
}
