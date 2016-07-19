package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;

/**
 * Data model for Cards in Hand
 * Created by Srikar on 4/15/2016.
 */
public class Card {
    private static final String TAG = "Card";
    //maps to JSON "id"
    public final int id;

    public Card(int id) {
        this.id = id;
    }

    public void onClick() {
        MagicLog.d(TAG, "onClick: " + id);
    }

    @Override
    public String toString() {
        return "" + id;
    }
}