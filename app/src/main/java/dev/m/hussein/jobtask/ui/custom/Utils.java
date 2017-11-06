package dev.m.hussein.jobtask.ui.custom;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by bruce on 14-11-6.
 */
public class Utils {
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    private static float getPixelScaleFactor(Resources resources) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
