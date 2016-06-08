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
    private static final int NUM_CREATURES = 3;
    private final ArrayList<Permanent> mLands, mCreatures, mCombat;

    /**
     * Used to signal to RecyclerViews for above ArrayLists that lists updated
     */
    private final RxEventBus<RecyclerViewEvent> mRecyclerViewEventBus;

    public Battlefield(RxEventBus<RecyclerViewEvent> rvEventBus) {
        mLands = new ArrayList<>();
        mCreatures = new ArrayList<>();
        for (int i = 0; i < NUM_CREATURES; i++) {
            Card card = new Card(i);
            mCreatures.add(new Permanent(card));
        }
        mCombat = new ArrayList<>();

        mRecyclerViewEventBus = rvEventBus;
    }

    /**
     * Get a land
     * @param position
     * @return
     */
    public Permanent getLand(int position) {
        return mLands.get(position);
    }

    /**
     * Get a creature that is not in combat
     * @param position
     * @return
     */
    public Permanent getCreature(int position) {
        return mCreatures.get(position);
    }

    /**
     * Get a creature that is in combat: attacking or blocking
     * @param position
     * @return
     */
    public Permanent getCombatCreature(int position) {
        return mCombat.get(position);
    }

    /**
     * Methods to return size of list
     */
    public int getLandsSize() {
        return mLands.size();
    }
    public int getCreaturesSize() {
        return mCreatures.size();
    }
    public int getCombatSize() {
        return mCombat.size();
    }

    /**
     * Add a new creature Permanent to the mBattlefield
     * @param creature
     */
    public void putCreatureOnBattlefield(Permanent creature) {
        mCreatures.add(creature);
    }

    /**
     * Select a creature to move to combat during your declare attackers step
     * Will throw exception if out of bounds
     * @param position
     */
    public void moveToAttack(int position) {
        Log.d(TAG, "moveToAttack: removing from creatures at " + position);
        Permanent creature = mCreatures.remove(position);
        Log.d(TAG, "moveToAttack: creature has id " + creature.toString());
        mCombat.add(creature);
        //update the RecyclerViews
        //remove from mCreatures
        addRecyclerViewEvent(
                RecyclerViewEvent.Target.CREATURES,
                RecyclerViewEvent.Action.REMOVE,
                position
        );
        //add to mCombat
        addRecyclerViewEvent(
                RecyclerViewEvent.Target.COMBAT,
                RecyclerViewEvent.Action.ADD,
                mCombat.size() - 1
        );
    }

    /**
     * Select a creature to move out of combat during your declare attackers step
     * Will throw exception if out of bounds
     * @param position
     */
    public void undoAttackDeclaration(int position) {
        Log.d(TAG, "undoAttackDeclaration: removing from combat at " + position);
        Permanent creature = mCombat.remove(position);
        Log.d(TAG, "undoAttackDeclaration: creature has id " + creature.toString());
        mCreatures.add(creature);
        //update RecyclerViews
        //remove from mCombat
        addRecyclerViewEvent(
                RecyclerViewEvent.Target.COMBAT,
                RecyclerViewEvent.Action.REMOVE,
                position
        );
        //add to mCreatures
        addRecyclerViewEvent(
                RecyclerViewEvent.Target.CREATURES,
                RecyclerViewEvent.Action.ADD,
                mCreatures.size() - 1
        );
    }

    /**
     * EVENT BUS
     */

    /**
     * Get Observable for event bus for RecyclerViewEvents, which can subscribe to for events that
     * would update the RecyclerViews for mLands, mCreatures, and mCombat
     * @return
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
        Log.d(TAG, "addRecyclerViewEvent: " + event.toString());
        mRecyclerViewEventBus.addEvent(event);
    }
}
