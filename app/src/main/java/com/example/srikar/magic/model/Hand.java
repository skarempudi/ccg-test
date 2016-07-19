package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

import java.util.ArrayList;

/**
 * Stores the cards in hand for each player
 * Created by Srikar on 4/27/2016.
 */
public class Hand {
    private static final String TAG = "Hand";

    private final GameState mGameState;

    /**
     * Used to signal to RecyclerViews for below ArrayLists that lists updated
     * Injected during construction
     */
    private final RxEventBus<ListChangeEvent> mListChangeEventBus;
    private final ArrayList<Card>[] mCards;

    /**
     * Initial state passed in by MagicApplication
     */
    public Hand(RxEventBus<ListChangeEvent> rvEventBus, GameState state) {
        mListChangeEventBus = rvEventBus;
        mGameState = state;

        mCards = new ArrayList[2];
        mCards[DataModelConstants.PLAYER_ALICE] = new ArrayList<>();
        mCards[DataModelConstants.PLAYER_BOB] = new ArrayList<>();
    }

    /**
     * Used to initialize the set of Cards
     * @param playerID Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @param cards Cards
     */
    public void setCards(int playerID, ArrayList<Card> cards) {
        MagicLog.d(TAG, "setCards: " + cards.toString() + " for " + playerID);
        mCards[playerID] = cards;

        //update all in RecyclerView for Hand
        addListChangeEvent(DataModelConstants.LIST_HAND, ListChangeEvent.UPDATE_ALL, 0);
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
     * Used by HandRecViewAdapter to determine how many entries are in the RecyclerView
     * Only see the hand of player that view focused on
     * @return Size of list
     */
    public int getHandSize() {
        return mCards[mGameState.getViewPlayer()].size();
    }

    /***********************************************************************************************
     * EVENT BUS
     * Listeners access through Dagger injection for bus, not with getter.
     **********************************************************************************************/
    /**
     * Add event to mListChangeEventBus, which alerts the specified RecyclerView to update
     * @param listName Which RecyclerView, using DataModelConstants list names
     * @param action Whether to add, remove, or update, using ListChangeEvent actions
     * @param index What index to update in list
     */
    private void addListChangeEvent(int listName, int action, int index) {
        ListChangeEvent event = new ListChangeEvent(listName, action, index);
        MagicLog.d(TAG, "addListChangeEvent: " + event.toString());

        mListChangeEventBus.addEvent(event);
    }

}
