package com.example.srikar.magic.model.zone;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Test the Battlefield class
 * Created by Srikar on 6/9/2016.
 */
public class BattlefieldUnitTest {
    private final int NUM_LANDS = 0;
    private final int NUM_CREATURES = 3;
    private final Battlefield battlefield;

    private final RxEventBus<GameStateChangeEvent> gameStateChangeEventBus;

    /**
     * Constructor, make Battlefield object
     */
    public BattlefieldUnitTest() {
        MagicLog.setLogging(false);
        gameStateChangeEventBus = new RxEventBus<>();
        battlefield = new Battlefield(new RxEventBus<>(), new GameState(gameStateChangeEventBus),
                gameStateChangeEventBus);
    }

    @Before
    /**
     * Starts with three creatures on battlefield on Alice's side
     */
    public void setUp() {
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < NUM_CREATURES; i++) {
            list.add(new Card(i));
        }
        battlefield.setCreatures(DataModelConstants.PLAYER_ALICE, list);
    }

    @After
    /**
     * Clears the lists
     */
    public void tearDown() {
        battlefield.clearLists();
    }

    @Test
    /**
     * Assert that after setUp(), there are NUM_CREATURES creatures and nothing in other lists
     */
    public void testStartCondition() {
        int numLands = battlefield.getViewPlayerLandsSize();
        int numCreatures = battlefield.getViewPlayerCreaturesSize();

        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS);
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES);
    }

    @Test
    /**
     * Assert that getViewPlayerCreature() returns the expected Permanents, given start condition
     */
    public void testGetViewPlayerCreature() {
        for (int i = 0; i < NUM_CREATURES; i++) {
            Card creature = battlefield.getViewPlayerCreature(i);
            assertTrue("The id is " + creature.toString(),
                    creature.toString().compareTo(i + " untapped") == 0);
        }
    }

    @Test
    /**
     * Assert that after add land, there is one land, NUM_CREATURES creatures, and nothing in other
     * lists
     */
    public void testAddLand() {
        battlefield.addLand(DataModelConstants.PLAYER_ALICE, new Card(0));

        int numLands = battlefield.getViewPlayerLandsSize();
        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS + 1);
    }

    @Test
    /**
     * Assert that after add land, getViewPlayerLand() returns the expected Permanent
     */
    public void testGetViewPlayerLand() {
        battlefield.addLand(DataModelConstants.PLAYER_ALICE, new Card(0));

        Card land = battlefield.getViewPlayerLand(NUM_LANDS);
        assertTrue("The id is " + land.toString(),
                land.toString().compareTo("0 untapped") == 0);
    }

//    @Test
//    /**
//     * Assert that after add combat, there is one combat, NUM_CREATURES creatures, and nothing in other
//     * lists
//     */
//    public void testAddCombatCreature() {
//        Card card = new Card(0);
//        battlefield.addCombatCreature(DataModelConstants.PLAYER_ALICE, new Permanent(card));
//
//        int numCombat = battlefield.getViewPlayerCombatSize();
//        assertTrue("Number of lands is " + numCombat, numCombat == NUM_COMBAT + 1);
//    }
//
//    @Test
//    /**
//     * Assert that after add combat, getViewPlayerCombatCreature() returns the expected Permanent
//     */
//    public void testGetViewPlayerCombatCreature() {
//        Card card = new Card(0);
//        battlefield.addCombatCreature(DataModelConstants.PLAYER_ALICE, new Permanent(card));
//
//        Permanent combat = battlefield.getViewPlayerCombatCreature(NUM_COMBAT);
//        assertTrue("The id is " + combat.toString(),
//                combat.toString().compareTo("" + 0) == 0);
//    }
//
//    @Test
//    /**
//     * Assert that putCreatureOnBattlefield() increases size of creatures list and has Permanent
//     * that passed in
//     */
//    public void testPutCreatureOnBattlefield() {
//        Card card = new Card(0);
//        battlefield.putCreatureOnBattlefield(DataModelConstants.PLAYER_ALICE, new Permanent(card));
//
//        //assert number of creatures
//        int numCreatures = battlefield.getViewPlayerCreaturesSize();
//        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES + 1);
//
//        //assert that is creature that just added
//        Permanent creature = battlefield.getViewPlayerCreature(NUM_CREATURES);
//        assertTrue("The id is " + creature.toString(),
//                creature.toString().compareTo("" + 0) == 0);
//    }
//
//    @Test
//    /**
//     * Assert that moveToAttack() moves the permanent at the given position in creatures list to
//     * the combat list
//     */
//    public void testMoveToAttack() {
//        Permanent creature = battlefield.getViewPlayerCreature(0);
//        battlefield.moveToAttack(0);
//
//        //assert lengths of creature and combat lists
//        int numCreatures = battlefield.getViewPlayerCreaturesSize();
//        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES - 1);
//        int numCombat = battlefield.getViewPlayerCombatSize();
//        assertTrue("Number of combat is " + numCombat, numCombat == NUM_COMBAT + 1);
//
//        //assert that moved right creature
//        Permanent combatCreature = battlefield.getViewPlayerCombatCreature(NUM_COMBAT);
//        assertTrue("The creatures aren't the same", creature == combatCreature);
//    }
//
//    @Test
//    /**
//     * Assert that undoAttackDeclaration() moves the permanent at the given position in combat list
//     * to the creatures list
//     */
//    public void testUndoAttackDeclaration() {
//        Card card = new Card(0);
//        Permanent combatCreature = new Permanent(card);
//        battlefield.addCombatCreature(DataModelConstants.PLAYER_ALICE, combatCreature);
//
//        //get the one that just added
//        battlefield.undoAttackDeclaration(NUM_COMBAT);
//
//        //assert lengths of creature and combat lists
//        int numCreatures = battlefield.getViewPlayerCreaturesSize();
//        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES + 1);
//        int numCombat = battlefield.getViewPlayerCombatSize();
//        assertTrue("Number of combat is " + numCombat, numCombat == NUM_COMBAT);
//
//        //assert that moved right creature
//        Permanent creature = battlefield.getViewPlayerCreature(NUM_CREATURES);
//        assertTrue("The creatures aren't the same", creature == combatCreature);
//    }
//
//    /**
//     * For testMoveToAttack_RecyclerViewEventBus(), listen for the Events on RxEventBus
//     * @param event Event used to update RecyclerView in response to change in list
//     */
//    private void testMoveToAttack_RecyclerViewEventBus_Sub(ListChangeEvent event) {
//        if (event.action == ListChangeEvent.Action.ADD) {
//            testMoveToAttack_RecyclerViewEventBus_add =
//                    event.listName == ListChangeEvent.ListName.COMBAT
//                    && event.index == NUM_COMBAT;
//        }
//        else if (event.action == ListChangeEvent.Action.REMOVE) {
//            testMoveToAttack_RecyclerViewEventBus_remove =
//                    event.listName == ListChangeEvent.ListName.LIST_CREATURES
//                    && event.index == 0;
//        }
//    }

//    boolean testMoveToAttack_RecyclerViewEventBus_add = false;
//    boolean testMoveToAttack_RecyclerViewEventBus_remove = false;
//    @Test
//    /**
//     * Assert that moveToAttack() puts two events on RecyclerView event bus and subscriber gets
//     * both
//     */
//    public void testMoveToAttack_RecyclerViewEventBus() {
//        //creates thread to receive Events from RxEventBus
//        Subscription sub = battlefield.getRecyclerViewEvents()
//                .observeOn(Schedulers.computation())
//                .subscribe(this::testMoveToAttack_RecyclerViewEventBus_Sub);
//
//        //will put two Events on RxEventBus
//        battlefield.moveToAttack(0);
//
//        //wait for response
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            sub.unsubscribe();
//            assertTrue("Took too long", false);
//        }
//
//        assertTrue("Add event was incorrect", testMoveToAttack_RecyclerViewEventBus_add);
//        assertTrue("Remove event was incorrect", testMoveToAttack_RecyclerViewEventBus_remove);
//    }
//
//    /**
//     * For testUndoAttackDeclaration_RecyclerViewEventBus(), listen for the Events on RxEventBus
//     * @param event Event used to update RecyclerView in response to change in list
//     */
//    private void testUndoAttackDeclaration_RecyclerViewEventBus_Sub(ListChangeEvent event) {
//        if (event.action == ListChangeEvent.Action.ADD) {
//            testUndoAttackDeclaration_RecyclerViewEventBus_add =
//                    event.listName == ListChangeEvent.ListName.LIST_CREATURES
//                            && event.index == NUM_CREATURES;
//        }
//        else if (event.action == ListChangeEvent.Action.REMOVE) {
//            testUndoAttackDeclaration_RecyclerViewEventBus_remove =
//                    event.listName == ListChangeEvent.ListName.COMBAT
//                            && event.index == NUM_COMBAT;
//        }
//    }
//
//    boolean testUndoAttackDeclaration_RecyclerViewEventBus_add = false;
//    boolean testUndoAttackDeclaration_RecyclerViewEventBus_remove = false;
//    @Test
//    /**
//     * Assert that undoAttackDeclaration() puts two events on RecyclerView event bus and subscriber
//     * gets both
//     */
//    public void testUndoAttackDeclaration_RecyclerViewEventBus() { //creates thread to receive Events from RxEventBus
//        Subscription sub = battlefield.getRecyclerViewEvents()
//                .observeOn(Schedulers.computation())
//                .subscribe(this::testUndoAttackDeclaration_RecyclerViewEventBus_Sub);
//
//        Card card = new Card(0);
//        Permanent combatCreature = new Permanent(card);
//        battlefield.addCombatCreature(DataModelConstants.PLAYER_ALICE, combatCreature);
//
//        //will put two Events on RxEventBus
//        //get the one that just added
//        battlefield.undoAttackDeclaration(NUM_COMBAT);
//
//        //wait for response
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            sub.unsubscribe();
//            assertTrue("Took too long", false);
//        }
//
//        assertTrue("Add event was incorrect", testUndoAttackDeclaration_RecyclerViewEventBus_add);
//        assertTrue("Remove event was incorrect",
//                testUndoAttackDeclaration_RecyclerViewEventBus_remove);
//    }
}
