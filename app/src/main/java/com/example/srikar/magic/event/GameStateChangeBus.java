package com.example.srikar.magic.event;

import rx.Observable;
import rx.Subscription;

/**
 * Event bus for changes to state
 * Created by Srikar on 8/26/2016.
 */
public class GameStateChangeBus extends RxEventBus<GameStateChangeEvent> {
    /**
     * Listen for changes to the state data model
     * Events are switch view player, next step, next turn
     */
    public interface GameStateChangeListener {
        void onGameStateChange(GameStateChangeEvent event);
    }

    /**
     * Subscribe to this bus, listening for state change events
     * @param listener Interface with onGameStateChange()
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeGameStateChangeListener(GameStateChangeListener listener) {
        return getEvents().subscribe(listener::onGameStateChange);
    }
}
