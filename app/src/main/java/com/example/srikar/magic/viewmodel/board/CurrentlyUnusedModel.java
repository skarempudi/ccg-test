package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for currently unused views: opposing creatures, library, graveyard
 * They will be split off when they start fulfilling their roles
 * Created by Srikar on 8/25/2016.
 */
public class CurrentlyUnusedModel extends BaseBoardModel {
    /**
     * View model for currently unused views
     * @param binding Binding used to access view that will update
     */
    public CurrentlyUnusedModel(FragmentBoardBinding binding) {
        super(binding);
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resources
        mBinding.oppCreatures.setBackgroundResource(backgroundResource);
        mBinding.library.setBackgroundResource(backgroundResource);
        mBinding.graveyard.setBackgroundResource(backgroundResource);
    }
}
