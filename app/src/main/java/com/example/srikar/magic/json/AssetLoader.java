package com.example.srikar.magic.json;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to load default values from JSON in asset folder.
 * Created by Srikar on 7/18/2016.
 */
public class AssetLoader {
    private static final String TAG = "AssetLoader";
    private static final String ASSET_CARDS_FILE = "cards.json";
    private static final String ASSET_CREATURES_FILE = "creatures.json";
    private static final String ASSET_CREATURES_ALICE_FILE = "creatures_alice.json";
    private static final String ASSET_CREATURES_BOB_FILE = "creatures_bob.json";

    /**
     * Uses cards.json to load default Cards.
     * @return List of Cards, used by Hand data model.
     */
    public static List<Card> loadCards(Context context) {
        MagicLog.d(TAG, "loadCards: Starting load");

        AssetManager assetManager = context.getAssets();
        Gson gson = new Gson();

        try {
            InputStream in = assetManager.open(ASSET_CARDS_FILE);
            Reader reader = new InputStreamReader(in);

            Type arrayListType = new TypeToken<ArrayList<Card>>(){}.getType();

            return gson.fromJson(reader, arrayListType);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        MagicLog.d(TAG, "loadCards: Load failed");

        return null;
    }

    /**
     * Used for testing. Temporary solution until soon-to-come Card overhauling.
     * @param context Used to open assets
     * @return List of cards
     */
    public static List<Card> loadCreatures(Context context) {
        return loadCreaturesHelper(context, ASSET_CREATURES_FILE);
    }

    public static List<Card> loadAliceCreatures(Context context) {
        return loadCreaturesHelper(context, ASSET_CREATURES_ALICE_FILE);
    }

    public static List<Card> loadBobCreatures(Context context) {
        return loadCreaturesHelper(context, ASSET_CREATURES_BOB_FILE);
    }

    private static List<Card> loadCreaturesHelper(Context context, String name) {
        MagicLog.d(TAG, "loadCreatures: Staring load");

        AssetManager assetManager = context.getAssets();
        Gson gson = new Gson();

        try {
            InputStream in = assetManager.open(name);
            Reader reader = new InputStreamReader(in);

            Type arrayListType = new TypeToken<ArrayList<Card>>(){}.getType();

            ArrayList<Card> list =  gson.fromJson(reader, arrayListType);
            for (Card card : list) {
                card.details = new CreatureDetails();
            }
            return list;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        MagicLog.d(TAG, "loadCreatures: Load failed");

        return null;
    }
}
