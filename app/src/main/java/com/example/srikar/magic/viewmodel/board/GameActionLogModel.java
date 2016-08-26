package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for game action log
 * Created by Srikar on 8/25/2016.
 */
public class GameActionLogModel extends BaseBoardModel {
    /**
     * View model for game action log
     * @param binding Binding used to access view that will update
     */
    public GameActionLogModel(FragmentBoardBinding binding) {
        super(binding);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.gameActionLog.setBackgroundResource(backgroundResource);
    }
}
