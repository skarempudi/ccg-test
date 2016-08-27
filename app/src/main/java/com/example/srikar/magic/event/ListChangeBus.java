package com.example.srikar.magic.event;

import rx.Observable;
import rx.Subscription;

/**
 * Event bus for changes in Battlefield or Hand, that would affect RecyclerView lists
 * Created by Srikar on 8/26/2016.
 */
public class ListChangeBus extends RxEventBus<ListChangeEvent> {
    public interface AddListener {
        void onAdd(ListChangeEvent event);
    }

    public interface RemoveListener {
        void onRemove(ListChangeEvent event);
    }

    public interface UpdateListener {
        void onUpdate(ListChangeEvent event);
    }

    public interface UpdateAllListener {
        void onUpdateAll(ListChangeEvent event);
    }

    /**
     * Filter ListChangeBus based on list name, from DataModelConstants
     * @param listName Hand, lands, or creatures, with int taken from DataModelConstants
     * @return Filtered Observable
     */
    Observable<ListChangeEvent> getListActionEvents(int listName, int action) {
        return getEvents()
                .filter(event -> event.listName == listName)
                .filter(event -> event.action == action);
    }

    /**
     * Subscribe to this bus, listening for add events
     * @param listener Interface with onAdd() method
     * @param listName Name of list that listening for changes to
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeAddListener(AddListener listener, int listName) {
        return getListActionEvents(listName, ListChangeEvent.ADD)
                .subscribe(listener::onAdd);
    }

    /**
     * Subscribe to this bus, listening for remove events
     * @param listener Interface with onRemove() method
     * @param listName Name of list that listening for changes to
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeRemoveListener(RemoveListener listener, int listName) {
        return getListActionEvents(listName, ListChangeEvent.REMOVE)
                .subscribe(listener::onRemove);
    }

    /**
     * Subscribe to this bus, listening for update events
     * @param listener Interface with onUpdate() method
     * @param listName Name of list that listening for changes to
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeUpdateListener(UpdateListener listener, int listName) {
        return getListActionEvents(listName, ListChangeEvent.UPDATE)
                .subscribe(listener::onUpdate);
    }

    /**
     * Subscribe to this bus, listening for update all events
     * @param listener Interface with onUpdateAll() method
     * @param listName Name of list that listening for changes to
     * @return Subscription, which can unsubscribe from
     */
    public Subscription subscribeUpdateAllListener(UpdateAllListener listener, int listName) {
        return getListActionEvents(listName, ListChangeEvent.UPDATE_ALL)
                .subscribe(listener::onUpdateAll);
    }
}
