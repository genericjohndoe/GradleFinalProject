package com.udacity.gradle.builditbigger.SignInTutorial.UserName.RotatingCalenderView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
    float rotation;
    final int daysInYear = 365;
    final int daysInLeapYear = 366;
    int year;
    int day;
    float oldX = 0;
    float oldY = 0;


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
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, getHeight()/2f ,getHeight()/2f);
        canvas.drawCircle(getWidth()/2f,getHeight()/2f ,getHeight()/2f - 16f,ringPaint);
        canvas.restore();
        Log.i("rotation", ""+rotation);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ringPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, COLORS2, null));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i("movement", event.toString());

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            oldX = event.getX();
            oldY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            //
            float x = event.getX();
            float y = event.getY();
            updateRotation(x,y);
            invalidate();
        }
        return true;
    }
    private void updateRotation(float x, float y) {
        double r = Math.atan2(x - getHeight()/2f, getWidth()/2f - y);
        rotation = (float) Math.toDegrees(r);
        day = (int) ((year %4==0) ? rotation * daysInLeapYear/360 : rotation *daysInYear/360);
    }

}
