package com.example.srikar.magic.model.state;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;

/**
 * Used to keep track of the current player and the player viewing as
 * Created by Srikar on 9/15/2016.
 */
public class PlayerInfo extends BaseGameState {
    private static final String TAG = "PlayerInfo";

    private int mCurrentPlayer; //the player whose turn it is
    private int mViewPlayer; //the player whose view is currently being used


    public PlayerInfo(GameStateChangeBus gameStateChangeBus) {
        super(gameStateChangeBus);

        mCurrentPlayer = DataModelConstants.PLAYER_ALICE; //starting player is Alice
        mViewPlayer = DataModelConstants.PLAYER_ALICE;
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
        //if not same, switches view player to current player
        if (mViewPlayer != mCurrentPlayer) {
            switchViewPlayer();
        }
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
        //alert listening view model that view player switched
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
     * Called by Turn when new turn starts
     * Will switch the current player
     */
    public void onNextTurn() {
        switchCurrentPlayer();
    }
}
