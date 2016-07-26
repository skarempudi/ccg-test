package com.example.srikar.magic.event;

/**
 * Events used to notify that GameState has changed in a certain way, listened to by the fragment
 * model
 * Created by Srikar on 7/12/2016.
 */
public class GameStateChangeEvent implements MagicEvent {
    /**
     * Actions
     */
    public static final int SWITCH_VIEW_PLAYER = 0; //changes backgrounds
    public static final int NEXT_STEP = 1; //next step, updates game log

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
