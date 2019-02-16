package com.zspirytus.enjoymusic.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;

public class EqualizerView extends ConstraintLayout {

    private static final String TAG = "EqualizerView";

    private OnBandLevelChangeListener mListener;

    @ColorInt
    private int mTextColor;
    @ColorInt
    private int mProgressColor;
    private Bitmap mThumb;

    public EqualizerView(Context context) {
        this(context, null);
    }

    public EqualizerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        /*EqualizerMetaData metaData = new EqualizerMetaData();
        metaData.setBand((short) 5);
        metaData.setMaxRange((short) (10 * 1000));
        metaData.setMinRange((short) (-10 * 1000));
        metaData.setCenterFreq(new short[]{(short) 0, (short) 100, (short) 200, (short) 300, (short) 400});
        setEqualizerMetaData(metaData);*/
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EqualizerView);
        mTextColor = array.getColor(R.styleable.EqualizerView_textColor, getResources().getColor(R.color.black));
        mProgressColor = array.getColor(R.styleable.EqualizerView_progressColor, getResources().getColor(R.color.colorAccent));
        int thumbId = array.getResourceId(R.styleable.EqualizerView_seekBarThumb, R.drawable.seekbar_thumb);
        mThumb = BitmapFactory.decodeResource(getResources(), thumbId);
        array.recycle();
    }

    public void setEqualizerMetaData(EqualizerMetaData metaData) {
        short bands = metaData.getBand();
        short maxRange = metaData.getMaxRange();
        short minRange = metaData.getMinRange();
        int[] centerFreq = metaData.getCenterFreq();
        initViews(bands, maxRange, minRange, centerFreq);
        layoutViews(bands);
    }

    public void setOnBandLevelChangeListener(OnBandLevelChangeListener listener) {
        mListener = listener;
    }

    @SuppressLint("SetTextI18n")
    private void initViews(short bands, short maxDB, short minDB, int[] centerFreq) {
        // generate View
        TextView maxDBTextView = new TextView(getContext());
        TextView centerDBTextView = new TextView(getContext());
        TextView minDBTextView = new TextView(getContext());
        VerticalSeekBar[] seekBars = new VerticalSeekBar[bands];
        TextView[] frequencies = new TextView[bands];
        for (int i = 0; i < bands; i++) {
            seekBars[i] = new VerticalSeekBar(getContext());
            frequencies[i] = new TextView(getContext());
        }

        // set LayoutParams
        maxDBTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        maxDBTextView.setId(View.generateViewId());
        centerDBTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        centerDBTextView.setId(View.generateViewId());
        minDBTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        minDBTextView.setId(View.generateViewId());
        for (short i = 0; i < bands; i++) {
            final short band = i;
            seekBars[i].setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            seekBars[i].setId(View.generateViewId());
            seekBars[i].setOnSlideChangeListener(new VerticalSeekBar.SlideChangeListener() {
                @Override
                public void onStart(VerticalSeekBar slideView, int progress) {
                }

                @Override
                public void onProgress(VerticalSeekBar slideView, int progress) {
                    if (mListener != null) {
                        mListener.onBandLevelChange(band, (short) ((progress / 100f) * (maxDB - minDB) + minDB));
                    }
                }

                @Override
                public void onStop(VerticalSeekBar slideView, int progress) {
                }
            });
            seekBars[i].setLayoutParams(new ViewGroup.LayoutParams(dp2px(24), 0));
            frequencies[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            frequencies[i].setId(View.generateViewId());
            frequencies[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        // set values
        maxDBTextView.setText((maxDB / 10) + "dB");
        maxDBTextView.setTextColor(mTextColor);
        centerDBTextView.setText(((maxDB + minDB)) + "dB");
        centerDBTextView.setTextColor(mTextColor);
        minDBTextView.setText((minDB / 10) + "dB");
        minDBTextView.setTextColor(mTextColor);
        for (int i = 0; i < bands; i++) {
            seekBars[i].setSelectColor(mProgressColor);
            frequencies[i].setText((centerFreq[i] / 1000) + "kHz");
            frequencies[i].setTextColor(mTextColor);
        }

        // add in root
        addView(maxDBTextView, 0);
        addView(centerDBTextView, 1);
        addView(minDBTextView, 2);
        for (int i = 0; i < bands; i++) {
            addView(seekBars[i], 3 + 2 * i);
            addView(frequencies[i], 3 + 2 * i + 1);
        }
    }

    private void layoutViews(short bands) {
        // get added view from root
        TextView maxDBTextView = (TextView) getChildAt(0);
        TextView centerDBTextView = (TextView) getChildAt(1);
        TextView minDBTextView = (TextView) getChildAt(2);
        VerticalSeekBar[] seekBars = new VerticalSeekBar[bands];
        TextView[] frequencies = new TextView[bands];
        for (int i = 0; i < bands; i++) {
            seekBars[i] = (VerticalSeekBar) getChildAt(3 + 2 * i);
            frequencies[i] = (TextView) getChildAt(3 + 2 * i + 1);
        }

        // set ConstrainSet
        ConstraintSet set = new ConstraintSet();
        set.clone(this);

        // layout dB Text
        set.connect(maxDBTextView.getId(), ConstraintSet.TOP, getId(), ConstraintSet.TOP, dp2px(16));
        set.connect(maxDBTextView.getId(), ConstraintSet.START, getId(), ConstraintSet.START, dp2px(16));
        set.connect(centerDBTextView.getId(), ConstraintSet.TOP, getId(), ConstraintSet.TOP, dp2px(200 + 16));
        set.connect(centerDBTextView.getId(), ConstraintSet.START, getId(), ConstraintSet.START, dp2px(16));
        set.connect(minDBTextView.getId(), ConstraintSet.TOP, getId(), ConstraintSet.TOP, dp2px(400 + 16));
        set.connect(minDBTextView.getId(), ConstraintSet.START, getId(), ConstraintSet.START, dp2px(16));

        // layout seekBars & frequencies from left to right
        for (int i = 0; i < bands; i++) {
            VerticalSeekBar targetSeekBar = seekBars[i];
            TextView targetFrequency = frequencies[i];
            if (i == bands - 1) {
                set.connect(targetSeekBar.getId(), ConstraintSet.START, seekBars[i - 1].getId(), ConstraintSet.END, 0);
                set.connect(targetSeekBar.getId(), ConstraintSet.END, getId(), ConstraintSet.END, 0);
            } else if (i == 0) {
                set.connect(targetSeekBar.getId(), ConstraintSet.START, getId(), ConstraintSet.START, dp2px(20));
                set.connect(targetSeekBar.getId(), ConstraintSet.END, seekBars[i + 1].getId(), ConstraintSet.START, 0);
            } else {
                set.connect(targetSeekBar.getId(), ConstraintSet.START, seekBars[i - 1].getId(), ConstraintSet.END, 0);
                set.connect(targetSeekBar.getId(), ConstraintSet.END, seekBars[i + 1].getId(), ConstraintSet.START, 0);
            }
            set.connect(targetSeekBar.getId(), ConstraintSet.BOTTOM, minDBTextView.getId(), ConstraintSet.BOTTOM, 0);
            set.connect(targetSeekBar.getId(), ConstraintSet.TOP, maxDBTextView.getId(), ConstraintSet.TOP, 0);

            set.connect(targetFrequency.getId(), ConstraintSet.TOP, targetSeekBar.getId(), ConstraintSet.BOTTOM, dp2px(4));
            set.connect(targetFrequency.getId(), ConstraintSet.START, targetSeekBar.getId(), ConstraintSet.START, 0);
            set.connect(targetFrequency.getId(), ConstraintSet.END, targetSeekBar.getId(), ConstraintSet.END, 0);
            set.connect(targetFrequency.getId(), ConstraintSet.BOTTOM, getId(), ConstraintSet.BOTTOM, dp2px(6));
        }
        set.applyTo(this);
    }

    private int dp2px(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public interface OnBandLevelChangeListener {
        void onBandLevelChange(short band, short level);
    }
}
