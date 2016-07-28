package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

import java.util.ArrayList;

/**
 * Stores the cards in hand for each player.
 * Created by Srikar on 4/27/2016.
 */
public class Hand extends BaseGameZone {
    private static final String TAG = "Hand";

    /**
     * Holds the Cards in Hand. Initial state passed in by MagicApplication calling setCards().
     */
    private final ArrayList<Card>[] mCards;

    /**
     * Data model for each player's hand of cards.
     * Constructed by dependency injection.
     * @param rvEventBus Event bus used to pass information to listening RecyclerViewModels
     * @param gameState Used to determine who the current player is
     * @param gscEventBus Event bus used to listen for changes in gameState
     */
    public Hand(RxEventBus<ListChangeEvent> rvEventBus, GameState gameState,
                RxEventBus<GameStateChangeEvent> gscEventBus) {
        super(rvEventBus, gameState, gscEventBus);

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

    @Override
    protected void clearLists() {
        mCards[DataModelConstants.PLAYER_ALICE].clear();
        mCards[DataModelConstants.PLAYER_BOB].clear();
    }

    /***********************************************************************************************
     * GAME STATE CHANGE EVENT BUS
     * Listen for changes to GameState
     **********************************************************************************************/
    /**
     * Act on changes to GameState
     * Doesn't do anything yet, but will draw a card at draw step later
     * @param event GameState change event
     */
    @Override
    protected void actOnGameStateChangeEvent(GameStateChangeEvent event) {

    }
}
