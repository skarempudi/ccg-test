package com.example.srikar.magic.viewmodel.board;

import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.viewmodel.BaseBoardModel;
import com.jakewharton.rxbinding.view.RxView;

import rx.Subscription;

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

        //subscribe to onClick for player switch button
        Subscription switchSub = RxView.clicks(mBinding.switchPlayer)
                .subscribe(this::switchPlayerOnClick);
        mSubscriptions.add(switchSub);
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
     * @param empty Handles void passed by Observable
     */
    void switchPlayerOnClick(Void empty) {
        //disable switch button
        mBinding.switchPlayer.setEnabled(false);

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
            mBinding.switchPlayer.setEnabled(true);
        }
    }
}
