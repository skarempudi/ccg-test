package com.example.srikar.magic.model.zone;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.state.LifeTotals;
import com.example.srikar.magic.model.state.PlayerInfo;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the Battlefield class
 * Created by Srikar on 6/9/2016.
 */
public class BattlefieldUnitTest {
    private final int NUM_LANDS = 1;
    private final int NUM_CREATURES = 3;

    private Battlefield battlefield;

    private final GameStateChangeBus gameStateChangeBus;
    private final ListChangeBus listChangeBus;
    private PlayerInfo playerInfo;
    private final LifeTotals lifeTotals;

    /**
     * Constructor, make Battlefield object
     */
    public BattlefieldUnitTest() {
        MagicLog.setLogging(false);
        gameStateChangeBus = new GameStateChangeBus();
        listChangeBus = new ListChangeBus();
        playerInfo = new PlayerInfo(gameStateChangeBus);
        lifeTotals = new LifeTotals(gameStateChangeBus);
    }

    @Before
    /**
     * Starts with three creatures on battlefield on Alice and Bob's side
     */
    public void setUp() {
        playerInfo = new PlayerInfo(gameStateChangeBus);
        battlefield = new Battlefield(listChangeBus, playerInfo, lifeTotals);
        ArrayList<Card> aliceList = new ArrayList<>();
        ArrayList<Card> bobList = new ArrayList<>();
        for (int i = 0; i < NUM_CREATURES; i++) {
            aliceList.add(new Card(i));
            bobList.add(new Card(100 + i));
        }
        battlefield.setCreatures(DataModelConstants.PLAYER_ALICE, aliceList);
        battlefield.setCreatures(DataModelConstants.PLAYER_BOB, bobList);
        //add lands
        battlefield.addLand(DataModelConstants.PLAYER_ALICE, new Card(0));
        battlefield.addLand(DataModelConstants.PLAYER_BOB, new Card(100));
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
        playerInfo.switchViewPlayer();
        int numLandsOther = battlefield.getViewPlayerLandsSize();
        int numCreaturesOther = battlefield.getViewPlayerCreaturesSize();
        playerInfo.switchViewPlayer();

        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS);
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES);
        assertTrue("Number of other lands is " + numLandsOther, numLandsOther == NUM_LANDS);
        assertTrue("Number of other creatures is " + numCreaturesOther,
                numCreaturesOther == NUM_CREATURES);
    }

    @Test
    /**
     * Assert that get card from correct list with getViewPlayerCard()
     */
    public void testGetViewPlayerCard() {
        String[] players = {"Alice", "Bob"};
        int[] testIds = {0, 100};

        //assert lands from both players
        Card land;
        for (int i = 0; i < 2; i++) {
            land = battlefield.getViewPlayerCard(DataModelConstants.LIST_LANDS, 0);
            assertTrue(players[i] + " land id is " + land.toString(),
                    land.toString().compareTo(testIds[i] + " untapped") == 0);
            playerInfo.switchViewPlayer();
        }

        //assert creatures from both players
        Card creature;
        for (int i = 0; i < 2; i++) {
            creature = battlefield.getViewPlayerCard(DataModelConstants.LIST_MY_CREATURES, 0);
            assertTrue(players[i] + " creature id is " + creature.toString(),
                    creature.toString().compareTo(testIds[i] + " untapped") == 0);
            playerInfo.switchViewPlayer();
        }

        //test nonexistent list
        Card nonCard;
        for (int i = 0; i < 2; i++) {
            nonCard = battlefield.getViewPlayerCard(999, 0);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);
            playerInfo.switchViewPlayer();
        }

        //test out of bounds
        for (int i = 0; i < 2; i++) {
            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_LANDS, -1);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);
            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_LANDS, NUM_LANDS);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);

            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_MY_CREATURES, -1);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);
            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_MY_CREATURES, NUM_CREATURES);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);
        }

        //clear lists, test
        battlefield.clearLists();
        for (int i = 0; i < 2; i++) {
            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_LANDS, 0);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);

            nonCard = battlefield.getViewPlayerCard(DataModelConstants.LIST_MY_CREATURES, 0);
            assertNull(players[i] + " not null, is " + ((nonCard == null)? "" : nonCard.toString()), nonCard);
        }
    }

    @Test
    /**
     * Assert that get right list sizes with getViewPlayerListSize()
     */
    public void testGetViewPlayerListSize() {
        int numLands = battlefield.getViewPlayerListSize(DataModelConstants.LIST_LANDS);
        int numCreatures = battlefield.getViewPlayerListSize(DataModelConstants.LIST_MY_CREATURES);
        playerInfo.switchViewPlayer();
        int numLandsOther = battlefield.getViewPlayerListSize(DataModelConstants.LIST_LANDS);
        int numCreaturesOther = battlefield.getViewPlayerListSize(DataModelConstants.LIST_MY_CREATURES);
        playerInfo.switchViewPlayer();

        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS);
        assertTrue("Number of creatures is " + numCreatures, numCreatures == NUM_CREATURES);
        assertTrue("Number of other lands is " + numLandsOther, numLandsOther == NUM_LANDS);
        assertTrue("Number of other creatures is " + numCreaturesOther,
                numCreaturesOther == NUM_CREATURES);

        //test nonexistent list
        int numNonCard = battlefield.getViewPlayerListSize(999);
        assertTrue("Not null, size is " + numNonCard, numNonCard == 0);
        playerInfo.switchViewPlayer();
        numNonCard = battlefield.getViewPlayerListSize(999);
        assertTrue("Not null, size is " + numNonCard, numNonCard == 0);
        playerInfo.switchViewPlayer();
    }

    @Test
    /**
     * Assert that after add land, there is one land, NUM_CREATURES creatures, and nothing in other
     * lists
     */
    public void testAddLand() {
        battlefield.addLand(DataModelConstants.PLAYER_ALICE, new Card(10));

        int numLands = battlefield.getViewPlayerLandsSize();
        assertTrue("Number of lands is " + numLands, numLands == NUM_LANDS + 1);
    }

    @Test
    /**
     * Assert that after add land, getViewPlayerLand() returns the expected Permanent
     */
    public void testGetViewPlayerLand() {
        battlefield.addLand(DataModelConstants.PLAYER_ALICE, new Card(10));

        Card land = battlefield.getViewPlayerLand(NUM_LANDS);
        assertTrue("The id is " + land.toString(),
                land.toString().compareTo("10 untapped") == 0);
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

}
