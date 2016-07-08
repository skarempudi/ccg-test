package com.example.srikar.magic.viewmodel.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.adapter.HandRecViewAdapter;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Hand;

import javax.inject.Inject;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores Cards in hand
 * Created by Srikar on 6/29/2016.
 */
public class HandRecViewModel extends BaseRecyclerViewModel {
    @Inject
    protected Hand mHand;

    public HandRecViewModel(Context appContext) {
        super(appContext, RecyclerViewEvent.Target.HAND);
        //gets singleton Hand instance
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new HandRecViewAdapter(mContext, this);
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
