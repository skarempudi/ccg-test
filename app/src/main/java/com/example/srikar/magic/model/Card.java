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
    private boolean declaredAttacking = false;

    public Card(int id) {
        this.id = id;
    }

    /**
     * Returns if this card is tapped, use only if on Battlefield
     * @return If tapped
     */
    public boolean isTapped() {
        return tapped;
    }

    /**
     * Returns if this card is declared attacking, use only if on Battlefield
     * @return If attacking
     */
    public boolean isDeclaredAttacking() {
        return declaredAttacking;
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

    /**
     * Set if declared attacking
     * @param willAttack If will declare attacking or not
     */
    public void declareAttack(boolean willAttack) {
        declaredAttacking = willAttack;
    }

    @Override
    public String toString() {
        return id + " " + (tapped? "tapped" : "untapped");
    }
}