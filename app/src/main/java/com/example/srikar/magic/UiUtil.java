package com.example.srikar.magic;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Holder for constants related to UI interactions.
 * Created by Srikar on 7/7/2016.
 */
public final class UiUtil {
    public static final int CLICK_DELAY = 500;

    //to prevent from creating an instance
    private UiUtil() {}

    /**
     * Wrapper for Html.fromHtml() that handles deprecation.
     * @param partial String to be formatted
     * @return Formatted version of given String
     */
    public static Spanned formatHTML(String partial) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(partial, Html.FROM_HTML_MODE_LEGACY);
        }
        else {
            return Html.fromHtml(partial);
        }
    }
}
