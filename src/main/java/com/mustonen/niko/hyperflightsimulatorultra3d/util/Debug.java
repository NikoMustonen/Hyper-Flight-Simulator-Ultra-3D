package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Class for debugging.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class Debug {

    /**
     * Defines whether debugging is on.
     */
    public static final boolean ON = true;

    /**
     * Stores default debugging tag.
     */
    private static String DEFAULT_DEBUG_TAG = "debuggaus";

    /**
     * Generates debug print.
     *
     * @param tag     If null uses default tag.
     * @param message Debug message.
     */
    public static final void debug(@Nullable String tag, String message) {
        if (ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.d(TAG, message);
        }
    }

    /**
     * Generates debug warning print.
     *
     * @param tag     If null uses default tag.
     * @param message Debug message.
     */
    public static final void warning(@Nullable String tag, String message) {
        if (ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.w(TAG, message);
        }
    }

    /**
     * Generates debug exception print.
     *
     * @param tag If null uses default tag.
     * @param e   Exception to be printed.
     */
    public static final void exception(@Nullable String tag, Exception e) {
        if (ON) {
            final String TAG = tag == null ? DEFAULT_DEBUG_TAG : tag;
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
