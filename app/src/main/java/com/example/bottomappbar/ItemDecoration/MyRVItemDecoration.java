package com.example.bottomappbar.ItemDecoration;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom RecyclerView item decoration which customises the position/index where it is drawn
 * - Currently, it is set up such that the divider is not drawn for the first position, and the last two position
 */

public class MyRVItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public MyRVItemDecoration(Drawable divider) {
        mDivider = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        // Set up such that divider is not drawn for the first position, and the last two position
        for (int i = 1; i <= childCount - 3; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }
}
