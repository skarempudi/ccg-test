package com.example.srikar.magic.viewmodel.board;

import android.content.Context;
import android.databinding.ObservableField;

import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

/**
 * View model for both life counters
 * Created by Srikar on 8/25/2016.
 */
public class LifeCounterModel extends BaseBoardModel {

    public ObservableField<CharSequence> aliceLifeText = new ObservableField<>();
    public ObservableField<CharSequence> bobLifeText = new ObservableField<>();

    /**
     * View model for both life counters
     * @param binding Binding used to access view that will update
     */
    public LifeCounterModel(FragmentBoardBinding binding) {
        super(binding);
        //set life texts
        setLifeText();

        //set in binding
        binding.setLifeCounterModel(this);
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource for both views
        mBinding.lifeCounterAlice.setBackgroundResource(backgroundResource);
        mBinding.lifeCounterBob.setBackgroundResource(backgroundResource);
    }

    /**
     * Set text for life total displays based on information in game state data model
     */
    void setLifeText() {
        Context context = mBinding.getRoot().getContext();

        //get names
        String aliceName = context.getResources().getString(R.string.alice);
        String bobName = context.getResources().getString(R.string.bob);

        //get colors
        String aliceColor = context.getResources().getString(R.string.alice_color);
        String bobColor = context.getResources().getString(R.string.bob_color);

        //get life totals
        int aliceLife = mGameState.getLifeTotal(DataModelConstants.PLAYER_ALICE);
        int bobLife = mGameState.getLifeTotal(DataModelConstants.PLAYER_BOB);

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
}
