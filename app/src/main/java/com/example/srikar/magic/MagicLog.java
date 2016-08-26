package com.example.srikar.magic;

import android.util.Log;

/**
 * Wrapper for Log such that can turn off logging for non-Android tests using setLogging()
 * Created by Srikar on 7/12/2016.
 */
public class MagicLog {
    public static boolean LOG_ENABLED = true;

    /**
     * Used to turn off or on logging
     * @param enabled If true, logging enabled, else disabled
     */
    public static void setLogging(boolean enabled) {
        LOG_ENABLED = enabled;
    }

    /**
     * Calls Log.d(), but checks if logs are enabled.
     * @param tag Passed into Log.d()
     * @param msg Passed into Log.d()
     */
    public static void d(String tag, String msg) {
        if (LOG_ENABLED) {
            Log.d(tag, msg);
        }
    }
}
