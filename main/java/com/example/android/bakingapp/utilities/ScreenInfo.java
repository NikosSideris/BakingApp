package com.example.android.bakingapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Created by Nikos on 05/03/18.
 */
public class ScreenInfo {

    private Context mContext;

    private String displayName;
    private int width;
    private int height;
    private int heightPixels;
    private int widthPixels;
    private int densityDpi;
    private float xdpi;
    private float ydpi;
    private int orientation;

    private static final int TABLET_THRESHOLD=720;

    public ScreenInfo(Context mContext) {
        this.mContext = mContext;
        getInfo();
    }
    private void getInfo(){
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        displayName = display.getName();  // minSdkVersion=17+
        String TAG = "ScreenInfo";
        Log.i(TAG, "displayName  = " + displayName);

// display size in pixels
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Log.i(TAG, "width        = " + width);
        Log.i(TAG, "height       = " + height);

// pixels, dpi
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        heightPixels = metrics.heightPixels;
        widthPixels = metrics.widthPixels;
        densityDpi = metrics.densityDpi;
        xdpi = metrics.xdpi;
        ydpi = metrics.ydpi;
        Log.i(TAG, "widthPixels  = " + widthPixels);
        Log.i(TAG, "heightPixels = " + heightPixels);
        Log.i(TAG, "densityDpi   = " + densityDpi);
        Log.i(TAG, "xdpi         = " + xdpi);
        Log.i(TAG, "ydpi         = " + ydpi);

// orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
        orientation = mContext.getResources().getConfiguration().orientation;
        Log.i(TAG, "orientation  = " + orientation);
    }

    public boolean isTablet(){
        int dim;
        if (widthPixels<heightPixels){dim=widthPixels;}else{dim=heightPixels;}
        if (dim>TABLET_THRESHOLD){return true;}
        return false;
    }

    public boolean inPortraitMode(){
        if (orientation==1){ return true;}
        return false;
    }

    public boolean inLandscapeMode(){
        if (orientation==2){ return true;}
        return false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public float getXdpi() {
        return xdpi;
    }

    public float getYdpi() {
        return ydpi;
    }

    public int getOrientation() {
        return orientation;
    }
}
