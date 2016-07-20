package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

/**
 * Base class for Battlefield and Hand. Covers data models for game zones a card may travel between.
 * Created by Srikar on 7/20/2016.
 */
public abstract class BaseGameZone {
    private static final String TAG = "BaseGameZone";
    /**
     * Used to signal to associated RecyclerViews that lists updated
     * Injected during construction of subclasses
     */
    protected final RxEventBus<ListChangeEvent> mListChangeEventBus;
    /**
     * Used to determine who the current view player is, and thus which list to use
     */
    protected final GameState mGameState;

    public BaseGameZone(RxEventBus<ListChangeEvent> rvEventBus, GameState gameState) {
        mListChangeEventBus = rvEventBus;
        mGameState = gameState;
    }

    /**
     * Used to clear all lists. Just used by tests.
     */
    protected abstract void clearLists();

    /***********************************************************************************************
     * EVENT BUS
     * Listeners access through Dagger injection for bus, not with getter.
     **********************************************************************************************/
    /**
     * Add event to mListChangeEventBus, which alerts the specified RecyclerView to update
     * @param listName Which RecyclerView, using DataModelConstants list names
     * @param action Whether to add, remove, or update, using ListChangeEvent actions
     * @param index What index to update in list
     */
    protected void addListChangeEvent(int listName, int action, int index) {
        ListChangeEvent event = new ListChangeEvent(listName, action, index);
        MagicLog.d(TAG, "addListChangeEvent: " + event.toString());

        mListChangeEventBus.addEvent(event);
    }
}
