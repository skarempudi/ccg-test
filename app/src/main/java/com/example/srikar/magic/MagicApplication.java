package com.example.srikar.magic;

import android.app.Application;

import com.example.srikar.magic.dagger.DaggerMainComponent;
import com.example.srikar.magic.dagger.MainComponent;
import com.example.srikar.magic.json.AssetLoader;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Hand;
import com.example.srikar.magic.model.DataModelConstants;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

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
    @Inject
    protected Hand mHand;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        mainComponent = DaggerMainComponent
                                .builder()
                                .build();
        mainComponent.inject(this);

        //load initial hand and battlefield state, loading done in new thread,
        //UI updating done on UI thread by listener
        loadInitialDataModelState();
    }

    public static MagicApplication getInstance() {
        return INSTANCE;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    /**
     * Loads initial data model states in a separate thread.
     * When updates model, will alert any listening RecyclerViews. Doesn't matter if there
     * aren't any at that point
     */
    private void loadInitialDataModelState() {
        //hand
        Observable.just(this)
                .observeOn(Schedulers.newThread()) //performs actions on new thread
                .map(AssetLoader::loadCards) //loads cards from JSON asset file
                .subscribe(list -> mHand.setCards(DataModelConstants.PLAYER_ALICE, list));

        //battlefield
        Observable.just(this)
                .observeOn(Schedulers.newThread()) //performs actions on new thread
                .map(AssetLoader::loadCreatures) //loads permanents from JSON asset file
                .subscribe(list -> mBattlefield.setCreatures(DataModelConstants.PLAYER_ALICE, list));
    }
}
