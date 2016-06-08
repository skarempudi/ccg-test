package com.example.srikar.magic.model;

import android.util.Log;

import com.example.srikar.magic.model.Card;

import java.util.ArrayList;

/**
 * Created by Srikar on 4/27/2016.
 */
public class Hand {
    private static final String TAG = "Hand";

    private static final int CARD_COUNT = 7;
    private final ArrayList<Card> mCards;

    /**
     * Currently just creates 7 cards on creation
     */
    public Hand() {
        mCards = new ArrayList<>();
        for (int i = 0; i < CARD_COUNT; i++) {
            mCards.add(new Card(i));
        }
    }

    /**
     * Used to determine which OnClick to use, based on Card selected
     * @param pos
     * @return
     */
    public Card getCard(int pos) {
        return mCards.get(pos);
    }

    /**
     * Add card to mHand, used to test if really singleton
     * @param cardID
     */
    public void addCard(int cardID) {
        mCards.add(new Card(cardID));
    }

    /**
     * Used by HandViewAdapter to determine how many entries are in the RecyclerView
     * @return
     */
    public int getHandSize() {
        return mCards.size();
    }

}
