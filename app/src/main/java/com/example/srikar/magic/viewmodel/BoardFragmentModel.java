package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.PlayerID;
import com.example.srikar.magic.viewmodel.recyclerview.CreaturesBfRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.LandsBfRecViewModel;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Handles interaction between the BoardFragment and the data models.
 * Created by Srikar on 7/6/2016.
 */
public class BoardFragmentModel extends BaseObservable {
    private static final String TAG = "BoardFragmentModel";
    @Inject
    protected GameState mGameState;

    private Context mContext;
    private FragmentBoardBinding mBinding;

    //used to store all onClick subscriptions, so can unsubscribe when destroy
    private CompositeSubscription mOnClickSubs;

    /**
     * RecyclerView Models, which handle more complex interactions and communicate with the
     * data model classes.
     */
    HandRecViewModel mHandRecViewModel;
    LandsBfRecViewModel mLandsRecViewModel;
    CreaturesBfRecViewModel mCreaturesRecViewModel;

    public BoardFragmentModel(Context context, FragmentBoardBinding binding) {
        Log.d(TAG, "BoardFragmentModel: Created");
        //get instance of GameState
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mContext = context;
        mBinding = binding;

        //set the backgrounds
        setBackgrounds();

        //create the RecyclerViewModels
        createRecyclerViewModels();

        //attach the view models to the binding
        binding.setHandModel(mHandRecViewModel);
        binding.setLandsModel(mLandsRecViewModel);
        binding.setCreaturesModel(mCreaturesRecViewModel);
    }

    /**
     * Used to create the RecyclerViewModels that handle interaction with the data model
     */
    private void createRecyclerViewModels() {
        if (mHandRecViewModel == null) {
            mHandRecViewModel = new HandRecViewModel(mContext);
        }
        if (mLandsRecViewModel == null) {
            mLandsRecViewModel = new LandsBfRecViewModel(mContext);
        }
        if (mCreaturesRecViewModel == null) {
            mCreaturesRecViewModel = new CreaturesBfRecViewModel(mContext);
        }
    }

    /**
     * Set backgrounds based on the current view player
     */
    private void setBackgrounds() {
        int backgroundResource;
        if (mGameState.getViewPlayer() == PlayerID.ALICE) {
            Log.d(TAG, "setBackgrounds: Alice");
            backgroundResource = R.drawable.alice_border;
        }
        else {
            Log.d(TAG, "setBackgrounds: Bob");
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