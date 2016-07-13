package com.example.srikar.magic.event;

/**
 * Events used to notify that GameState has changed in a certain way, listened to by the fragment
 * model
 * Created by Srikar on 7/12/2016.
 */
public class GameStateChangeEvent implements MagicEvent {
    public enum Action {
        SWITCH_VIEW_PLAYER
    }

    public final Action action;

    public GameStateChangeEvent(Action updateAction) {
        action = updateAction;
    }

    @Override
    public String toString() {
        return action + "";
    }
}
