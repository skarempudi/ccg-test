package com.example.srikar.magic.event;

import rx.Observable;
import rx.Subscription;

/**
 * Event bus for changes in Battlefield or Hand, that would affect RecyclerView lists
 * Created by Srikar on 8/26/2016.
 */
public class ListChangeBus extends RxEventBus<ListChangeEvent> {
    /**
     * Listener for changes to any list represented by a RecyclerView
     * Events are add, delete, update, and update all
     */
    public interface ListChangeListener {
        void onListChange(ListChangeEvent event);
    }

    /**
     * Subscribe to this bus, listening for changes to the specified list
     * @param listener Interface with onListChange()
     * @param listName Which list that want to listen for changes to, are
     *                 DataModelConstants.HAND, LANDS, or CREATURES
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeListChangeListener(ListChangeListener listener, int listName) {
        return getEvents()
                .filter(event -> event.listName == listName)
                .subscribe(listener::onListChange);
    }
}
