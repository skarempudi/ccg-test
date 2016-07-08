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

    private boolean tapped = false;

    public Permanent(Card card) {
        mCard = card;
    }

    /**
     * Returns if this Permanent is tapped
     * @return if tapped
     */
    public boolean isTapped() {
        return tapped;
    }

    /**
     * Tap the permanent. Returns false if already tapped.
     * Some cards may care if try to tap already tapped Permanent.
     * @return If tap action succeeded
     */
    public boolean tap() {
        //if already tapped, tap action doesn't occur
        if (tapped) {
            Log.d(TAG, "tap: Already tapped, no action");
            return false;
        }

        tapped = true;
        return true;
    }

    /**
     * Untap the permanent. Returns false if already untapped.
     * Some cards may care if try to untap already untapped Permanent.
     * @return If untap action succeeded
     */
    public boolean untap() {
        //if already untapped, untap action doesn't occur
        if (!tapped) {
            Log.d(TAG, "untap: Already untapped, no action");
            return false;
        }

        tapped = false;
        return true;
    }

    public void onClick() {
        Log.d(TAG, "onClick: " + mCard.mId);
    }

    @Override
    public String toString() {
        return mCard.mId + " " + (tapped? "tapped" : "untapped");
    }
}
