package com.example.srikar.magic.model;

/**
 * Created by Srikar on 4/26/2016.
 */
public class GameState {
    private String mName;
    private int mCurrentPlayer; //the player whose turn it is
    private int mViewPlayer; //the player whose view is currently being used

    public GameState() {
        mName = "Hello GameState";
        mCurrentPlayer = PlayerID.ALICE; //starting player is Alice
        mViewPlayer = PlayerID.ALICE;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    /**
     * Returns ID of player whose turn it is
     * @return
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
     * @return
     */
    public int getViewPlayer() {
        return mViewPlayer;
    }

    /**
     * Switches display to match the other player
     */
    public void switchViewPlayer() {
        mViewPlayer ^= 1; //swaps 0 to 1, and 1 to 0
    }

    /**
     * Return ID of the other player
     * @return
     */
    public int getOtherPlayer() {
        return mCurrentPlayer ^ 1;
    }

    /**
     * Return ID of player that view not focused on
     * @return
     */
    public int getOtherViewPlayer() {
        return mViewPlayer ^ 1;
    }
}
