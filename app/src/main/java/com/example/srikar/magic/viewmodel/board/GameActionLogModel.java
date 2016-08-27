package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for game action log
 * Created by Srikar on 8/25/2016.
 */
public class GameActionLogModel extends BaseBoardModel implements GameStateChangeBus.NextStepListener{
    /**
     * View model for game action log
     * @param binding Binding used to access view that will update
     */
    public GameActionLogModel(FragmentBoardBinding binding) {
        super(binding);
        //add subscription to listen for next step in turn
        mSubscriptions.add(mGameStateChangeBus.subscribeNextStepListener(this));
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.gameActionLog.setBackgroundResource(backgroundResource);

        //set initial text
        setLogText();
    }

    /**
     * Set text in game action log based on current step in game state data model
     */
    void setLogText() {
        //get current step
        int step = mGameState.getCurrentStep();
        //get the string resource ID
        int stringId = DataModelConstants.getStepLogText(step);

        //set the text in log
        mBinding.gameActionLog.setText(stringId);
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onNextStep(GameStateChangeEvent event) {
        //update log text
        setLogText();
    }
}
