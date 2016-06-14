package com.example.srikar.magic.model;

import android.util.Log;

import com.example.srikar.magic.model.Card;

import java.util.ArrayList;

/**
 * Stores the cards in hand for each player
 * Created by Srikar on 4/27/2016.
 */
public class Hand {
    private static final String TAG = "Hand";

    private final GameState mGameState;

    private static final int CARD_COUNT = 7;
    private final ArrayList<Card>[] mCards;

    /**
     * Currently just creates 7 cards on creation
     */
    public Hand(GameState state) {
        mGameState = state;

        mCards = new ArrayList[2];
        mCards[PlayerID.ALICE] = new ArrayList<>();
        mCards[PlayerID.BOB] = new ArrayList<>();

        //default values
        for (int i = 0; i < CARD_COUNT; i++) {
            mCards[PlayerID.ALICE].add(new Card(i));
        }
    }

    /**
     * Used to determine which OnClick to use, based on Card selected
     * Only see the hand of player that view focused on
     * @param pos Position in the list that user touches
     * @return Card
     */
    public Card getCard(int pos) {
        return mCards[mGameState.getViewPlayer()].get(pos);
    }

    /**
     * Used by HandViewAdapter to determine how many entries are in the RecyclerView
     * Only see the hand of player that view focused on
     * @return Size of list
     */
    public int getHandSize() {
        return mCards[mGameState.getViewPlayer()].size();
    }

}
