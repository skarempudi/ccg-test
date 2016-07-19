package com.example.srikar.magic.json;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.srikar.magic.model.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    /**
     * Uses cards.json to load default Cards.
     * @return List of Cards, used by Hand data model.
     */
    public static ArrayList<Card> loadCards(Context context) {
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

        return null;
    }
}
