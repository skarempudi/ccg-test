package com.example.srikar.magic.viewmodel;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.viewmodel.GameViewModel;

import javax.inject.Inject;

/**
 * Base class for subviews on BoardFragment that aren't RecyclerViews
 * Created by Srikar on 8/25/2016.
 */
public abstract class BaseBoardModel implements GameViewModel {
    /**
     * Used to access views in Fragment
     */
    protected FragmentBoardBinding mBinding;

    /**
     * Used to access information regarding turns, current player, etc.
     */
    @Inject
    protected GameState mGameState;

    /**
     * Base view model for views that aren't Recycler Views
     * @param binding Binding used to access view that will update
     */
    public BaseBoardModel(FragmentBoardBinding binding) {
        //used to access view this will modify
        mBinding = binding;

        //get instance of GameState
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
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
}
