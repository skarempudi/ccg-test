package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.srikar.magic.AppConstants;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.PlayerID;
import com.example.srikar.magic.viewmodel.recyclerview.BattlefieldRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Handles interaction between the BoardFragment and the data models.
 * Created by Srikar on 7/6/2016.
 */
public class BoardFragmentModel extends BaseObservable {
    private static final String TAG = "BoardFragmentModel";
    @Inject
    protected GameState mGameState;

    private final Context mContext;
    //view bindings for the Fragment
    private final FragmentBoardBinding mBinding;

    //used to store all onClick subscriptions, so can unsubscribe when destroy
    private final CompositeSubscription mOnClickSubs;

    /**
     * RecyclerView Models, which handle more complex interactions and communicate with the
     * data model classes.
     */
    private HandRecViewModel mHandRecViewModel;
    private BattlefieldRecViewModel mLandsRecViewModel;
    private BattlefieldRecViewModel mCreaturesRecViewModel;

    public BoardFragmentModel(Context context, FragmentBoardBinding binding) {
        MagicLog.d(TAG, "BoardFragmentModel: Created");
        //get instance of GameState
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mContext = context;
        mBinding = binding;

        //used to hold onClick subscriptions
        mOnClickSubs = new CompositeSubscription();

        //set the backgrounds
        setBackgrounds();

        //create the RecyclerViewModels
        createRecyclerViewModels();

        //attach the view models to the binding
        binding.setHandModel(mHandRecViewModel);
        binding.setLandsModel(mLandsRecViewModel);
        binding.setCreaturesModel(mCreaturesRecViewModel);

        //register on click event handlers
        registerOnClicks();
    }

    /**
     * Used to create the RecyclerViewModels that handle interaction with the data model
     */
    private void createRecyclerViewModels() {
        if (mHandRecViewModel == null) {
            mHandRecViewModel = new HandRecViewModel(mContext);
        }
        if (mLandsRecViewModel == null) {
            mLandsRecViewModel = new BattlefieldRecViewModel(mContext, ListChangeEvent.ListName.LANDS);
        }
        if (mCreaturesRecViewModel == null) {
            mCreaturesRecViewModel = new BattlefieldRecViewModel(mContext, ListChangeEvent.ListName.CREATURES);
        }
    }

    /**
     * Set backgrounds based on the current view player
     */
    private void setBackgrounds() {
        int backgroundResource;
        if (mGameState.getViewPlayer() == PlayerID.ALICE) {
            MagicLog.d(TAG, "setBackgrounds: Alice");
            backgroundResource = R.drawable.alice_border;
        }
        else {
            MagicLog.d(TAG, "setBackgrounds: Bob");
            backgroundResource = R.drawable.bob_border;
        }

        //first row - turns, life, switch player
        mBinding.turnCounter.setBackgroundResource(backgroundResource);
        mBinding.lifeCounter.setBackgroundResource(backgroundResource);
        mBinding.switchPlayer.setBackgroundResource(backgroundResource);

        //second row - game action log
        mBinding.gameActionLog.setBackgroundResource(backgroundResource);

        //third row - opposing creatures
        mBinding.oppCreatures.setBackgroundResource(backgroundResource);

        //fourth row - my creatures
        mBinding.creaturesRecyclerview.setBackgroundResource(backgroundResource);

        //fifth row - lands, library, graveyard
        mBinding.landsRecyclerview.setBackgroundResource(backgroundResource);
        mBinding.library.setBackgroundResource(backgroundResource);
        mBinding.graveyard.setBackgroundResource(backgroundResource);

        //sixth row - hand
        mBinding.handRecyclerview.setBackgroundResource(backgroundResource);
    }

    /**
     * Register onClick event handling for views other than RecyclerViews
     * RecyclerView onClicks handled by Permanent
     */
    private void registerOnClicks() {
        //subscribe to onClick for player switch button
        Subscription switchSub = RxView.clicks(mBinding.switchPlayer)
                .throttleFirst(AppConstants.CLICK_DELAY, TimeUnit.MILLISECONDS) //ignore double clicks
                .subscribe(empty -> switchPlayerOnClick());

        mOnClickSubs.add(switchSub);
    }

    /**
     * When click the switch player button, switches the view player.
     * Updates the backgrounds.
     */
    private void switchPlayerOnClick() {
        //switch the player in the data model
        mGameState.switchViewPlayer();

        //TODO: Should separate into listener for EventBus
        //updates the backgrounds
        setBackgrounds();

        //notifies the RecyclerViewModels to update Adapters
        mHandRecViewModel.onViewPlayerSwitched();
        mLandsRecViewModel.onViewPlayerSwitched();
        mCreaturesRecViewModel.onViewPlayerSwitched();
    }

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        //remove onClick subscriptions
        if (mOnClickSubs != null) {
            mOnClickSubs.unsubscribe();
        }

        //for each RecyclerViewModel, remove the subscriptions to RxJava Observables
        mHandRecViewModel.onDestroy();
        mLandsRecViewModel.onDestroy();
        mCreaturesRecViewModel.onDestroy();
    }
}
