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
    private final ArrayList<Permanent>[] mLands, mCreatures, mCombat;

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

        mCombat = new ArrayList[2];
        mCombat[PlayerID.ALICE] = new ArrayList<>();
        mCombat[PlayerID.BOB] = new ArrayList<>();

        mRecyclerViewEventBus = rvEventBus;
        mGameState = gameState;
    }

    /**
     * Get a land for player that viewing
     * @param position Position in list that user touching
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
    protected void addLand(int playerID, Permanent land) {
        mLands[playerID].add(land);
    }

    /**
     * Get a creature that is not in combat for player that viewing
     * @param position Position in list that user touching
     * @return Creature
     */
    public Permanent getViewPlayerCreature(int position) {
        return mCreatures[mGameState.getViewPlayer()].get(position);
    }

    /**
     * Add creature not in combat for specified player
     * @param playerID Either PlayerID.ALICE or PlayerID.BOB
     * @param creature Creature
     */
    protected void addCreature(int playerID, Permanent creature) {
        mCreatures[playerID].add(creature);
    }

    /**
     * Get a creature that is in combat: attacking or blocking
     * @param position Position in list that user touching
     * @return Creature
     */
    public Permanent getViewPlayerCombatCreature(int position) {
        return mCombat[mGameState.getViewPlayer()].get(position);
    }

    protected void addCombatCreature(int playerID, Permanent combatCreature) {
        mCombat[playerID].add(combatCreature);
    }

    /**
     * Methods to return size of list
     */
    public int getViewPlayerLandsSize() {
        return mLands[mGameState.getViewPlayer()].size();
    }
    public int getViewPlayerCreaturesSize() {
        return mCreatures[mGameState.getViewPlayer()].size();
    }
    public int getViewPlayerCombatSize() {
        return mCombat[mGameState.getViewPlayer()].size();
    }

    /**
     * Empties all lists
     */
    protected void clearLists() {
        mLands[PlayerID.ALICE].clear();
        mLands[PlayerID.BOB].clear();

        mCreatures[PlayerID.ALICE].clear();
        mCreatures[PlayerID.BOB].clear();

        mCombat[PlayerID.ALICE].clear();
        mCombat[PlayerID.BOB].clear();
    }

    /**
     * Add a new creature Permanent to the mBattlefield for given player
     * @param playerID Either PlayerID.ALICE or PlayerID.BOB
     * @param creature Creature
     */
    public void putCreatureOnBattlefield(int playerID, Permanent creature) {
        addCreature(playerID, creature);
    }

    /**
     * Select a creature to move to combat during your declare attackers step
     * Will throw exception if out of bounds
     * Can only be done with current player when viewing as it
     * @param position Position in list that user touching
     */
    public void moveToAttack(int position) {
//        Log.d(TAG, "moveToAttack: removing from creatures at " + position);
        Permanent creature = mCreatures[mGameState.getViewPlayer()].remove(position);
//        Log.d(TAG, "moveToAttack: creature has id " + creature.toString());
        addCombatCreature(mGameState.getViewPlayer(), creature);
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
                mCombat[mGameState.getViewPlayer()].size() - 1
        );
    }

    /**
     * Select a creature to move out of combat during your declare attackers step
     * Will throw exception if out of bounds
     * Can only be done with current player when viewing as it
     * @param position Position in list that user touching
     */
    public void undoAttackDeclaration(int position) {
//        Log.d(TAG, "undoAttackDeclaration: removing from combat at " + position);
        Permanent creature = mCombat[mGameState.getViewPlayer()].remove(position);
//        Log.d(TAG, "undoAttackDeclaration: creature has id " + creature.toString());
        addCreature(mGameState.getViewPlayer(), creature);
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
                mCreatures[mGameState.getViewPlayer()].size() - 1
        );
    }

    /**
     * EVENT BUS
     */

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
