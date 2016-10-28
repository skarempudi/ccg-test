package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.model.detail.CreatureDetails;

import java.util.ArrayList;
import java.util.List;

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

    boolean mTapped = false;
    boolean mDeclaredAttacking = false;

    //blocking
    boolean mBlocked = false; //if this is attacking and mBlocked by opposing creatures
    boolean mDeclaredBlocking = false; //if is blocking an attacking opposing creature
    Card mCreatureThatBlocking = null; //if is blocking, then what creature

    public Card(int id) {
        this.id = id;
    }

    /***********************************************************************************************
     * TAPPING
     **********************************************************************************************/

    /**
     * Returns if this card is mTapped, use only if on Battlefield
     * @return If mTapped
     */
    public boolean isTapped() {
        return mTapped;
    }

    /**
     * Tap the card, use only if on Battlefield
     * @return If tap action actually occurred
     */
    public boolean tap() {
        //if already mTapped, tap action doesn't occur
        if (mTapped) {
            MagicLog.d(TAG, "tap: Already mTapped, no action");
            return false;
        }

        mTapped = true;
        return true;
    }

    /**
     * Untap the card, use only if on Battlefield
     * @return If untap action actually occurred
     */
    public boolean untap() {
        //if already untapped, untap action doesn't occur
        if (!mTapped) {
            MagicLog.d(TAG, "untap: Already untapped, no action");
            return false;
        }

        mTapped = false;
        return true;
    }

    /***********************************************************************************************
     * ATTACKING
     **********************************************************************************************/

    /**
     * Returns if this card is declared attacking, use only if on Battlefield
     * @return If attacking
     */
    public boolean isDeclaredAttacking() {
        return mDeclaredAttacking;
    }

    /**
     * Set if declared attacking
     * @param willAttack If will declare attacking or not
     */
    public void declareAttack(boolean willAttack) {
        mDeclaredAttacking = willAttack;
    }

    /***********************************************************************************************
     * BLOCKING
     **********************************************************************************************/

    /**
     * Returns if this card is attacking and blocked by a creature, use only if on Battlefield
     * @return If blocked by another creature
     */
    public boolean isBlocked() {
        return mBlocked;
    }

    /**
     * Set if card is blocked by another creature or not
     * @param blocked If blocked by another creature
     */
    void setBlocked(boolean blocked) {
        mBlocked = blocked;
    }

    /**
     * Returns if this card is declared blocking, use only if on Battlefield
     * @return If attacking
     */
    public boolean isDeclaredBlocking() {
        return mDeclaredBlocking;
    }

    /**
     * Returns if this card is declared blocking the given creature
     * @param attackingCreature The creature that this may be blocking
     * @return If is blocking that creature
     */
    public boolean isBlockingThisCreature(Card attackingCreature) {
        return isDeclaredBlocking() && mCreatureThatBlocking == attackingCreature;
    }

    /**
     * Set if declared blocking, and what creature will block
     * @param willBlock If will declare blocking or not
     * @param creatureThatWillBlock Creature declared blocking. If willBlock is false, can be null,
     *                              will always remove from saved mCreatureThatBlocking.
     */
    public void declareBlock(boolean willBlock, Card creatureThatWillBlock) {
        //if adding as blocker
        if (willBlock) {
            addThisAsBlocker(creatureThatWillBlock);
        }
        //if removing as blocker
        else {
            removeThisAsBlocker();
        }
    }

    /**
     * Add this creature as a blocker for the specified creature
     * @param creatureThatWillBlock Creature that this will block
     */
    private void addThisAsBlocker(Card creatureThatWillBlock) {
        //if no creature to block, returns false and sets mCreatureThatBlocking to null
        if (creatureThatWillBlock == null) {
            removeThisAsBlocker();
            return;
        }

        mDeclaredBlocking = true;
        //save that will be blocking that creature
        mCreatureThatBlocking = creatureThatWillBlock;
    }

    /**
     * Remove this creature as a blocker for mCreatureThatBlocking
     */
    private void removeThisAsBlocker() {
        mDeclaredBlocking = false;
        mCreatureThatBlocking = null;
    }

    /**
     * When confirm blockers, go through every creature defending player controls and set all creatures
     * declared blocked by them to be blocked
     */
    public void onConfirmBlockers() {
        if (mCreatureThatBlocking != null) {
            mCreatureThatBlocking.setBlocked(true);
        }
    }

    /***********************************************************************************************
     * OTHER
     **********************************************************************************************/

    /**
     * Remove card from combat, use only if on Battlefield
     * @return If removal from combat actually occurred, returns false if wasn't in combat
     */
    public boolean removeFromCombat() {
        boolean wasInCombat = false;
        if (isDeclaredAttacking()) {
            wasInCombat = true;
            declareAttack(false);
            setBlocked(false);
        }
        if (isDeclaredBlocking()) {
            wasInCombat = true;
            declareBlock(false, null);
        }
        return wasInCombat;
    }

    @Override
    public String toString() {
        return id + " " + (mTapped ? "mTapped" : "untapped");
    }
}