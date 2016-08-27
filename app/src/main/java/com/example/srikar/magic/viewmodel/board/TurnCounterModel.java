package com.example.srikar.magic.viewmodel.board;

import android.content.Context;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

/**
 * View model for the turn counter
 * Created by Srikar on 8/25/2016.
 */
public class TurnCounterModel extends BaseBoardModel
        implements GameStateChangeBus.NextTurnListener{
    private static final String TAG = "TurnCounterModel";

    @Inject
    protected GameStateChangeBus mGameStateChangeBus;

    /**
     * View model for the turn counter
     * @param binding Binding used to access view that will update
     */
    public TurnCounterModel(FragmentBoardBinding binding) {
        super(binding);

        //used to get game state change event bus
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set text
        setTurnText();

        //listen for turn changes
        mGameStateChangeBus.subscribeNextTurnListener(this);
    }

    @Override
    public void updateBackground() {
        //turn background based on current player, not view player
        int backgroundResource = getCurrentPlayerBackground();

        //set resource
        mBinding.turnCounter.setBackgroundResource(backgroundResource);
    }

    /**
     * Set the current turn number and current player in the turn display
     */
    private void setTurnText() {
        Context context = mBinding.getRoot().getContext();

        //get unformatted string
        String unformatted = context.getResources().getString(R.string.unformat_turn_display);

        //get turn number
        int turn = mGameState.getTurnNumber();

        //get current player name
        int currentPlayer = mGameState.getCurrentPlayer();
        String name;
        if (currentPlayer == DataModelConstants.PLAYER_ALICE) {
            name = context.getResources().getString(R.string.alice);
        }
        else {
            name = context.getResources().getString(R.string.bob);
        }

        //format the string
        String partial = String.format(unformatted, turn, name);
        //uses HTML to bold player name
        CharSequence formatted = UiUtil.formatHTML(partial);

        //set in turn display
        mBinding.turnCounter.setText(formatted);
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onNextTurn(GameStateChangeEvent event) {
        //update turn text
        setTurnText();
    }
}
