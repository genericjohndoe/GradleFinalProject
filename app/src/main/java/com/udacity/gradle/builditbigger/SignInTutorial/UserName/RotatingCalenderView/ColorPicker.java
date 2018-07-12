package com.udacity.gradle.builditbigger.SignInTutorial.UserName.RotatingCalenderView;

import android.view.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.udacity.gradle.builditbigger.R;


public abstract class ColorPicker extends View {


    protected Paint mColorPaint;
    protected Paint mHandlePaint, mHandleStrokePaint;
    private int mHandleStrokeColor;

    protected float mHandleSize, mTouchSize;

    protected float mHue, mSat, mVal; // HSV color values

    protected float mHalfWidth, mHalfHeight;

    protected boolean mDragging; // whether handle is being dragged

    protected static final int[] COLORS = new int[] { 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000 };

    public ColorPicker(Context context){
        super(context);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyle, int stuff){
        super(context, attrs,defStyle, stuff);
    }

    protected void init() {
        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mHandlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHandlePaint.setColor(getColorFromHSV(mHue, mSat, mVal));

        mHandleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHandleStrokePaint.setStyle(Paint.Style.STROKE);
        mHandleStrokePaint.setColor(mHandleStrokeColor);
        mHandleStrokePaint.setStrokeWidth(4);
    }

    protected void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPicker);

        try {
            mHandleStrokeColor = a.getColor(R.styleable.ColorPicker_handleStrokeColor, Color.WHITE);
            mHue = normalizeAngle(a.getFloat(R.styleable.ColorPicker_hue, 0));
            mSat = clamp(a.getFloat(R.styleable.ColorPicker_saturation, 1), 0, 1);
            mVal = clamp(a.getFloat(R.styleable.ColorPicker_value, 1), 0, 1);

            // TODO add to XML attributes
            mHandleSize = getResources().getDimensionPixelSize(R.dimen.default_handleSize);
            mTouchSize = getResources().getDimensionPixelSize(R.dimen.default_touchSize);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected abstract void onDraw(Canvas canvas);

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        mHalfWidth = w / 2f;
        mHalfHeight = h / 2f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        getLocationOnScreen(location);

        float x = event.getRawX() - location[0];
        float y = event.getRawY() - location[1];

        handleTouch(event.getAction(), x, y);
        return true;
    }

    protected abstract void handleTouch(int motionAction, float x, float y);

    protected abstract void moveHandleTo(float x, float y);
    protected abstract void animateHandleTo(float x, float y);

    /**
     * Get view maximum padding
     * @return maximum padding
     */
    protected int getMaxPadding() {
        return Math.max(Math.max(getPaddingLeft(), getPaddingRight()), Math.max(getPaddingTop(), getPaddingBottom()));
    }

    /*
    SETTERS/GETTERS
     */

    /**
     * Set new picker color
     * @param color new picker color
     */
    public abstract void setColor(int color);

    /**
     * Get current picker color
     * @return current color
     */
    public int getColor() {
        return mHandlePaint.getColor();
    }

    /**
     * Set handle stroke color
     * @param color new handle stroke color
     */
    public void setHandleStrokeColor(int color) {
        mHandleStrokeColor = color;
        mHandleStrokePaint.setColor(mHandleStrokeColor);
        invalidate();
    }

    /**
     * Get handle stroke color
     * @return current handle stroke color
     */
    public int getHandleStrokeColor() {
        return mHandleStrokeColor;
    }

    public static float normalizeAngle(float deg) {
        if (deg > 360) deg -= 360;
        else if (deg < 0) deg += 360;
        return deg;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int getColorFromHSV(float angle, float saturation, float value) {
        angle = normalizeAngle(angle);
        return Color.HSVToColor(new float[]{angle, saturation, value});
    }


}
