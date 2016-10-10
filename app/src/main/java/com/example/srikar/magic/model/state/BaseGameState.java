package com.example.srikar.magic.model.state;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;

/**
 * Base class for game states, like turn, life total, and current player
 * Created by Srikar on 9/15/2016.
 */
public class BaseGameState {
    private static final String TAG = "BaseGameState";

    /**
     * Used to signal to view models that game state changed
     */
    protected final GameStateChangeBus mGameStateChangeBus;

    public BaseGameState(GameStateChangeBus gameStateChangeBus) {
        mGameStateChangeBus = gameStateChangeBus;
    }

    /***********************************************************************************************
     * EVENT BUS
     * Listeners access through Dagger injection for bus, not with getter.
     **********************************************************************************************/
    /**
     * Add event to mGameStateChangeBus, which alerts the listening view models
     * @param action From GameStateChangeEvent
     */
    protected void addGameStateChangeEvent(int action) {
        GameStateChangeEvent event = new GameStateChangeEvent(action);
        MagicLog.d(TAG, "addGameStateChangeEvent: " + event.toString());

        mGameStateChangeBus.addEvent(event);
    }

    /**
     * Add event to mGameStateChangeBus, which alerts the listening view models
     * Use this version if the action requires an extra detail, such as LIFE_CHANGE requiring a
     * player ID
     * @param action From GameStateChangeEvent
     * @param detail Varies for each action, most don't use this, but LIFE_CHANGE uses player ID
     */
    protected void addGameStateChangeEvent(int action, int detail) {
        GameStateChangeEvent event = new GameStateChangeEvent(action, detail);
        MagicLog.d(TAG, "addGameStateChangeEvent: " + event.toString());

        mGameStateChangeBus.addEvent(event);
    }
}
