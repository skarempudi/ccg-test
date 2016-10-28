package com.example.srikar.magic.model;

import com.example.srikar.magic.MagicLog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing Card code, mainly focusing on blocking flow
 * Created by Srikar on 10/27/2016.
 */
public class CardUnitTest {
    Card card1, card2, card3;

    @Before
    public void setUp() {
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
    }

    @Test
    public void testDeclareBlock() {
        //true
        //normal case, assert that successfully declared blocking
        //card2 is not yet blocked, do that only after confirm blockers
        card1.declareBlock(true, card2);
        assertTrue("Card 1 not declared blocking", card1.mDeclaredBlocking);
        assertTrue("Card 1 not declared blocking", card1.isDeclaredBlocking());
        assertTrue("Card 1 not blocking card 2", card1.mCreatureThatBlocking == card2);

        //if set different block, changes what card1 is blocking
        card1.declareBlock(true, card3);
        assertTrue("Card 1 not declared blocking", card1.mDeclaredBlocking);
        assertTrue("Card 1 not declared blocking", card1.isDeclaredBlocking());
        assertTrue("Card 1 not blocking card 3", card1.mCreatureThatBlocking == card3);
        assertTrue("Card 1 is blocking card 2", card1.mCreatureThatBlocking != card2);

        //if set null block, card1 is no longer blocking
        card1.declareBlock(true, null);
        assertTrue("Card 1 is declared blocking", !card1.mDeclaredBlocking);
        assertTrue("Card 1 is declared blocking", !card1.isDeclaredBlocking());
        assertNull("Card 1 is blocking a card", card1.mCreatureThatBlocking);

        //false
        //if cancel block, card1 is not blocking
        //start case, not blocking
        card1.declareBlock(false, card2);
        assertTrue("Card 1 is declared blocking", !card1.mDeclaredBlocking);
        assertTrue("Card 1 is declared blocking", !card1.isDeclaredBlocking());
        assertNull("Card 1 is blocking a card", card1.mCreatureThatBlocking);

        //try with null, but should be same outcome no matter what route it takes
        card1.declareBlock(false, null);
        assertTrue("Card 1 is declared blocking", !card1.mDeclaredBlocking);
        assertTrue("Card 1 is declared blocking", !card1.isDeclaredBlocking());
        assertNull("Card 1 is blocking a card", card1.mCreatureThatBlocking);

        //start case, is declared blocking
        card1.declareBlock(true, card2);
        card1.declareBlock(false, card3);
        assertTrue("Card 1 is declared blocking", !card1.mDeclaredBlocking);
        assertTrue("Card 1 is declared blocking", !card1.isDeclaredBlocking());
        assertNull("Card 1 is blocking a card", card1.mCreatureThatBlocking);
    }

    @Test
    public void testIsBlockingThisCreature() {
        //user selects card, displays creatures that can block it, and specially marks cards already
        //blocking it

        //normal case, assert that successfully declared blocking
        card1.declareBlock(true, card2);
        assertTrue("Card 1 isn't blocking card 2", card1.isBlockingThisCreature(card2));

        //if set different block, changes what blocking
        card1.declareBlock(true, card3);
        assertTrue("Card 1 isn't blocking card 3", card1.isBlockingThisCreature(card3));

        //if set null block, no longer blocking
        card1.declareBlock(true, null);
        assertTrue("Card 1 is blocking a card", !card1.isBlockingThisCreature(card2));
        assertTrue("Card 1 still considered blocking", !card1.isBlockingThisCreature(null));

        //if cancel block, no longer blocking
        //start case, not blocking
        card1.declareBlock(false, card2);
        assertTrue("Card 1 is blocking a card", !card1.isBlockingThisCreature(card2));
        assertTrue("Card 1 still considered blocking", !card1.isBlockingThisCreature(null));

        //try with null
        card1.declareBlock(false, null);
        assertTrue("Card 1 is blocking a card", !card1.isBlockingThisCreature(card2));
        assertTrue("Card 1 still considered blocking", !card1.isBlockingThisCreature(null));

        //start case, is declared blocking
        card1.declareBlock(true, card2);
        card1.declareBlock(false, card3);
        assertTrue("Card 1 is blocking card 2", !card1.isBlockingThisCreature(card2));
        assertTrue("Card 1 is blocking card 3", !card1.isBlockingThisCreature(card3));
        assertTrue("Card 1 still considered blocking", !card1.isBlockingThisCreature(null));
    }

    @Test
    public void testOnConfirmBlockers() {
        //normal case, set card1 as blocker for card2, then confirm block
        card1.declareBlock(true, card2);
        card1.onConfirmBlockers();
        assertTrue("Card 2 is not blocked", card2.mBlocked);
        assertTrue("Card 2 is not blocked", card2.isBlocked());

        //reset card2
        card2.mBlocked = false;

        //if set different block, change what is blocked
        card1.declareBlock(true, card3);
        card1.onConfirmBlockers();
        assertTrue("Card 3 is not blocked", card3.mBlocked);
        assertTrue("Card 3 is not blocked", card3.isBlocked());
        assertTrue("Card 2 is blocked", !card2.mBlocked);
        assertTrue("Card 2 is blocked", !card2.isBlocked());

        //reset cards
        card2.mBlocked = false;
        card3.mBlocked = false;

        //if set null, nothing is blocked
        card1.declareBlock(true, null);
        card1.onConfirmBlockers();
        assertTrue("Card 2 is blocked", !card2.mBlocked);
        assertTrue("Card 2 is blocked", !card2.isBlocked());
        assertTrue("Card 3 is blocked", !card3.mBlocked);
        assertTrue("Card 3 is blocked", !card3.isBlocked());

        //if cancel block, no longer blocking
        //start case, not blocking
        card1.declareBlock(false, card2);
        card1.onConfirmBlockers();
        assertTrue("Card 2 is blocked", !card2.mBlocked);
        assertTrue("Card 2 is blocked", !card2.isBlocked());
        assertTrue("Card 3 is blocked", !card3.mBlocked);
        assertTrue("Card 3 is blocked", !card3.isBlocked());

        //try with null
        card1.declareBlock(false, null);
        card1.onConfirmBlockers();
        assertTrue("Card 2 is blocked", !card2.mBlocked);
        assertTrue("Card 2 is blocked", !card2.isBlocked());
        assertTrue("Card 3 is blocked", !card3.mBlocked);
        assertTrue("Card 3 is blocked", !card3.isBlocked());

        //start case, is declared blocking
        card1.declareBlock(true, card2);
        card1.declareBlock(false, card3);
        card1.onConfirmBlockers();
        assertTrue("Card 2 is blocked", !card2.mBlocked);
        assertTrue("Card 2 is blocked", !card2.isBlocked());
        assertTrue("Card 3 is blocked", !card3.mBlocked);
        assertTrue("Card 3 is blocked", !card3.isBlocked());
    }
}
