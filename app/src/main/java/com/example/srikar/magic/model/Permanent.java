package com.example.srikar.magic.model;

import android.util.Log;

/**
 * Used on the Battlefield
 * If a Permanent is not a Token, then it has a Card.
 * Created by Srikar on 5/18/2016.
 */
public class Permanent {
    private static final String TAG = "Permanent";
    private Card mCard;

    public Permanent(Card card) {
        mCard = card;
    }

    public void onClick() {
        Log.d(TAG, "onClick: " + mCard.mId);
    }

    @Override
    public String toString() {
        return new String("" + mCard.mId);
    }
}
