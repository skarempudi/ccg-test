package com.example.srikar.magic;

import android.app.Application;

import com.example.srikar.magic.dagger.DaggerMainComponent;
import com.example.srikar.magic.dagger.MainComponent;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.Permanent;
import com.example.srikar.magic.model.PlayerID;

import javax.inject.Inject;

/**
 * Application class created before everything else and acts independently of Activities.
 * Stores the Dagger component that stores singletons.
 * Created by Srikar on 4/26/2016.
 */
public class MagicApplication extends Application {
    private static MagicApplication INSTANCE;

    private MainComponent mainComponent;

    @Inject
    protected Battlefield mBattlefield;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        mainComponent = DaggerMainComponent
                                .builder()
                                .build();

        //temporary start state, remove later
        mainComponent.inject(this);
        for (int i = 0; i < 3; i++) {
            Card card = new Card(i);
            mBattlefield.addCreature(PlayerID.ALICE, new Permanent(card));
        }
    }

    public static MagicApplication getInstance() {
        return INSTANCE;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
