package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

public class OverlayWithHoleImageView extends AppCompatImageView {
    private RectF circleRect;
    private int radius;
    Paint paint;

    public OverlayWithHoleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //circleRect = new RectF();
    }

    public void setCircle(RectF rect, int radius) {
        this.circleRect = rect;
        this.radius = radius;
        //Redraw after defining circle
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(circleRect != null) {
            canvas.drawPaint(paint);
            canvas.drawRoundRect(circleRect, radius, radius, paint);
        }
    }
}
