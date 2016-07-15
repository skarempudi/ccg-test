package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;

/**
 * Used on the Battlefield
 * If a Permanent is not a Token, then it has a Card.
 * Created by Srikar on 5/18/2016.
 */
public class Permanent {
    private static final String TAG = "Permanent";
    private final Card mCard;

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
     * Tap the permanent.
     */
    public void tap() {
        //if already tapped, tap action doesn't occur
        if (tapped) {
            MagicLog.d(TAG, "tap: Already tapped, no action");
            return;
        }

        tapped = true;
    }

    /**
     * Untap the permanent.
     */
    public void untap() {
        //if already untapped, untap action doesn't occur
        if (!tapped) {
            MagicLog.d(TAG, "untap: Already untapped, no action");
            return;
        }

        tapped = false;
    }

    public void onClick() {
        MagicLog.d(TAG, "onClick: " + toString());
    }

    @Override
    public String toString() {
        return mCard.mId + " " + (tapped? "tapped" : "untapped");
    }
}
