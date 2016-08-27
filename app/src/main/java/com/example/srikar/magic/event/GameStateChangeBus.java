package com.example.srikar.magic.event;

import rx.Observable;
import rx.Subscription;

/**
 * Event bus for changes to GameState
 * Created by Srikar on 8/26/2016.
 */
public class GameStateChangeBus extends RxEventBus<GameStateChangeEvent> {
    /**
     * Listens for when the player that viewing as switches
     */
    public interface SwitchViewPlayerListener {
        void onSwitchViewPlayer(GameStateChangeEvent event);
    }

    /**
     * Listens for next step in turn
     */
    public interface NextStepListener {
        void onNextStep(GameStateChangeEvent event);
    }

    /**
     * Listens for start of next turn
     */
    public interface NextTurnListener {
        void onNextTurn(GameStateChangeEvent event);
    }

    /**
     * Filter GameStateChangeBus based on action from GameStateChangeEvent
     * @param action Switch view player, next step, or next turn, from GameStateChangeEvent
     * @return Filtered Observable
     */
    Observable<GameStateChangeEvent> getActionEvents(int action) {
        return getEvents().filter(event -> event.action == action);
    }

    /**
     * Subscribe to this bus, listening for switch player events
     * @param listener Interface with onSwitchViewPlayer() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeSwitchViewPlayerListener(SwitchViewPlayerListener listener) {
        return getActionEvents(GameStateChangeEvent.SWITCH_VIEW_PLAYER)
                .subscribe(listener::onSwitchViewPlayer);
    }

    /**
     * Subscribe to this bus, listening for next step events
     * @param listener Interface with onNextStep() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeNextStepListener(NextStepListener listener) {
        return getActionEvents(GameStateChangeEvent.NEXT_STEP)
                .subscribe(listener::onNextStep);
    }

    /**
     * Subscribe to this bus, listening for next turn events
     * @param listener Interface with onNextTurn() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeNextTurnListener(NextTurnListener listener) {
        return getActionEvents(GameStateChangeEvent.NEXT_TURN)
                .subscribe(listener::onNextTurn);
    }
}
