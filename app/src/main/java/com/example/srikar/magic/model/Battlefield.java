package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

import java.util.ArrayList;

/**
 * The Battlefield is where Permanents like Creatures get to interact
 * Display separately lands, creatures that are not attacking/blocking,
 * and creatures that are attacking/blocking
 * Created by Srikar on 5/18/2016.
 */
public class Battlefield {
    private static final String TAG = "Battlefield";
    /**
     * Used to signal to RecyclerViews for below ArrayLists that lists updated
     * Injected during construction
     */
    private final RxEventBus<ListChangeEvent> mListChangeEventBus;
    private final GameState mGameState;

    private final ArrayList<Permanent>[] mLands, mCreatures;

    public Battlefield(RxEventBus<ListChangeEvent> rvEventBus, GameState gameState) {
        mListChangeEventBus = rvEventBus;
        mGameState = gameState;

        mLands = new ArrayList[2];
        mLands[PlayerID.ALICE] = new ArrayList<>();
        mLands[PlayerID.BOB] = new ArrayList<>();

        mCreatures = new ArrayList[2];
        mCreatures[PlayerID.ALICE] = new ArrayList<>();
        mCreatures[PlayerID.BOB] = new ArrayList<>();
    }

    /**
     * Get a permanent from the specified list for the player that is viewing
     * @param listName The list that is being targeted
     * @param position The position in the list
     * @return The permanent
     */
    public Permanent getViewPlayerPermanent(ListChangeEvent.ListName listName, int position) {
        switch(listName) {
            case LANDS:
                return getViewPlayerLand(position);

            case CREATURES:
                return getViewPlayerCreature(position);

            default:
                return null;
        }
    }

    /**
     * Get a land for player that viewing
     * @param position Position in list
     * @return Land
     */
    public Permanent getViewPlayerLand(int position) {
        return mLands[mGameState.getViewPlayer()].get(position);
    }

    /**
     * Add land for the specified player
     * @param playerID Either PlayerID.ALICE or PlayerID.BOB
     * @param land Land
     */
    void addLand(int playerID, Permanent land) {
        mLands[playerID].add(land);
    }

    /**
     * Get a creature for player that viewing
     * @param position Position in list
     * @return Creature
     */
    public Permanent getViewPlayerCreature(int position) {
        return mCreatures[mGameState.getViewPlayer()].get(position);
    }

    /**
     * Add creature for specified player
     * @param playerID Either PlayerID.ALICE or PlayerID.BOB
     * @param creature Creature
     */
    public void addCreature(int playerID, Permanent creature) {
        MagicLog.d(TAG, "addCreature: " + creature.toString() + " for " + playerID);
        mCreatures[playerID].add(creature);
    }

    /**
     * Methods to return size of list
     */
    /**
     * Gets the size of the list that matches to the given listName.
     * @param listName The list being targeted
     * @return The size of that list
     */
    public int getViewPlayerListSize(ListChangeEvent.ListName listName) {
        switch(listName) {
            case LANDS:
                return getViewPlayerLandsSize();

            case CREATURES:
                return getViewPlayerCreaturesSize();

            default:
                return 0;
        }
    }

    public int getViewPlayerLandsSize() {
        return mLands[mGameState.getViewPlayer()].size();
    }
    public int getViewPlayerCreaturesSize() {
        return mCreatures[mGameState.getViewPlayer()].size();
    }

    /**
     * Empties all lists
     */
    void clearLists() {
        mLands[PlayerID.ALICE].clear();
        mLands[PlayerID.BOB].clear();

        mCreatures[PlayerID.ALICE].clear();
        mCreatures[PlayerID.BOB].clear();
    }

    /**
     * When a creature Permanent is clicked from the view player creature RecyclerView, determine
     * what to do.
     * Right now, just taps or untaps creature.
     * @param position Position in view player creatures list, which matches position in RecyclerView
     */
    public void onViewPlayerPermanentClicked(ListChangeEvent.ListName listName, int position) {
        switch(listName) {
            case LANDS:
                return;

            case CREATURES:
                onViewPlayerCreatureClicked(position);
                return;

            default:
        }
    }

    /**
     * When a creature Permanent is clicked from the view player creature RecyclerView, determine
     * what to do.
     * Right now, just taps or untaps creature.
     * @param position Position in view player creatures list, which matches position in RecyclerView
     */
    private void onViewPlayerCreatureClicked(int position) {
        //get creature
        Permanent creature = getViewPlayerCreature(position);

        //if creature tapped, untap it
        if (creature.isTapped()) {
            MagicLog.d(TAG, "onViewPlayerCreatureClicked: Creature at position " + position + " tapped, untapping");
            creature.untap();
        }
        //if creature not tapped, tap it
        else {
            MagicLog.d(TAG, "onViewPlayerCreatureClicked: Creature at position " + position + " untapped, tapping");
            creature.tap();
        }

        //alert RecyclerView that position has updated, and Permanent should be drawn tapped or
        //untapped
        addListChangeEvent(
                ListChangeEvent.ListName.CREATURES,
                ListChangeEvent.Action.UPDATE,
                position
        );
    }

    /***********************************************************************************************
     * EVENT BUS
     * Listeners access through Dagger injection for bus, not with getter.
     **********************************************************************************************/
    /**
     * Add event to mListChangeEventBus, which alerts the specified RecyclerView to update
     * @param listName Which RecyclerView, using ListChangeEvent.ListName
     * @param action Whether to add, remove, or update, using ListChangeEvent.Action
     * @param index What index to update in list
     */
    private void addListChangeEvent(ListChangeEvent.ListName listName, ListChangeEvent.Action action, int index) {
        ListChangeEvent event = new ListChangeEvent(listName, action, index);
        MagicLog.d(TAG, "addListChangeEvent: " + event.toString());

        mListChangeEventBus.addEvent(event);
    }
}
