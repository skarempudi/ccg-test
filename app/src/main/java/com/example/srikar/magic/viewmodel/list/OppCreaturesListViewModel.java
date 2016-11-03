package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.view.BoardBinding;

import javax.inject.Inject;

/**
 * View model for Battlefield list of opponent's creatures
 * Created by Srikar on 10/7/2016.
 */

public class OppCreaturesListViewModel extends CreaturesListViewModel {

    public OppCreaturesListViewModel(BoardBinding binding) {
        super(binding, DataModelConstants.LIST_OPP_CREATURES);
        //set in binding
        mBinding.get().setOppCreaturesModel(this);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getOtherViewPlayerCreaturesSize();
    }

    @Override
    public void onItemClick(ViewDataBinding binding, int position) {
        onBoardItemClick(binding, position);
    }

    public Card retrieveCreature(int position) {
        return mBattlefield.getOtherViewPlayerCreature(position);
    }

    @Override
    protected float getUntappedRotation() {
        return 180f;
    }

    @Override
    protected float getTappedRotation() {
        return 270f;
    }

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();
        mBinding.get().oppCreatures.setBackgroundResource(backgroundResource);
    }
}
