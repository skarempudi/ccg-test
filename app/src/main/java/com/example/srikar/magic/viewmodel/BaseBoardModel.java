package com.example.srikar.magic.viewmodel;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.viewmodel.GameViewModel;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Base class for subviews on BoardFragment that aren't inside RecyclerViews
 * Created by Srikar on 8/25/2016.
 */
public abstract class BaseBoardModel implements
        GameViewModel, UpdateBackground, GameStateChangeBus.GameStateChangeListener {
    //used to access views in Fragment
    protected FragmentBoardBinding mBinding;

    //used to access information regarding turns, current player, etc.
    @Inject
    protected GameState mGameState;
    //used to listen for changes to GameState
    @Inject
    protected GameStateChangeBus mGameStateChangeBus;

    //used to store Subscriptions, destroyed in onDestroy()
    protected final CompositeSubscription mSubscriptions;

    /**
     * Base view model for views that aren't inside Recycler Views
     * Will set background by default
     * @param binding Binding used to access views that will update
     */
    public BaseBoardModel(FragmentBoardBinding binding) {
        this(binding, true);
    }

    /**
     * Base view model for views that aren't inside Recycler Views
     * Can specify if want to set background here or not, since BaseRecyclerViewModel won't know
     * what list it represents until after it calls this constructor
     * @param binding Binding used to access views that will update
     * @param isBackgroundReady If true, will set background; if not, won't at this point
     */
    public BaseBoardModel(FragmentBoardBinding binding, boolean isBackgroundReady) {
        //used to access view this will modify
        mBinding = binding;

        //get instance of GameState and GameStateChangeBus
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //used to store Subscriptions
        mSubscriptions = new CompositeSubscription();

        //add subscription to listen for changes to GameState, handles switch view player here
        mSubscriptions.add(mGameStateChangeBus.subscribeGameStateChangeListener(this));

        //set background if ready
        if (isBackgroundReady) {
            updateBackground();
        }
    }

    /**
     * Get background resource based on view player
     * @return Resource for Alice or Bob
     */
    protected int getViewPlayerBackground() {
        if (mGameState.getViewPlayer() == DataModelConstants.PLAYER_ALICE) {
            return R.drawable.alice_border;
        }
        else {
            return R.drawable.bob_border;
        }
    }

    /**
     * Get background resource based on current player
     * @return Resource for Alice or Bob
     */
    protected int getCurrentPlayerBackground() {
        if (mGameState.getCurrentPlayer() == DataModelConstants.PLAYER_ALICE) {
            return R.drawable.alice_border;
        }
        else {
            return R.drawable.bob_border;
        }
    }

    /**
     * Used to unsubscribe when Activity goes through onDestroy()
     */
    @Override
    public void onDestroy() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.action == GameStateChangeEvent.SWITCH_VIEW_PLAYER) {
            //update background when switch view player
            updateBackground();
        }
    }
}
