package com.example.srikar.magic.event;

/**
 * Event used to notify that a list (in Hand or Battlefield) has changed, listened to by view
 * models for RecyclerViews
 * Created by Srikar on 6/6/2016.
 */
public class ListChangeEvent implements MagicEvent {
    /**
     * Action
     */
    public static final int ADD = 0, REMOVE = 1, UPDATE = 2, UPDATE_ALL = 3;

    public final int listName;
    public final int action;
    public final int index;

    /**
     * An event to notify that a list changed, listened to by RecyclerViews
     * @param changedList Which list was updated, using DataModelConstants (hand, lands, creatures)
     * @param updateAction Whether to add, remove, update, or update all, using ListChangeEvent.Action
     * @param listIndex What index of the list to update, not used in update all
     */
    public ListChangeEvent(int changedList, int updateAction, int listIndex) {
        listName = changedList;
        action = updateAction;
        index = listIndex;
    }

    @Override
    public String toString() {
        String listNames[] = {"hand", "lands", "my creatures", "opp creatures"};
        String actions[] = {"add", "remove", "update", "update all"};

        return listNames[listName] + ", " + actions[action] + ", " + index;
    }
}
