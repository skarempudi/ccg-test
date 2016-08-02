package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.model.detail.CreatureDetails;

/**
 * Data model for Cards in Hand
 * Created by Srikar on 4/15/2016.
 */
public class Card {
    private static final String TAG = "Card";
    //maps to JSON "id"
    public final int id;

    //for now, defaults to 3/3
    public CreatureDetails details;

    private boolean tapped = false;

    public Card(int id) {
        this.id = id;
    }

    /**
     * Returns if this card is tapped, use only if on Battlefield
     * @return if tapped
     */
    public boolean isTapped() {
        return tapped;
    }

    /**
     * Tap the card, use only if on Battlefield
     * @return If tap action actually occurred
     */
    public boolean tap() {
        //if already tapped, tap action doesn't occur
        if (tapped) {
            MagicLog.d(TAG, "tap: Already tapped, no action");
            return false;
        }

        tapped = true;
        return true;
    }

    /**
     * Untap the card, use only if on Battlefield
     * @return If untap action actually occurred
     */
    public boolean untap() {
        //if already untapped, untap action doesn't occur
        if (!tapped) {
            MagicLog.d(TAG, "untap: Already untapped, no action");
            return false;
        }

        tapped = false;
        return true;
    }

    public void onClick() {
        MagicLog.d(TAG, "onClick: " + id);
    }

    @Override
    public String toString() {
        return id + " " + (tapped? "tapped" : "untapped");
    }
}