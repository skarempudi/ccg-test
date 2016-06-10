package com.example.srikar.magic.model;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;

/**
 * Created by Srikar on 6/9/2016.
 */
public class BattlefieldUnitTest {
    final int NUM_LANDS = 0;
    final int NUM_CREATURES = 3;
    final int NUM_COMBAT = 0;
    Battlefield battlefield;

    /**
     * Constructor, make Battlefield object
     */
    public BattlefieldUnitTest() {
        battlefield = new Battlefield(new RxEventBus<>());
    }

    @Before
    /**
     * Starts with three creatures on battlefield
     */
    public void setUp() {
        for (int i = 0; i < NUM_CREATURES; i++) {
            Card card = new Card(i);
            battlefield.addCreature(new Permanent(card));
        }
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
        int numLands = battlefield.getLandsSize();
        int numCreatures = battlefield.getCreaturesSize();
        int numCombat = battlefield.getCombatSize();

        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS);
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES);
        assertTrue("Number of combat is " + numCombat, numCombat == NUM_COMBAT);
    }

    @Test
    /**
     * Assert that getCreature() returns the expected Permanents, given start condition
     */
    public void testGetCreature() {
        for (int i = 0; i < NUM_CREATURES; i++) {
            Permanent creature = battlefield.getCreature(i);
            assertTrue("The id is " + creature.toString(),
                    creature.toString().compareTo("" + i) == 0);
        }
    }

    @Test
    /**
     * Assert that after add land, there is one land, NUM_CREATURES creatures, and nothing in other
     * lists
     */
    public void testAddLand() {
        Card card = new Card(0);
        battlefield.addLand(new Permanent(card));

        int numLands = battlefield.getLandsSize();
        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS + 1);
    }

    @Test
    /**
     * Assert that after add land, getLand() returns the expected Permanent
     */
    public void testGetLand() {
        Card card = new Card(0);
        battlefield.addLand(new Permanent(card));

        Permanent land = battlefield.getLand(NUM_LANDS);
        assertTrue("The id is " + land.toString(),
                land.toString().compareTo("" + 0) == 0);
    }

    @Test
    /**
     * Assert that after add combat, there is one combat, NUM_CREATURES creatures, and nothing in other
     * lists
     */
    public void testAddCombatCreature() {
        Card card = new Card(0);
        battlefield.addCombatCreature(new Permanent(card));

        int numCombat = battlefield.getCombatSize();
        assertTrue("Number of lands is " + numCombat, numCombat == NUM_COMBAT + 1);
    }

    @Test
    /**
     * Assert that after add combat, getCombatCreature() returns the expected Permanent
     */
    public void testGetCombatCreature() {
        Card card = new Card(0);
        battlefield.addCombatCreature(new Permanent(card));

        Permanent combat = battlefield.getCombatCreature(NUM_COMBAT);
        assertTrue("The id is " + combat.toString(),
                combat.toString().compareTo("" + 0) == 0);
    }

    @Test
    /**
     * Assert that putCreatureOnBattlefield() increases size of creatures list and has Permanent
     * that passed in
     */
    public void testPutCreatureOnBattlefield() {
        Card card = new Card(0);
        battlefield.putCreatureOnBattlefield(new Permanent(card));

        //assert number of creatures
        int numCreatures = battlefield.getCreaturesSize();
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES + 1);

        //assert that is creature that just added
        Permanent creature = battlefield.getCreature(NUM_CREATURES);
        assertTrue("The id is " + creature.toString(),
                creature.toString().compareTo("" + 0) == 0);
    }

    @Test
    /**
     * Assert that moveToAttack() moves the permanent at the given position in creatures list to
     * the combat list
     */
    public void testMoveToAttack() {
        Permanent creature = battlefield.getCreature(0);
        battlefield.moveToAttack(0);

        //assert lengths of creature and combat lists
        int numCreatures = battlefield.getCreaturesSize();
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES - 1);
        int numCombat = battlefield.getCombatSize();
        assertTrue("Number of combat is " + numCombat, numCombat == NUM_COMBAT + 1);

        //assert that moved right creature
        Permanent combatCreature = battlefield.getCombatCreature(NUM_COMBAT);
        assertTrue("The creatures aren't the same", creature == combatCreature);
    }

    @Test
    /**
     * Assert that undoAttackDeclaration() moves the permanent at the given position in combat list
     * to the creatures list
     */
    public void testUndoAttackDeclaration() {
        Card card = new Card(0);
        Permanent combatCreature = new Permanent(card);
        battlefield.addCombatCreature(combatCreature);

        //get the one that just added
        battlefield.undoAttackDeclaration(NUM_COMBAT);

        //assert lengths of creature and combat lists
        int numCreatures = battlefield.getCreaturesSize();
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES + 1);
        int numCombat = battlefield.getCombatSize();
        assertTrue("Number of combat is " + numCombat, numCombat == NUM_COMBAT);

        //assert that moved right creature
        Permanent creature = battlefield.getCreature(NUM_CREATURES);
        assertTrue("The creatures aren't the same", creature == combatCreature);
    }

    /**
     * For testMoveToAttack_RecyclerViewEventBus(), listen for the Events on RxEventBus
     * @param event
     */
    private void testMoveToAttack_RecyclerViewEventBus_Sub(RecyclerViewEvent event) {
        if (event.action == RecyclerViewEvent.Action.ADD) {
            testMoveToAttack_RecyclerViewEventBus_add =
                    event.target == RecyclerViewEvent.Target.COMBAT
                    && event.index == NUM_COMBAT;
        }
        else if (event.action == RecyclerViewEvent.Action.REMOVE) {
            testMoveToAttack_RecyclerViewEventBus_remove =
                    event.target == RecyclerViewEvent.Target.CREATURES
                    && event.index == 0;
        }
    }

    boolean testMoveToAttack_RecyclerViewEventBus_add = false;
    boolean testMoveToAttack_RecyclerViewEventBus_remove = false;
    @Test
    /**
     * Assert that moveToAttack() puts two events on RecyclerView event bus and subscriber gets
     * both
     */
    public void testMoveToAttack_RecyclerViewEventBus() {
        //creates thread to receive Events from RxEventBus
        Subscription sub = battlefield.getRecyclerViewEvents()
                .observeOn(Schedulers.computation())
                .subscribe(e -> testMoveToAttack_RecyclerViewEventBus_Sub(e));

        //will put two Events on RxEventBus
        battlefield.moveToAttack(0);

        //wait for response
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            sub.unsubscribe();
            assertTrue("Took too long", false);
        }

        assertTrue("Add event was incorrect", testMoveToAttack_RecyclerViewEventBus_add);
        assertTrue("Remove event was incorrect", testMoveToAttack_RecyclerViewEventBus_remove);
    }

    /**
     * For testUndoAttackDeclaration_RecyclerViewEventBus(), listen for the Events on RxEventBus
     * @param event
     */
    private void testUndoAttackDeclaration_RecyclerViewEventBus_Sub(RecyclerViewEvent event) {
        if (event.action == RecyclerViewEvent.Action.ADD) {
            testUndoAttackDeclaration_RecyclerViewEventBus_add =
                    event.target == RecyclerViewEvent.Target.CREATURES
                            && event.index == NUM_CREATURES;
        }
        else if (event.action == RecyclerViewEvent.Action.REMOVE) {
            testUndoAttackDeclaration_RecyclerViewEventBus_remove =
                    event.target == RecyclerViewEvent.Target.COMBAT
                            && event.index == NUM_COMBAT;
        }
    }

    boolean testUndoAttackDeclaration_RecyclerViewEventBus_add = false;
    boolean testUndoAttackDeclaration_RecyclerViewEventBus_remove = false;
    @Test
    /**
     * Assert that undoAttackDeclaration() puts two events on RecyclerView event bus and subscriber
     * gets both
     */
    public void testUndoAttackDeclaration_RecyclerViewEventBus() { //creates thread to receive Events from RxEventBus
        Subscription sub = battlefield.getRecyclerViewEvents()
                .observeOn(Schedulers.computation())
                .subscribe(e -> testUndoAttackDeclaration_RecyclerViewEventBus_Sub(e));

        Card card = new Card(0);
        Permanent combatCreature = new Permanent(card);
        battlefield.addCombatCreature(combatCreature);

        //will put two Events on RxEventBus
        //get the one that just added
        battlefield.undoAttackDeclaration(NUM_COMBAT);

        //wait for response
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            sub.unsubscribe();
            assertTrue("Took too long", false);
        }

        assertTrue("Add event was incorrect", testUndoAttackDeclaration_RecyclerViewEventBus_add);
        assertTrue("Remove event was incorrect",
                testUndoAttackDeclaration_RecyclerViewEventBus_remove);
    }
}
