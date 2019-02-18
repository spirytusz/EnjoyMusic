package com.zspirytus.enjoymusic.view.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Fork From: https://github.com/amulyakhare/TextDrawable/blob/master/library/src/main/java/com/amulyakhare/textdrawable/TextDrawable.java
 * Modify by: ZSpirtus
 *
 * @author amulya
 * @modify: ZSpirytus
 * @datetime 14 Oct 2014, 3:53 PM
 */

// TODO: 17/02/2019 rotation
public class TextDrawable extends ShapeDrawable {

    private final Paint textPaint;
    private final String text;
    private final int height;
    private final int width;
    private final float radius;

    private TextDrawable(Builder builder) {
        super(builder.shape);

        // shape properties
        height = builder.height;
        width = builder.width;
        radius = builder.radius;

        // text and color
        text = builder.text;

        // text paint settings
        textPaint = new Paint();
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // drawable paint color
        Paint paint = getPaint();
        paint.setColor(ColorGenerator.getColor(text.hashCode()));

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = (int) (width * 0.618f);
        textPaint.setTextSize(fontSize);
        canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);

    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public static IShapeBuilder builder() {
        return new Builder();
    }

    public static class Builder implements IConfigBuilder, IShapeBuilder, IBuilder {

        private String text;

        private int width;

        private int height;

        private Typeface font;

        private RectShape shape;

        public int textColor;

        private boolean isBold;

        public float radius;

        private Builder() {
            text = "";
            textColor = Color.WHITE;
            width = -1;
            height = -1;
            shape = new RectShape();
            font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            isBold = false;
        }

        public IConfigBuilder width(int width) {
            this.width = width;
            return this;
        }

        public IConfigBuilder height(int height) {
            this.height = height;
            return this;
        }

        public IConfigBuilder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public IConfigBuilder useFont(Typeface font) {
            this.font = font;
            return this;
        }

        public IConfigBuilder bold() {
            this.isBold = true;
            return this;
        }

        @Override
        public IConfigBuilder beginConfig() {
            return this;
        }

        @Override
        public IShapeBuilder endConfig() {
            return this;
        }

        @Override
        public IBuilder rect() {
            this.shape = new RectShape();
            return this;
        }

        @Override
        public IBuilder round() {
            this.shape = new OvalShape();
            return this;
        }

        @Override
        public IBuilder roundRect(int radius) {
            this.radius = radius;
            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            this.shape = new RoundRectShape(radii, null, null);
            return this;
        }

        @Override
        public TextDrawable buildRect(String text) {
            rect();
            return build(text);
        }

        @Override
        public TextDrawable build(String text) {
            this.text = text;
            return new TextDrawable(this);
        }
    }

    public interface IConfigBuilder {
        IConfigBuilder width(int width);

        IConfigBuilder height(int height);

        IConfigBuilder textColor(int color);

        IConfigBuilder useFont(Typeface font);

        IConfigBuilder bold();

        IShapeBuilder endConfig();
    }

    public interface IBuilder {

        TextDrawable build(String text);
    }

    public interface IShapeBuilder {

        IConfigBuilder beginConfig();

        IBuilder rect();

        IBuilder round();

        IBuilder roundRect(int radius);

        TextDrawable buildRect(String text);
    }

    private static class ColorGenerator {
        private static final List<Integer> mColors = Arrays.asList(
                0xffe57373, 0xfff06292, 0xffba68c8, 0xff9575cd,
                0xff7986cb, 0xff64b5f6, 0xff4fc3f7, 0xff4dd0e1,
                0xff4db6ac, 0xff81c784, 0xffaed581, 0xffff8a65,
                0xffd4e157, 0xffffd54f, 0xffffb74d, 0xffa1887f,
                0xff90a4ae
        );
        private static final Random mRandom = new Random();

        public static int getColor() {
            return mColors.get(mRandom.nextInt(mColors.size()));
        }

        public static int getColor(Object key) {
            return mColors.get(Math.abs(key.hashCode()) % mColors.size());
        }
    }
}