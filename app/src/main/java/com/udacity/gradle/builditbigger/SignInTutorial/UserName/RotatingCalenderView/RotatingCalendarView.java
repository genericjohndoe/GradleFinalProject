package com.udacity.gradle.builditbigger.SignInTutorial.UserName.RotatingCalenderView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import com.udacity.gradle.builditbigger.R;

public class RotatingCalendarView extends View {

    Paint ringPaint;
    //red, yellow, green, light blue, blue, light purple, red
    final int[] COLORS = new int[] { 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000 };
    final int[] COLORS2 = new int[]{Color.parseColor("#33004c"), Color.parseColor("#4600d2"),
                                    Color.parseColor("#0000ff"), Color.parseColor("#0099ff"),
                                    Color.parseColor("#00eeff"),Color.parseColor("#00FF7F"),
                                    Color.parseColor("#48FF00"),Color.parseColor("#B6FF00"),
                                    Color.parseColor("#FFD700"),Color.parseColor("#ff9500"),
                                    Color.parseColor("#FF6200"),Color.parseColor("#FF0000"),
                                    Color.parseColor("#33004c")};
    final float[] postions = new float[]{0.0417f,0.083f,0.083f,0.083f,0.083f,0.083f,0.083f,0.083f,0.083f,
            0.083f,0.083f,0.083f,0.0417f};


    public RotatingCalendarView(Context context){
        super(context);
        init();
    }

    public RotatingCalendarView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    public RotatingCalendarView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    public RotatingCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int stuff){
        super(context, attrs, defStyleAttr, stuff);
        init();
    }

    public void init(){
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(8f);
        //ringPaint.setShader(new SweepGradient(0, 0, COLORS2, null));
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth()/2f,getHeight()/2f ,getHeight()/2f - 16f,ringPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ringPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, COLORS2, null));
    }

    public static int[] getHueRingColors(int n, float saturation, float value) {
        int[] c = new int[n];

        for (int i = 0; i < n; i++)
            c[i] = getColorFromHSV(i / (n - 1f) * 360f, saturation, value);
        return c;
    }

    public static int getColorFromHSV(float angle, float saturation, float value) {
        angle = normalizeAngle(angle);
        return Color.HSVToColor(new float[]{angle, saturation, value});
    }

    public static float normalizeAngle(float deg) {
        if (deg > 360) deg -= 360;
        else if (deg < 0) deg += 360;
        return deg;
    }
}
