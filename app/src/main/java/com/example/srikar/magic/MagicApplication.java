package com.example.srikar.magic;

import android.app.Application;

import com.example.srikar.magic.dagger.DaggerMainComponent;
import com.example.srikar.magic.dagger.MainComponent;
import com.example.srikar.magic.json.AssetLoader;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.model.zone.Hand;
import com.example.srikar.magic.model.DataModelConstants;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

//        LeakCanary.install(this);

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
        AssetLoader.loadCards(this)
                .subscribeOn(Schedulers.io()) //performs actions on I/O thread
                .onErrorResumeNext(Observable.empty()) //if fail, empty list
                .observeOn(AndroidSchedulers.mainThread()) //set data on main thread
                .subscribe(list -> mHand.setCards(DataModelConstants.PLAYER_ALICE, list));

        //battlefield
        AssetLoader.loadAliceCreatures(this)
                .subscribeOn(Schedulers.io()) //performs actions on I/O thread
                .onErrorResumeNext(Observable.empty()) //if fail, stop
                .observeOn(AndroidSchedulers.mainThread()) //set data on main thread
                .subscribe(list -> mBattlefield.setCreatures(DataModelConstants.PLAYER_ALICE, list));
        AssetLoader.loadBobCreatures(this)
                .subscribeOn(Schedulers.io()) //performs actions on I/O thread
                .onErrorResumeNext(Observable.empty()) //if fail, stop
                .observeOn(AndroidSchedulers.mainThread()) //set data on main thread
                .subscribe(list -> mBattlefield.setCreatures(DataModelConstants.PLAYER_BOB, list));
    }
}
