package com.example.srikar.magic.viewmodel.recyclerview;

import android.app.Activity;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.adapter.BaseRecViewAdapter;
import com.example.srikar.magic.adapter.BattlefieldRecViewAdapter;
import com.example.srikar.magic.model.zone.Battlefield;

import javax.inject.Inject;

/**
 * RecyclerViewModel used by RecyclerViews that link up to a Battlefield list
 * Created by Srikar on 7/7/2016.
 */
public class BattlefieldRecViewModel extends BaseRecyclerViewModel {
    @Inject
    protected Battlefield mBattlefield;

    public BattlefieldRecViewModel(Activity activity, int listName) {
        super(activity, listName);
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
}
