package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.support.annotation.Nullable;
import android.util.Log;

public class Debug {

    public static final boolean ON = true;
    private static String DEFAULT_DEBUG_TAG = "debug";

    public static final void debug(@Nullable String tag, String message) {
        if(ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.d(TAG, message);
        }
    }

    public static final void warning(@Nullable String tag, String message) {
        if(ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.w(TAG, message);
        }
    }

    public static final void exception(@Nullable String tag, Exception e) {
        if(ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
