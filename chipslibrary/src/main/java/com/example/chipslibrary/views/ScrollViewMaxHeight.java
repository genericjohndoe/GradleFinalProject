package com.example.chipslibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

import com.example.chipslibrary.R;
import com.example.chipslibrary.utils.ViewUtil;

/**
 * Created by joeljohnson on 2/1/18.
 */

public class ScrollViewMaxHeight extends NestedScrollView {

    private int mMaxHeight;
    private int mWidthMeasureSpec;

    public ScrollViewMaxHeight(Context context) {
        super(context);
    }

    public ScrollViewMaxHeight(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ScrollViewMaxHeight,
                0, 0);

        try {
            //sets default height of view to 300 dp
            mMaxHeight = a.getDimensionPixelSize(R.styleable.ScrollViewMaxHeight_maxHeight, ViewUtil.dpToPx(300));
        }
        finally {
            a.recycle();
        }
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        measure(mWidthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //sets minWidth to width the root view wants to be
        mWidthMeasureSpec = widthMeasureSpec;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
