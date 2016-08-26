package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for next step button
 * Created by Srikar on 8/25/2016.
 */
public class NextStepModel extends BaseBoardModel {
    /**
     * View model for next step button
     * @param binding Binding used to access view that will update
     */
    public NextStepModel(FragmentBoardBinding binding) {
        super(binding);
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.nextStep.setBackgroundResource(backgroundResource);
    }
}
