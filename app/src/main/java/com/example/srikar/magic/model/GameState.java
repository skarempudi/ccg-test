package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

/**
 * Class used to keep track of the general game state, such as whose turn it is, the life totals,
 * and if the game has ended.
 * Created by Srikar on 4/26/2016.
 */
public class GameState {
    private static final String TAG = "GameState";

    /**
     * Used to signal to BoardFragmentModel that game state changed
     */
    private final RxEventBus<GameStateChangeEvent> mGameStateChangeEventBus;

    private int mCurrentPlayer; //the player whose turn it is
    private int mViewPlayer; //the player whose view is currently being used

    private int mStep; //what step in the turn it currently is

    public GameState(RxEventBus<GameStateChangeEvent> rxEventBus) {
        mGameStateChangeEventBus = rxEventBus;

        mCurrentPlayer = DataModelConstants.PLAYER_ALICE; //starting player is Alice
        mViewPlayer = DataModelConstants.PLAYER_ALICE;

        mStep = DataModelConstants.STEP_UNTAP; //first step is untap
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
     * Goes to the next step in the turn.
     * When it reaches the end of the steps, it goes to the next player
     */
    public void nextStep() {
        //next step will be in same turn
        if (mStep < DataModelConstants.STEP_END) {
            mStep++;
        }
        //next step will be in next turn
        //don't switch players yet
        else {
            mStep = DataModelConstants.STEP_UNTAP;
        }
        //either way, add to event bus
        addGameStateChangeEvent(GameStateChangeEvent.NEXT_STEP);
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
