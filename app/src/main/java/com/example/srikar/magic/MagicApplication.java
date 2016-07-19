package com.example.srikar.magic;

import android.app.Application;

import com.example.srikar.magic.dagger.DaggerMainComponent;
import com.example.srikar.magic.dagger.MainComponent;
import com.example.srikar.magic.json.AssetLoader;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.Hand;
import com.example.srikar.magic.model.Permanent;
import com.example.srikar.magic.model.DataModelConstants;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Application class created before everything else and acts independently of Activities.
 * Stores the Dagger component that stores singletons.
 * Created by Srikar on 4/26/2016.
 */
public class MagicApplication extends Application {
    private static MagicApplication INSTANCE;

    private MainComponent mainComponent;

    private CompositeSubscription subscriptions;

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

        subscriptions = new CompositeSubscription();

        //temporary start state, remove later
        for (int i = 0; i < 3; i++) {
            Card card = new Card(i);
            mBattlefield.addCreature(DataModelConstants.PLAYER_ALICE, new Permanent(card));
        }
        //loads from JSON in main thread, but in later versions, will do in separate thread
//        mHand.setCards(AssetLoader.loadCards(this), DataModelConstants.PLAYER_ALICE);

        subscriptions.add(loadInitialHandState());
    }

    public static MagicApplication getInstance() {
        return INSTANCE;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    /**
     * Loads initial Hand state in a separate thread.
     * When updates Hand model, will alert any listening RecyclerViews. Doesn't matter if there
     * aren't any at that point
     * @return Subscription, which add to group of Subscriptions
     */
    private Subscription loadInitialHandState() {
        return Observable.just(this)
                .subscribeOn(Schedulers.newThread()) //performs actions on new thread
                .map(AssetLoader::loadCards) //loads cards from JSON asset file
                .subscribe(list -> mHand.setCards(list, DataModelConstants.PLAYER_ALICE));
    }
}
