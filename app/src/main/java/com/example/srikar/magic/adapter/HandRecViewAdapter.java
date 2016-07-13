package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.R;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.viewmodel.CardViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Adapter for the RecyclerView used to hold cards in mHand
 * Created by Srikar on 4/15/2016.
 */
public class HandRecViewAdapter extends BaseRecViewAdapter {
    private static final String TAG = "HandRecViewAdapter";

    /**
     * Constructor
     * @param activityContext Used to inflate views
     */
    public HandRecViewAdapter(Context activityContext, BaseRecyclerViewModel recyclerViewModel) {
        super(activityContext, recyclerViewModel, ListChangeEvent.ListName.HAND);
    }

    @Override
    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a mBinding to the card layout
     */
    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.card,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        //create view model that takes binding, will handle onClick and such
        CardViewModel viewModel = new CardViewModel(binding);

        return new BaseRecViewHolder(binding, viewModel);
    }
}
