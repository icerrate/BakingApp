package com.icerrate.bakingapp.view.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.icerrate.bakingapp.utils.MeasureUtils;

/**
 * @author Ivan Cerrate.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    private final int horizontalSpaceWidth;

    public VerticalSpaceItemDecoration(int horizontalSpaceWidth, int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpaceWidth = horizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int verticalSpacePx = MeasureUtils.dpToPx(verticalSpaceHeight);
        int horizontalSpacePx = MeasureUtils.dpToPx(horizontalSpaceWidth);
        outRect.top = verticalSpacePx;
        outRect.left = horizontalSpacePx;
        outRect.right = horizontalSpacePx;
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = verticalSpacePx;
        }
    }
}