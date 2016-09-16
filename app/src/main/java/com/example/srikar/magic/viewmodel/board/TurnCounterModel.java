package com.example.srikar.magic.viewmodel.board;

import android.content.Context;
import android.databinding.ObservableField;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.state.Turn;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

/**
 * View model for the turn counter
 * Created by Srikar on 8/25/2016.
 */
public class TurnCounterModel extends BaseBoardModel {
    private static final String TAG = "TurnCounterModel";

    //changes to this will automatically update text view, after set in binding
    public ObservableField<CharSequence> turnText = new ObservableField<>();

    @Inject
    Turn mTurn;

    /**
     * View model for the turn counter
     * @param binding Binding used to access view that will update
     */
    public TurnCounterModel(FragmentBoardBinding binding) {
        super(binding);

        //inject Turn
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set text
        setTurnText();

        //set in binding
        binding.setTurnCounterModel(this);
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
    void setTurnText() {
        Context context = mBinding.getRoot().getContext();

        //get unformatted string
        String unformatted = context.getResources().getString(R.string.unformat_turn_display);

        //get turn number
        int turn = mTurn.getTurnNumber();

        //get current player name
        int currentPlayer = mPlayerInfo.getCurrentPlayer();
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
        turnText.set(formatted);
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        //don't handle background on switch view player, so don't call super
        //on start of next turn, update turn text and update background
        if (event.action == GameStateChangeEvent.NEXT_TURN) {
            setTurnText();
            updateBackground();
        }
    }
}
