package com.example.srikar.magic.model;

import android.util.Log;
import android.view.View;

/**
 * Created by Srikar on 4/15/2016.
 */
public class Card {
    private static final String TAG = "Card";
    public int mId;

    public Card(int id) {
        mId = id;
    }

    public void onClick() {
        Log.d(TAG, "onClick: " + mId);
    }
}