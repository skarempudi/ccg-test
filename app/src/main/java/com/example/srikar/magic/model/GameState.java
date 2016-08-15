package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.model.zone.Hand;

/**
 * Class used to keep track of the general game state, such as whose turn it is, the life totals,
 * and if the game has ended.
 * Created by Srikar on 4/26/2016.
 */
public class GameState {
    private static final String TAG = "GameState";
    private static final int STARTING_LIFE = 20;

    /**
     * Used to signal to BoardFragmentModel that game state changed
     */
    private final RxEventBus<GameStateChangeEvent> mGameStateChangeEventBus;
    /**
     * Not injected, due to dependency chain. Depend on GameState.
     */
    private Hand mHand;
    private Battlefield mBattlefield;

    private int mCurrentPlayer; //the player whose turn it is
    private int mViewPlayer; //the player whose view is currently being used

    private int mTurn; //what turn it currently is
    private int mStep; //what step in the turn it currently is

    private int[] lifeTotals = {STARTING_LIFE, STARTING_LIFE};

    public GameState(RxEventBus<GameStateChangeEvent> rxEventBus) {
        mGameStateChangeEventBus = rxEventBus;

        mCurrentPlayer = DataModelConstants.PLAYER_ALICE; //starting player is Alice
        mViewPlayer = DataModelConstants.PLAYER_ALICE;

        mTurn = 1; //first turn is 1
        mStep = DataModelConstants.STEP_UNTAP; //first step is untap
    }

    /**
     * Called by Hand constructor to create linkage
     * Not done through dependency injection because Hand depends on GameState
     * @param hand Hand singleton
     */
    public void setHand(Hand hand) {
        mHand = hand;
    }

    /**
     * Called by Battlefield constructor to create linkage
     * Not done through dependency injection because Battlefield depends on GameState
     * @param battlefield Battlefield singleton
     */
    public void setBattlefield(Battlefield battlefield) {
        mBattlefield = battlefield;
    }

    /**
     * Returns ID of player whose turn it is
     * @return Player ID
     */
    public int getCurrentPlayer() {
        return mCurrentPlayer;
    }

    /**
     * When one player's turn ends, switch to the other
     * Also changes the view to that player
     */
    public void switchCurrentPlayer() {
        mCurrentPlayer ^= 1; //swaps 0 to 1, and 1 to 0
        mViewPlayer = mCurrentPlayer;
    }

    /**
     * Returns what player the view is focusing on
     * @return Player ID
     */
    public int getViewPlayer() {
        return mViewPlayer;
    }

    /**
     * Switches display to match the other player
     */
    public void switchViewPlayer() {
        mViewPlayer ^= 1; //swaps 0 to 1, and 1 to 0
        //alert listening BoardFragmentModel that view player switched
        addGameStateChangeEvent(GameStateChangeEvent.SWITCH_VIEW_PLAYER);
    }

    /**
     * Return ID of the other player
     * @return Player ID
     */
    public int getOtherPlayer() {
        return mCurrentPlayer ^ 1;
    }

    /**
     * Return ID of player that view not focused on
     * @return Player ID
     */
    public int getOtherViewPlayer() {
        return mViewPlayer ^ 1;
    }

    /**
     * Get the current turn number, which is incremented by going through all steps in a turn
     * @return Turn number
     */
    public int getTurnNumber() {
        return mTurn;
    }

    /**
     * Goes to the next step in the turn.
     * When it reaches the end of the steps, it goes to the next player
     */
    public void nextStep() {
        //next step will be in same turn
        if (mStep < DataModelConstants.STEP_END) {
            mStep++;

            //add to event bus
            addGameStateChangeEvent(GameStateChangeEvent.NEXT_STEP);
        }
        //next step will be in next turn
        else {
            nextTurn();
        }
    }

    /**
     * Start the next turn
     */
    private void nextTurn() {
        //next turn
        mTurn++;
        //go to untap step
        mStep = DataModelConstants.STEP_UNTAP;
        //switch current player and change view player to it
        switchCurrentPlayer();

        //have permanents untap for now current player
        mBattlefield.onUntapStep();

        //alert listening BoardFragmentModel to handle change in turn
        addGameStateChangeEvent(GameStateChangeEvent.NEXT_TURN);
    }

    /**
     * Get the current step, returning a value from DataModelConstants (STEP_)
     * @return The current step, as an int
     */
    public int getCurrentStep() {
        return mStep;
    }

    /**
     * Get the current life total for the given player
     * @param playerID Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @return Life total
     */
    public int getLifeTotal(int playerID) {
        return lifeTotals[playerID];
    }

    /***********************************************************************************************
     * EVENT BUS
     * Listeners access through Dagger injection for bus, not with getter.
     **********************************************************************************************/
    /**
     * Add event to mGameStateChangeEventBus, which alerts the listening BoardFragmentModel
     * @param action From GameStateChangeEvent. Right now, only action is to switch view player
     */
    private void addGameStateChangeEvent(int action) {
        GameStateChangeEvent event = new GameStateChangeEvent(action);
        MagicLog.d(TAG, "addGameStateChangeEvent: " + event.toString());

        mGameStateChangeEventBus.addEvent(event);
    }
}
