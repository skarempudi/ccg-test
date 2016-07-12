package com.example.srikar.magic.model;

import android.util.Log;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;

import java.util.ArrayList;

import rx.Observable;

/**
 * The Battlefield is where Permanents like Creatures get to interact
 * Display separately lands, creatures that are not attacking/blocking,
 * and creatures that are attacking/blocking
 * Created by Srikar on 5/18/2016.
 */
public class Battlefield {
    private static final String TAG = "Battlefield";
    private final ArrayList<Permanent>[] mLands, mCreatures;

    /**
     * Used to signal to RecyclerViews for above ArrayLists that lists updated
     */
    private final RxEventBus<RecyclerViewEvent> mRecyclerViewEventBus;
    private final GameState mGameState;

    public Battlefield(RxEventBus<RecyclerViewEvent> rvEventBus, GameState gameState) {
        mLands = new ArrayList[2];
        mLands[PlayerID.ALICE] = new ArrayList<>();
        mLands[PlayerID.BOB] = new ArrayList<>();

        mCreatures = new ArrayList[2];
        mCreatures[PlayerID.ALICE] = new ArrayList<>();
        mCreatures[PlayerID.BOB] = new ArrayList<>();

        //temporary default permanents
        for (int i = 0; i < 3; i++) {
            Card card = new Card(i);
            mCreatures[PlayerID.ALICE].add(new Permanent(card));
        }

        mRecyclerViewEventBus = rvEventBus;
        mGameState = gameState;
    }

    /**
     * Get a permanent from the specified list for the player that is viewing
     * @param targetList The list that is being targeted
     * @param position The position in the list
     * @return The permanent
     */
    public Permanent getViewPlayerPermanent(RecyclerViewEvent.Target targetList, int position) {
        switch(targetList) {
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
    void addCreature(int playerID, Permanent creature) {
        mCreatures[playerID].add(creature);
    }

    /**
     * Methods to return size of list
     */
    /**
     * Gets the size of the list that matches to the given target.
     * @param targetList The list being targeted
     * @return The size of that list
     */
    public int getViewPlayerListSize(RecyclerViewEvent.Target targetList) {
        switch(targetList) {
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
    public void onViewPlayerPermanentClicked(RecyclerViewEvent.Target targetList, int position) {
        switch(targetList) {
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
            Log.d(TAG, "onViewPlayerCreatureClicked: Creature at position " + position + " tapped, untapping");
            creature.untap();
        }
        //if creature not tapped, tap it
        else {
            Log.d(TAG, "onViewPlayerCreatureClicked: Creature at position " + position + " untapped, tapping");
            creature.tap();
        }

        //alert RecyclerView that position has updated, and Permanent should be drawn tapped or
        //untapped
        addRecyclerViewEvent(
                RecyclerViewEvent.Target.CREATURES,
                RecyclerViewEvent.Action.UPDATE,
                position
        );
    }

    /***********************************************************************************************
     * EVENT BUS
     **********************************************************************************************/

    /**
     * Get Observable for event bus for RecyclerViewEvents, which can subscribe to for events that
     * would update the RecyclerViews for mLands, mCreatures, and mCombat
     * @return Observable that can subscribe to
     */
    public Observable<RecyclerViewEvent> getRecyclerViewEvents() {
        return mRecyclerViewEventBus.getEvents();
    }

    /**
     * Add event to mRecyclerViewEventBus, which alerts the specified RecyclerView to update
     * @param target Which RecyclerView, using RecyclerViewEvent.Target
     * @param action Whether to add or remove, using RecyclerViewEvent.Action
     * @param index What index to update in list
     */
    private void addRecyclerViewEvent(RecyclerViewEvent.Target target, RecyclerViewEvent.Action action, int index) {
        RecyclerViewEvent event = new RecyclerViewEvent(target, action, index);
//        Log.d(TAG, "addRecyclerViewEvent: " + event.toString());
        mRecyclerViewEventBus.addEvent(event);
    }
}
