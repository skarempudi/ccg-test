package com.example.srikar.magic.event;

import rx.Subscription;

/**
 * Event bus for changes to GameState
 * Created by Srikar on 8/26/2016.
 */
public class GameStateChangeBus extends RxEventBus<GameStateChangeEvent> {
    public interface SwitchViewPlayerListener {
        void onSwitchViewPlayer(GameStateChangeEvent event);
    }

    public interface NextStepListener {
        void onNextStep(GameStateChangeEvent event);
    }

    public interface NextTurnListener {
        void onNextTurn(GameStateChangeEvent event);
    }

    /**
     * Subscribe to this bus, listening for switch player events
     * @param listener Interface with onSwitchViewPlayer() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeSwitchViewPlayerListener(SwitchViewPlayerListener listener) {
        return getEvents()
                .filter(event -> event.action == GameStateChangeEvent.SWITCH_VIEW_PLAYER)
                .subscribe(listener::onSwitchViewPlayer);
    }

    /**
     * Subscribe to this bus, listening for next step events
     * @param listener Interface with onNextStep() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeNextStepListener(NextStepListener listener) {
        return getEvents()
                .filter(event -> event.action == GameStateChangeEvent.NEXT_STEP)
                .subscribe(listener::onNextStep);
    }

    /**
     * Subscribe to this bus, listening for next turn events
     * @param listener Interface with onNextTurn() method
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeNextTurnListener(NextTurnListener listener) {
        return getEvents()
                .filter(event -> event.action == GameStateChangeEvent.NEXT_TURN)
                .subscribe(listener::onNextTurn);
    }
}
