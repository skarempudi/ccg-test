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
     * Used to signal to BoardFragmentModel that game state changed
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
     * @param action From GameStateChangeEvent.
     */
    protected void addGameStateChangeEvent(int action) {
        GameStateChangeEvent event = new GameStateChangeEvent(action);
        MagicLog.d(TAG, "addGameStateChangeEvent: " + event.toString());

        mGameStateChangeBus.addEvent(event);
    }
}
