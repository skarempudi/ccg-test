package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;

import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.model.DataModelConstants;

/**
 * View model for Battlefield list of your creatures
 * Created by Srikar on 7/7/2016.
 */
public class MyCreaturesListViewModel extends CreaturesListViewModel {

    public MyCreaturesListViewModel(BoardBinding binding) {
        super(binding, DataModelConstants.LIST_MY_CREATURES);

        //set in binding
        mBinding.get().setCreaturesModel(this);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerListSize(mListName);
    }

    @Override
    public void onItemClick(ViewDataBinding binding, int position) {
        onBoardItemClick(binding, position);
    }

    @Override
    protected Card retrieveCreature(int position) {
        return mBattlefield.getViewPlayerCreature(position);
    }

    @Override
    protected float getUntappedRotation() {
        return 0;
    }

    @Override
    protected float getTappedRotation() {
        return 90f;
    }

    /**
     * Used to update the background color of this view, based on the active player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.get().creaturesRecyclerview.setBackgroundResource(backgroundResource);
    }
}
