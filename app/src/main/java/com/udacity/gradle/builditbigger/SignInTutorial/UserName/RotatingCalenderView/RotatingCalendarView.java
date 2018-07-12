package com.udacity.gradle.builditbigger.SignInTutorial.UserName.RotatingCalenderView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureListener());
    float rotation;


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
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            //Log.i("movement", "move action logged");
            //calculate distance away from center, further away = less fricition
            //get center point
            //find angle between point A, center, and point B
            //divide by unit time to get angular velo
            //rotate canvas at that rate
            float x = event.getX();
            float y = event.getY();
            updateRotation(x,y);
            invalidate();
        }
        return true;
    }
    private void updateRotation(float x, float y) {

        double r = Math.atan2(x - getHeight()/2f, getWidth()/2f - y);
        rotation = (int) Math.toDegrees(r);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("movement", "move action logged");
            return true;
        }
    }
}
