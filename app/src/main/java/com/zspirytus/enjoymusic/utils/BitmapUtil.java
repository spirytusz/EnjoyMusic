package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.zspirytus.enjoymusic.global.MainApplication;

/**
 * Created by ZSpirytus on 2018/12/15.
 * <p>
 * Bitmap压缩工具类
 */

public class BitmapUtil {

    private static final int TARGET_WIDTH_DP = 96;
    private static final int TARGET_HEIGHT_DP = 96;

    private BitmapUtil() {
        throw new AssertionError();
    }

    public static Bitmap compressCenterCrop(String imagePath) {
        return compress(imagePath, true);
    }

    public static Bitmap compress(String imagePath, boolean centerCrop) {
        int[] widthHeight = getWidthHeight(imagePath);
        int width = widthHeight[0];
        int height = widthHeight[1];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeInSampleSize(width, height);
        Bitmap compressBitmap = BitmapFactory.decodeFile(imagePath, options);
        if (centerCrop && width != height) {
            compressBitmap = centerCrop(compressBitmap);
        }
        return compressBitmap;
    }

    public static Bitmap createBitmapByResId(int resId) {
        return BitmapFactory.decodeResource(MainApplication.getBackgroundContext().getResources(), resId);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap bitmapBlur(Context context, Bitmap source, int radius) {
        int width = Math.round(source.getWidth() * 0.125f);
        int height = Math.round(source.getHeight() * 0.125f);
        Bitmap inputBmp = Bitmap.createScaledBitmap(source, width, height, false);
        RenderScript renderScript = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(inputBmp);
        renderScript.destroy();
        return inputBmp;
    }

    private static int computeInSampleSize(int width, int height) {
        if (width == height) {
            return width / PixelsUtil.dp2px(MainApplication.getBackgroundContext(), TARGET_WIDTH_DP);
        } else if (width > height) {
            return height / PixelsUtil.dp2px(MainApplication.getBackgroundContext(), TARGET_HEIGHT_DP);
        } else {
            return width / PixelsUtil.dp2px(MainApplication.getBackgroundContext(), TARGET_WIDTH_DP);
        }
    }

    private static int[] getWidthHeight(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    private static Bitmap centerCrop(Bitmap source) {
        Bitmap cropBitmap;
        int compressWidth = source.getWidth();
        int compressHeight = source.getHeight();
        int cropX = 0;
        int cropY = 0;
        if (compressWidth >= compressHeight) {
            cropX = compressWidth / 2 - compressHeight / 2;
            int newWidth = compressHeight;
            int newHeight = compressHeight;
            cropBitmap = Bitmap.createBitmap(source, cropX, cropY, newWidth, newHeight);
        } else {
            cropY = compressHeight / 2 - compressWidth / 2;
            int newWidth = compressWidth;
            int newHeight = compressWidth;
            cropBitmap = Bitmap.createBitmap(source, cropX, cropY, newWidth, newHeight);
        }
        return cropBitmap;
    }
}
