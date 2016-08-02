package com.example.srikar.magic.json;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.model.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Used to load default values from JSON in asset folder.
 * Created by Srikar on 7/18/2016.
 */
public class AssetLoader {
    private static final String TAG = "AssetLoader";
    private static final String ASSET_CARDS_FILE = "cards.json";
    private static final String ASSET_CREATURES_FILE = "creatures.json";

    /**
     * Uses cards.json to load default Cards.
     * @return List of Cards, used by Hand data model.
     */
    public static ArrayList<Card> loadCards(Context context) {
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

    public static ArrayList<Card> loadCreatures(Context context) {
        MagicLog.d(TAG, "loadCreatures: Staring load");

        AssetManager assetManager = context.getAssets();
        Gson gson = new Gson();

        try {
            InputStream in = assetManager.open(ASSET_CREATURES_FILE);
            Reader reader = new InputStreamReader(in);
//            JsonReader jsonReader = new JsonReader(reader);
//
//            ArrayList<Card> list = new ArrayList<>();

            Type arrayListType = new TypeToken<ArrayList<Card>>(){}.getType();

            return gson.fromJson(reader, arrayListType);

            //since Permanent holds custom Cards, have to do mixed read
//            jsonReader.beginArray();
//            while (jsonReader.hasNext()) {
//                //{"card":{"id":"#"}}
//                jsonReader.beginObject();
//                //"card":{"id":"#"}
//                jsonReader.nextName();
//                //{"id":"#"}
//                Card card = gson.fromJson(jsonReader, Card.class);
//                MagicLog.d(TAG, "loadCreatures: loaded card " + card.toString());
//                list.add(new Permanent(card));
//                jsonReader.endObject();
//            }
//            jsonReader.endArray();
//            jsonReader.close();
//
//            return list;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        MagicLog.d(TAG, "loadCreatures: Load failed");

        return null;
    }
}
