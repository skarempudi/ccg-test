package com.example.srikar.magic.model;

import android.util.Log;
import android.view.View;

import com.example.srikar.magic.AppConstants;
import com.example.srikar.magic.MagicLog;

/**
 * Data model for Cards in Hand
 * Created by Srikar on 4/15/2016.
 */
public class Card {
    private static final String TAG = "Card";
    public final int mId;

    public Card(int id) {
        mId = id;
    }

    public void onClick() {
        MagicLog.d(TAG, "onClick: " + mId);
    }
}