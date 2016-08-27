package com.example.srikar.magic.event;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * When a change happens to a singleton like Battlefield, that event is sent through this bus,
 * and anything listening will react to that event.
 * Creates link between the model and the view (adapter) with low coupling.
 * Created by Srikar on 6/6/2016.
 */
public class RxEventBus<T extends MagicEvent> {
    private final PublishSubject<T> subject;

    public RxEventBus() {
        subject = PublishSubject.create();
    }

    /**
     * Add an event to the bus
     * Will trigger anything subscribed to the Observable provided by getEvents()
     * @param t Event to be put on bus
     */
    public void addEvent(T t) {
        subject.onNext(t);
    }

    /**
     * Used to subscribe to this Observable
     * When subscribe, will respond every time something is added to the bus
     * @return Get observable for Events that can subscribe to
     */
    public Observable<T> getEvents() {
        return subject;
    }
}
