package com.example.srikar.magic.viewmodel.board;

import android.databinding.ObservableBoolean;
import android.view.View;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for switch player button
 * Created by Srikar on 8/25/2016.
 */
public class SwitchPlayerModel extends BaseBoardModel {
    public ObservableBoolean enabled = new ObservableBoolean(true);

    /**
     * View model for switch player button
     * @param binding Binding used to access view that will update
     */
    public SwitchPlayerModel(FragmentBoardBinding binding) {
        super(binding);

        //set in binding
        binding.setSwitchPlayerModel(this);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.switchPlayer.setBackgroundResource(backgroundResource);
    }

    /**
     * When click the switch player button, switches the view player in data model
     */
    public void switchPlayerOnClick(View view) {
        //disable switch button
        enabled.set(false);

        //switch the player in the data model
        mGameState.switchViewPlayer();
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        //update background
        super.onGameStateChange(event);

        //handles switch view player and next turn
        if (event.action == GameStateChangeEvent.SWITCH_VIEW_PLAYER ||
                event.action == GameStateChangeEvent.NEXT_TURN) {
            //enable switch button
            enabled.set(true);
        }
    }
}
