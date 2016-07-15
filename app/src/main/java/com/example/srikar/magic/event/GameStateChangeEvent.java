package com.example.srikar.magic.event;

/**
 * Events used to notify that GameState has changed in a certain way, listened to by the fragment
 * model
 * Created by Srikar on 7/12/2016.
 */
public class GameStateChangeEvent implements MagicEvent {
    /**
     * Action
     */
    public static final int SWITCH_VIEW_PLAYER = 0;

    public final int action;

    public GameStateChangeEvent(int updateAction) {
        action = updateAction;
    }

    @Override
    public String toString() {
        String actions[] = {"switch view player"};
        return actions[action];
    }
}
