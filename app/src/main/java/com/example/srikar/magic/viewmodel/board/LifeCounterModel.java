package com.example.srikar.magic.viewmodel.board;

import android.content.Context;
import android.databinding.ObservableField;

import com.example.srikar.magic.event.GameStateChangeEvent;
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
    //bound to layout, changes the text for life counters
    public ObservableField<CharSequence> aliceLifeText = new ObservableField<>();
    public ObservableField<CharSequence> bobLifeText = new ObservableField<>();

    @Inject
    LifeTotals mLifeTotals;

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

        //set life texts
        setLifeText();

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
     */
    void setLifeText() {
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
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        super.onGameStateChange(event);
        //update display if life totals change
        if (event.action == GameStateChangeEvent.LIFE_CHANGE) {
            setLifeText();
        }
    }
}
