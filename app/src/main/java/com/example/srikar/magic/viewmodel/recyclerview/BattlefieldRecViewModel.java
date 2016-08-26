package com.example.srikar.magic.viewmodel.recyclerview;

import android.app.Activity;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.adapter.BaseRecViewAdapter;
import com.example.srikar.magic.adapter.BattlefieldRecViewAdapter;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;

import javax.inject.Inject;

/**
 * RecyclerViewModel used by RecyclerViews that link up to a Battlefield list
 * Created by Srikar on 7/7/2016.
 */
public class BattlefieldRecViewModel extends BaseRecyclerViewModel {
    @Inject
    protected Battlefield mBattlefield;

    public BattlefieldRecViewModel(FragmentBoardBinding binding, Activity activity, int listName) {
        super(binding, activity, listName);
        //injects singleton instance of Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    @Override
    protected BaseRecViewAdapter getAdapter() {
        return new BattlefieldRecViewAdapter(mActivity, this, mListName);
    }

    /**
     * Gets the number of items in the RecyclerView from a list in the Battlefield specified during
     * construction of this class.
     * @return Number of items in Battlefield list
     */
    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerListSize(mListName);
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //choose which view to set based on list name
        if (mListName == DataModelConstants.LIST_LANDS) {
            mBinding.landsRecyclerview.setBackgroundResource(backgroundResource);
        }
        else if (mListName == DataModelConstants.LIST_CREATURES) {
            mBinding.creaturesRecyclerview.setBackgroundResource(backgroundResource);
        }
    }
}
