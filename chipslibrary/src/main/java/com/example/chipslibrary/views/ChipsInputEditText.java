package com.example.chipslibrary.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by joeljohnson on 2/1/18.
 */

public class ChipsInputEditText extends androidx.appcompat.widget.AppCompatEditText {
    //todo should make edittext wrap contents by default
    private FilterableListView filterableListView;

    public ChipsInputEditText(Context context) {
        super(context);
    }

    public ChipsInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isFilterableListVisible() {
        return filterableListView != null && filterableListView.getVisibility() == VISIBLE;
    }

    public FilterableListView getFilterableListView() {
        return filterableListView;
    }

    public void setFilterableListView(FilterableListView filterableListView) {
        this.filterableListView = filterableListView;
    }
}
