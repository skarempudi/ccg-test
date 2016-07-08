package com.example.srikar.magic.event;

/**
 * Event used to tell a specified RecyclerView to notifyDataSetChanged()
 * For adding, will only notify for the index added, but with removing, will have to notify that
 * item removed
 * Created by Srikar on 6/6/2016.
 */
public class RecyclerViewEvent implements MagicEvent {
    public enum Target {
        HAND,
        LANDS,
        CREATURES
    }

    public enum Action {
        ADD,
        REMOVE,
        UPDATE
    }

    public Target target;
    public Action action;
    public int index;

    /**
     * An event to update a RecyclerView
     * @param targetRecyclerView Which RecyclerView to update, using RecyclerViewEvent.Target
     * @param updateAction Whether to add or remove, using RecyclerViewEvent.Action
     * @param listIndex What index of the list to update
     */
    public RecyclerViewEvent(Target targetRecyclerView, Action updateAction, int listIndex) {
        target = targetRecyclerView;
        action = updateAction;
        index = listIndex;
    }

    @Override
    public String toString() {
        return target + ", " + action + ", " + index;
    }
}
