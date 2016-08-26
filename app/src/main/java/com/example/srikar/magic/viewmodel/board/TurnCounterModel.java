package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for the turn counter
 * Created by Srikar on 8/25/2016.
 */
public class TurnCounterModel extends BaseBoardModel {
    /**
     * View model for the turn counter
     * @param binding Binding used to access view that will update
     */
    public TurnCounterModel(FragmentBoardBinding binding) {
        super(binding);
    }

    @Override
    public void updateBackground() {
        //turn background based on current player, not view player
        int backgroundResource = getCurrentPlayerBackground();

        //set resource
        mBinding.turnCounter.setBackgroundResource(backgroundResource);
    }
}
