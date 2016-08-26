package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for switch player button
 * Created by Srikar on 8/25/2016.
 */
public class SwitchPlayerModel extends BaseBoardModel {
    /**
     * View model for switch player button
     * @param binding Binding used to access view that will update
     */
    public SwitchPlayerModel(FragmentBoardBinding binding) {
        super(binding);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.switchPlayer.setBackgroundResource(backgroundResource);
    }
}
