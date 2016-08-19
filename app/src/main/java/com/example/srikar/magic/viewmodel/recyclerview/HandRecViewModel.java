package com.example.srikar.magic.viewmodel.recyclerview;

import android.app.Activity;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.adapter.BaseRecViewAdapter;
import com.example.srikar.magic.adapter.HandRecViewAdapter;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Hand;

import javax.inject.Inject;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores Cards in hand
 * Created by Srikar on 6/29/2016.
 */
public class HandRecViewModel extends BaseRecyclerViewModel {
    @Inject
    protected Hand mHand;

    public HandRecViewModel(Activity activity) {
        super(activity, DataModelConstants.LIST_HAND);
        //gets singleton Hand instance
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    @Override
    protected BaseRecViewAdapter getAdapter() {
        return new HandRecViewAdapter(mActivity, this);
    }

    /**
     * Get the number of Cards in Hand. Called by HandRecViewAdapter
     * @return Number of Cards
     */
    @Override
    public int getItemCount() {
        return mHand.getHandSize();
    }
}
