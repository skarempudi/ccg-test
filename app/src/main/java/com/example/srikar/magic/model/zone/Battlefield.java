package com.example.srikar.magic.model.zone;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.state.LifeTotals;
import com.example.srikar.magic.model.state.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * The Battlefield is where Permanents like Creatures get to interact
 * Display separately lands and creatures.
 * Stores data for both players.
 * Created by Srikar on 5/18/2016.
 */
public class Battlefield extends BaseGameZone {
    private static final String TAG = "Battlefield";

    private final LifeTotals mLifeTotals;
    private final List<Card>[] mLands, mCreatures;

    /**
     * Holds the lands and creatures used by both players.
     * Constructed by dependency injection.
     * @param listChangeBus Event bus used to pass information to listening RecyclerViewModels
     * @param playerInfo Used to determine who the current player is
     * @param lifeTotals Keeps track of player life totals, so can deal damage
     */
    public Battlefield(ListChangeBus listChangeBus, PlayerInfo playerInfo, LifeTotals lifeTotals) {
        super(listChangeBus, playerInfo);
        mLifeTotals = lifeTotals;

        mLands = new List[2];
        mLands[DataModelConstants.PLAYER_ALICE] = new ArrayList<>();
        mLands[DataModelConstants.PLAYER_BOB] = new ArrayList<>();

        mCreatures = new List[2];
        mCreatures[DataModelConstants.PLAYER_ALICE] = new ArrayList<>();
        mCreatures[DataModelConstants.PLAYER_BOB] = new ArrayList<>();
    }

    /**
     * Get a card from the specified list for the player that is viewing
     * @param listName The list being retrieved from, using list name constants
     * @param position The position in the list
     * @return The card
     */
    public Card getViewPlayerCard(int listName, int position) {
        switch(listName) {
            case DataModelConstants.LIST_LANDS:
                return getViewPlayerLand(position);

            case DataModelConstants.LIST_MY_CREATURES:
                return getViewPlayerCreature(position);

            default:
                return null;
        }
    }

    /**
     * Gets the size of the list that matches to the given listName.
     * @param listName The list being targeted, using list name constants
     * @return The size of that list
     */
    public int getViewPlayerListSize(int listName) {
        switch(listName) {
            case DataModelConstants.LIST_LANDS:
                return getViewPlayerLandsSize();

            case DataModelConstants.LIST_MY_CREATURES:
                return getViewPlayerCreaturesSize();

            default:
                return 0;
        }
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
     * Get a land for player that viewing
     * @param position Position in list
     * @return Land
     */
    public Card getViewPlayerLand(int position) {
        return mLands[mPlayerInfo.getViewPlayer()].get(position);
    }

    public int getViewPlayerLandsSize() {
        return mLands[mPlayerInfo.getViewPlayer()].size();
    }

    /**
     * Sets creatures for player to new list, alerts listening RecyclerViewModels to change.
     * Fine even if there aren't any listening.
     * @param playerID Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @param creatures List of creatures
     */
    public void setCreatures(int playerID, List<Card> creatures) {
        MagicLog.d(TAG, "setCreatures: " + creatures.toString() + " for " + playerID);
        mCreatures[playerID] = creatures;
        addListChangeEvent(
                DataModelConstants.LIST_MY_CREATURES,
                ListChangeEvent.UPDATE_ALL,
                0
        );
    }

    /**
     * Get a creature for player that viewing
     * @param position Position in list
     * @return Creature
     */
    public Card getViewPlayerCreature(int position) {
        return mCreatures[mPlayerInfo.getViewPlayer()].get(position);
    }

    public Card getOtherViewPlayerCreature(int position) {
        return mCreatures[mPlayerInfo.getOtherViewPlayer()].get(position);
    }

    public int getViewPlayerCreaturesSize() {
        return mCreatures[mPlayerInfo.getViewPlayer()].size();
    }

    public int getOtherViewPlayerCreaturesSize() {
        return mCreatures[mPlayerInfo.getOtherViewPlayer()].size();
    }

    /**
     * Used to determine if should skip combat for this player or not, since can't attack with no creatures
     * Used by Turn.nextStep() after start of combat, since stuff could still happen in that step
     * @return Number of creatures current player controls
     */
    public int getCurrentPlayerCreaturesSize() {
        return mCreatures[mPlayerInfo.getCurrentPlayer()].size();
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
        int index = mPlayerInfo.getCurrentPlayer();

        //untap all current player lands
        for (int i = 0; i < mLands[index].size(); i++) {
            mLands[index].get(i).untap();
            //since don't have lands implemented yet, no action when untap
        }

        //untap all current player creatures
        for (int i = 0; i < mCreatures[index].size(); i++) {
            if (mCreatures[index].get(i).untap()) {
                //only update the creatures that untap
                addListChangeEvent(DataModelConstants.LIST_MY_CREATURES, ListChangeEvent.UPDATE, i);
            }
        }
    }

    /**
     * Not really a step, but when confirm attackers, tap all attacking creatures that should be tapped
     */
    public void onConfirmAttack() {
        MagicLog.d(TAG, "onAttackersConfirmed: Tap all attackers without vigilance");
        int index = mPlayerInfo.getCurrentPlayer();
        for (int i = 0; i < mCreatures[index].size(); i++) {
            Card creature = mCreatures[index].get(i);
            if (creature.isDeclaredAttacking()) {
                //tap creatures without vigilance
                creature.tap();
                //update those creatures
                int list = (index == mPlayerInfo.getViewPlayer())? DataModelConstants.LIST_MY_CREATURES
                        : DataModelConstants.LIST_OPP_CREATURES;
                addListChangeEvent(list, ListChangeEvent.UPDATE, i);
            }
        }
    }

    /**
     * At start of combat damage step, creatures deal combat damage
     * Includes "step" in name to distinguish from combat damage triggered abilities
     */
    public void onCombatDamageStep() {
        MagicLog.d(TAG, "onCombatDamageStep: Creatures deal combat damage");
        //every attacking creature deals damage, since no blockers yet
        //get list (moving away from using Combat object for this)
        int index = mPlayerInfo.getCurrentPlayer();
        List<Card> attackers = new ArrayList<>();
        for (Card creature : mCreatures[index]) {
            if (creature.isDeclaredAttacking()) {
                attackers.add(creature);
            }
        }
        //send to LifeTotals
        int opponent = mPlayerInfo.getOtherPlayer();
        mLifeTotals.dealCombatDamage(opponent, attackers);
    }

    /**
     * At start of second main phase, remove all creatures from combat
     */
    public void onPostcombatMain() {
        MagicLog.d(TAG, "onSecondMain: Removing all creatures from combat");
        //remove all creatures from combat
        for (int index : new int[]{DataModelConstants.PLAYER_ALICE, DataModelConstants.PLAYER_BOB}) {
            for (int i = 0; i < mCreatures[index].size(); i++) {
                //only update the creatures removed from combat
                if (mCreatures[index].get(i).removeFromCombat()) {
                    //determine which view to update
                    int list = (mPlayerInfo.getViewPlayer() == index)?
                            DataModelConstants.LIST_MY_CREATURES :
                            DataModelConstants.LIST_OPP_CREATURES;
                    addListChangeEvent(list, ListChangeEvent.UPDATE, i);
                }
            }
        }
    }
}
