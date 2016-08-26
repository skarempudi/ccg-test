package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for both life counters
 * Created by Srikar on 8/25/2016.
 */
public class LifeCounterModel extends BaseBoardModel {
    /**
     * View model for both life counters
     * @param binding Binding used to access view that will update
     */
    public LifeCounterModel(FragmentBoardBinding binding) {
        super(binding);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource for both views
        mBinding.lifeCounterAlice.setBackgroundResource(backgroundResource);
        mBinding.lifeCounterBob.setBackgroundResource(backgroundResource);
    }
}
