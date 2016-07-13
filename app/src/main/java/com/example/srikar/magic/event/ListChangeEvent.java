package com.example.srikar.magic.event;

/**
 * Event used to notify that a list (in Hand or Battlefield) has changed, listened to by view
 * models for RecyclerViews
 * Created by Srikar on 6/6/2016.
 */
public class ListChangeEvent implements MagicEvent {
    public enum ListName {
        HAND,
        LANDS,
        CREATURES
    }

    public enum Action {
        ADD,
        REMOVE,
        UPDATE
    }

    public final ListName listName;
    public final Action action;
    public final int index;

    /**
     * An event to notify that a list changed, listened to by RecyclerViews
     * @param changedList Which list was updated, using ListChangeEvent.ListName
     * @param updateAction Whether to add, remove, or update, using ListChangeEvent.Action
     * @param listIndex What index of the list to update
     */
    public ListChangeEvent(ListName changedList, Action updateAction, int listIndex) {
        listName = changedList;
        action = updateAction;
        index = listIndex;
    }

    @Override
    public String toString() {
        return listName + ", " + action + ", " + index;
    }
}
