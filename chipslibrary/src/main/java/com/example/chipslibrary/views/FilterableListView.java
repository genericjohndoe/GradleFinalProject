package com.example.chipslibrary.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.RelativeLayout;

import com.example.chipslibrary.ChipsInput;
import com.example.chipslibrary.R;
import com.example.chipslibrary.adapters.FilterableAdapter;
import com.example.chipslibrary.databinding.ListFilterableViewBinding;
import com.example.chipslibrary.models.ChipInterface;
import com.example.chipslibrary.utils.ViewUtil;

import java.util.List;

/**
 * for other recyclerview, should be deleted later
 */

public class FilterableListView extends RelativeLayout {

    private static final String TAG = FilterableListView.class.toString();
    private Context mContext;
    // list
    private ListFilterableViewBinding bind;
    private FilterableAdapter mAdapter;
    private List<? extends ChipInterface> mFilterableList;
    // others
    private ChipsInput mChipsInput;

    public FilterableListView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        // inflate layout
        bind = DataBindingUtil.bind(inflate(getContext(), R.layout.list_filterable_view, this));

        // butter knife

        // recycler
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        // hide on first
        setVisibility(GONE);
    }

    public void build(List<? extends ChipInterface> filterableList, ChipsInput chipsInput, ColorStateList backgroundColor, ColorStateList textColor) {
        mFilterableList = filterableList;
        mChipsInput = chipsInput;

        // adapter
        mAdapter = new FilterableAdapter(mContext, bind.recyclerView, filterableList, chipsInput, backgroundColor, textColor);
        bind.recyclerView.setAdapter(mAdapter);
        if(backgroundColor != null)
            bind.recyclerView.getBackground().setColorFilter(backgroundColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);

        // listen to change in the tree
        mChipsInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                // position
                ViewGroup rootView = (ViewGroup) mChipsInput.getRootView();

                // size
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewUtil.getWindowWidth(mContext),
                        ViewGroup.LayoutParams.MATCH_PARENT);

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    layoutParams.bottomMargin = ViewUtil.getNavBarHeight(mContext);
                }


                // add view
                rootView.addView(FilterableListView.this, layoutParams);

                // remove the listener:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mChipsInput.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mChipsInput.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }

        });
    }

    public void filterList(CharSequence text) {
        mAdapter.getFilter().filter(text, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                // show if there are results
                if(mAdapter.getItemCount() > 0)
                    fadeIn();
                else
                    fadeOut();
            }
        });
    }

    /**
     * Fade in
     */
    public void fadeIn() {
        if(getVisibility() == VISIBLE)
            return;

        // get visible window (keyboard shown)
        final View rootView = getRootView();
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);

        int[] coord = new int[2];
        mChipsInput.getLocationInWindow(coord);
        ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = coord[1] + mChipsInput.getHeight();
        // height of the keyboard
        layoutParams.bottomMargin = rootView.getHeight() - r.bottom;
        setLayoutParams(layoutParams);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(VISIBLE);
    }

    /**
     * Fade out
     */
    public void fadeOut() {
        if(getVisibility() == GONE)
            return;

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(GONE);
    }
}
