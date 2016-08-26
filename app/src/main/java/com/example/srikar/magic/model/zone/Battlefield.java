package com.example.srikar.magic.model.zone;

import android.support.annotation.Nullable;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.action.Combat;

import java.util.ArrayList;

/**
 * The Battlefield is where Permanents like Creatures get to interact
 * Display separately lands and creatures.
 * Stores data for both players.
 * Created by Srikar on 5/18/2016.
 */
public class Battlefield extends BaseGameZone {
    private static final String TAG = "Battlefield";

    private final ArrayList<Card>[] mLands, mCreatures;

    private Combat mCombat;

    /**
     * Holds the lands and creatures used by both players.
     * Constructed by dependency injection.
     * @param listChangeBus Event bus used to pass information to listening RecyclerViewModels
     * @param gameState Used to determine who the current player is
     */
    public Battlefield(ListChangeBus listChangeBus, GameState gameState) {
        super(listChangeBus, gameState);

        mGameState.setBattlefield(this);

        mLands = new ArrayList[2];
        mLands[DataModelConstants.PLAYER_ALICE] = new ArrayList<>();
        mLands[DataModelConstants.PLAYER_BOB] = new ArrayList<>();

        mCreatures = new ArrayList[2];
        mCreatures[DataModelConstants.PLAYER_ALICE] = new ArrayList<>();
        mCreatures[DataModelConstants.PLAYER_BOB] = new ArrayList<>();

        mCombat = null;
    }

    /**
     * Add land for the specified player
     * @param playerID Either DataModelConstants.PLAYER_ALICE or DataModelConstants.PLAYER_BOB
     * @param land Land
     */
    void addLand(int playerID, Card land) {
        mLands[playerID].add(land);
    }

    /**
     * Sets creatures for player to new list, alerts listening RecyclerViewModels to change.
     * Fine even if there aren't any listening.
     * @param playerID Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @param creatures List of creatures
     */
    public void setCreatures(int playerID, ArrayList<Card> creatures) {
        MagicLog.d(TAG, "setCreatures: " + creatures.toString() + " for " + playerID);
        mCreatures[playerID] = creatures;
        addListChangeEvent(
                DataModelConstants.LIST_CREATURES,
                ListChangeEvent.UPDATE_ALL,
                0
        );
    }

    /**
     * Get a permanent from the specified list for the player that is viewing
     * @param listName The list being retrieved from, using list name constants
     * @param position The position in the list
     * @return The permanent
     */
    public Card getViewPlayerPermanent(int listName, int position) {
        switch(listName) {
            case DataModelConstants.LIST_LANDS:
                return getViewPlayerLand(position);

            case DataModelConstants.LIST_CREATURES:
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
    public Card getViewPlayerLand(int position) {
        return mLands[mGameState.getViewPlayer()].get(position);
    }

    /**
     * Get a creature for player that viewing
     * @param position Position in list
     * @return Creature
     */
    public Card getViewPlayerCreature(int position) {
        return mCreatures[mGameState.getViewPlayer()].get(position);
    }

    /**
     * Methods to return size of list
     */
    /**
     * Gets the size of the list that matches to the given listName.
     * @param listName The list being targeted, using list name constants
     * @return The size of that list
     */
    public int getViewPlayerListSize(int listName) {
        switch(listName) {
            case DataModelConstants.LIST_LANDS:
                return getViewPlayerLandsSize();

            case DataModelConstants.LIST_CREATURES:
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
     * Used to determine if should skip combat for this player or not, since can't attack with no creatures
     * Used by GameState.nextStep() after start of combat, since stuff could still happen in that step
     * @return Number of creatures current player controls
     */
    public int getCurrentPlayerCreaturesSize() {
        return mCreatures[mGameState.getCurrentPlayer()].size();
    }

    /**
     * Confirm declaration of attackers for combat
     */
    public void confirmAttack() {
        mCombat.setAttackers();
    }

    /**
     * When a creature Permanent is clicked from the view player creature RecyclerView, determine
     * what to do.
     * Right now, just taps or untaps creature.
     * @param listName Which list this event came from, using list name constants
     * @param position Position in view player creatures list, which matches position in RecyclerView
     */
    public void onViewPlayerPermanentClicked(int listName, int position) {
        switch(listName) {
            case DataModelConstants.LIST_LANDS:
                return;

            case DataModelConstants.LIST_CREATURES:
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
        Card creature = getViewPlayerCreature(position);

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
                DataModelConstants.LIST_CREATURES,
                ListChangeEvent.UPDATE,
                position
        );
    }

    /**
     * If it's the declare attackers step and attackers have not been confirmed, then want to
     * confirm with player
     * @return If should confirm attackers
     */
    public boolean shouldConfirmAttack() {
        return mCombat != null && mGameState.getCurrentStep() == DataModelConstants.STEP_DECLARE_ATTACKERS
                && !mCombat.isAttackConfirmed();
    }

    @Override
    protected void clearLists() {
        mLands[DataModelConstants.PLAYER_ALICE].clear();
        mLands[DataModelConstants.PLAYER_BOB].clear();

        mCreatures[DataModelConstants.PLAYER_ALICE].clear();
        mCreatures[DataModelConstants.PLAYER_BOB].clear();
    }

    /***********************************************************************************************
     * ON STEP LISTENERS
     * Listeners for changes in steps in the turn
     **********************************************************************************************/
    /**
     * At start of untap step, untap all permanents current player controls.
     */
    public void onUntapStep() {
        MagicLog.d(TAG, "onUntapStep: Untapping all lands and creatures current player controls");
        int index = mGameState.getCurrentPlayer();

        //untap all lands
        for (int i = 0; i < mLands[index].size(); i++) {
            mLands[index].get(i).untap();
            //since don't have lands implemented yet, no action when untap
        }

        //untap all creatures
        for (int i = 0; i < mCreatures[index].size(); i++) {
            if (mCreatures[index].get(i).untap()) {
                //only update the creatures that untap
                addListChangeEvent(DataModelConstants.LIST_CREATURES, ListChangeEvent.UPDATE, i);
            }
        }
    }

    /**
     * At start of start of combat step, create Combat object
     */
    public void onStartOfCombatStep() {
        mCombat = new Combat();
    }

    /**
     * At start of second main phase, nullify Combat object
     */
    public void onPostcombatMainStep() {
        mCombat = null;
    }
}
