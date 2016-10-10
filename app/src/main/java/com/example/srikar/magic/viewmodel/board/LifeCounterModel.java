package com.example.srikar.magic.viewmodel.board;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.TextView;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.state.Turn;
import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.state.LifeTotals;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

/**
 * View model for both life counters
 * Created by Srikar on 8/25/2016.
 */
public class LifeCounterModel extends BaseBoardModel {
    private static final String TAG = "LifeCounterModel";
    //bound to layout, changes the text for life counters
    public ObservableField<CharSequence> aliceLifeText = new ObservableField<>();
    public ObservableField<CharSequence> bobLifeText = new ObservableField<>();
    //changes text for life change
    public ObservableField<CharSequence> aliceLifeChangeText = new ObservableField<>();
    public ObservableField<CharSequence> bobLifeChangeText = new ObservableField<>();

    @Inject
    LifeTotals mLifeTotals;
    @Inject
    Turn mTurn;

    /**
     * View model for both life counters
     * @param binding Binding used to access view that will update
     */
    public LifeCounterModel(BoardBinding binding) {
        super(binding);

        //inject LifeTotals
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set life texts without life change text
        setLifeText(false, GameStateChangeEvent.NO_DETAIL);

        //set in binding
        mBinding.get().setLifeCounterModel(this);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource for both views
        mBinding.get().lifeCounterAlice.setBackgroundResource(backgroundResource);
        mBinding.get().lifeCounterBob.setBackgroundResource(backgroundResource);
    }

    /**
     * Set text for life total displays based on information in game state data model
     * @param lifeChange If want to display text that shows change in life total
     * @param player Only looked at if lifeChange is set; which player had life change
     */
    void setLifeText(boolean lifeChange, int player) {
        Context context = mBinding.get().getRoot().getContext();

        //get names
        String aliceName = context.getResources().getString(R.string.alice);
        String bobName = context.getResources().getString(R.string.bob);

        //get colors
        String aliceColor = context.getResources().getString(R.string.alice_color);
        String bobColor = context.getResources().getString(R.string.bob_color);

        //get life totals
        int aliceLife = mLifeTotals.getLifeTotal(DataModelConstants.PLAYER_ALICE);
        int bobLife = mLifeTotals.getLifeTotal(DataModelConstants.PLAYER_BOB);

        //get unformatted strings
        String unformat = context.getResources().getString(R.string.unformat_life);

        //format the strings: color, name, life total
        String partialAlice = String.format(unformat, aliceColor, aliceName, aliceLife);
        String partialBob = String.format(unformat, bobColor, bobName, bobLife);

        //use HTML to finalize
        CharSequence formatAlice = UiUtil.formatHTML(partialAlice);
        CharSequence formatBob = UiUtil.formatHTML(partialBob);

        //set in life total displays
        aliceLifeText.set(formatAlice);
        bobLifeText.set(formatBob);

        //if a player's life has changed, then display by how much in second text view
        //if want to display life change, then show it, don't remove until next step
        if (lifeChange) {
            int change = mLifeTotals.getLifeChange(player);
            String display = (change == 0)? null : "" + mLifeTotals.getLifeChange(player);
            MagicLog.d(TAG, "setLifeText: " + display);
            if (player == DataModelConstants.PLAYER_ALICE) {
                aliceLifeChangeText.set(display);
            }
            else {
                bobLifeChangeText.set(display);
            }
        }
        //if don't want to display life change, then remove it
        else {
            aliceLifeChangeText.set(null);
            bobLifeChangeText.set(null);
        }
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        super.onGameStateChange(event);
        //reset life change display on next step, unless combat damage step
        if ((event.action == GameStateChangeEvent.NEXT_STEP || event.action == GameStateChangeEvent.NEXT_TURN)
                && mTurn.getCurrentStep() != DataModelConstants.STEP_COMBAT_DAMAGE) {
            setLifeText(false, GameStateChangeEvent.NO_DETAIL);
        }
        //update display if life totals change
        if (event.action == GameStateChangeEvent.LIFE_CHANGE) {
            setLifeText(true, event.detail);
        }
    }
}
