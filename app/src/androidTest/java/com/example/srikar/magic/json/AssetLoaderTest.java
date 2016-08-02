package com.example.srikar.magic.json;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.srikar.magic.model.Card;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Test AssetLoader, which is used to load JSON from assets
 * Should be using the test versions of asset files.
 * Created by Srikar on 7/25/2016.
 */
@RunWith(AndroidJUnit4.class)
public class AssetLoaderTest {
    private final Context context;

    public AssetLoaderTest() {
        //gets test's context
        context = InstrumentationRegistry.getContext();
    }

    @Test
    public void testLoadCards() {
        int TEST_SIZE = 6;

        //method being tested
        ArrayList<Card> cards = AssetLoader.loadCards(context);

        //assert that not null
        assertTrue("Card list is null", cards != null);

        //assert that number of cards is 6
        int size = cards.size();
        assertTrue("Size is " + size + " instead of " + TEST_SIZE, size == TEST_SIZE);

        //confirm IDs go from 0 to 5
        for (int i = 0; i < TEST_SIZE; i++) {
            int id = cards.get(i).id;
            assertTrue("Wrong ID for " + i + ", is " + id, i == id);
        }
    }

    @Test
    public void testLoadCreatures() {
        int TEST_SIZE = 3;

        //method being tested
        ArrayList<Card> creatures = AssetLoader.loadCreatures(context);

        //assert that not null
        assertTrue("Creature list is null", creatures != null);

        //assert that number of cards is 3
        int size = creatures.size();
        assertTrue("Size is " + size + " instead of " + TEST_SIZE, size == TEST_SIZE);

        //confirm IDs go from 0 to 2
        for (int i = 0; i < TEST_SIZE; i++) {
            String shouldBe = i + " untapped";
            String id = creatures.get(i).toString();
            assertTrue("Wrong ID for " + i + ", is " + id, shouldBe.compareTo(id) == 0);
        }
    }
}
