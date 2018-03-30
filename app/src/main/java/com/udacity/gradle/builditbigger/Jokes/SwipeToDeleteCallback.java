package com.udacity.gradle.builditbigger.Jokes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.udacity.gradle.builditbigger.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

/**
 * Created by joeljohnson on 3/30/18.
 */

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private final Drawable deleteIcon;
    private final int intrinsicWidth;
    private final int intrinsicHeight;
    private final ColorDrawable background;
    private final int backgroundColor;
    private final Paint clearPaint;

    public int getMovementFlags(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null) {
            if (viewHolder.getAdapterPosition() == 10) return 0;
        }
        return super.getMovementFlags(recyclerView, viewHolder);
    }

    public boolean onMove(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder target) {
        return false;
    }

    public void onChildDraw(@Nullable Canvas c, @Nullable RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom() - itemView.getTop();
        boolean isCanceled = dX == 0.0F && !isCurrentlyActive;
        if (isCanceled) {
            this.clearCanvas(c, (float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        } else {
            this.background.setColor(this.backgroundColor);
            this.background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            this.background.draw(c);
            int deleteIconTop = itemView.getTop() + (itemHeight - this.intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - this.intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - this.intrinsicWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + this.intrinsicHeight;
            Drawable var10000 = this.deleteIcon;
            if (this.deleteIcon != null) {
                var10000.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            }

            var10000 = this.deleteIcon;
            if (this.deleteIcon != null) {
                var10000.draw(c);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private final void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        if (c != null) c.drawRect(left, top, right, bottom, this.clearPaint);
    }

    public SwipeToDeleteCallback(@NotNull Context context) {
        super(ItemTouchHelper.START, ItemTouchHelper.START);
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_black_24dp);
        Drawable var10001 = this.deleteIcon;
        if (this.deleteIcon == null) {
            Intrinsics.throwNpe();
        }

        this.intrinsicWidth = var10001.getIntrinsicWidth();
        var10001 = this.deleteIcon;
        if (this.deleteIcon == null) {
            Intrinsics.throwNpe();
        }
        this.intrinsicHeight = var10001.getIntrinsicHeight();
        this.background = new ColorDrawable();
        this.backgroundColor = Color.parseColor("#f44336");
        Paint var2 = new Paint();
        var2.setXfermode((Xfermode) (new PorterDuffXfermode(PorterDuff.Mode.CLEAR)));
        this.clearPaint = var2;
    }
}