package com.icerrate.bakingapp.utils;

import android.content.res.Resources;

/**
 * @author Ivan Cerrate.
 */

public class MeasureUtils {

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}