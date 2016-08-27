package com.example.srikar.magic.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.R;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.viewmodel.card.HandCardViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Adapter for the RecyclerView used to hold cards in mHand
 * Created by Srikar on 4/15/2016.
 */
public class HandRecViewAdapter extends BaseRecViewAdapter {
    private static final String TAG = "HandRecViewAdapter";

    /**
     * Adapter for the RecyclerView used to hold cards
     * @param recyclerViewModel View model that created this adapter
     */
    public HandRecViewAdapter(BaseRecyclerViewModel recyclerViewModel) {
        super(recyclerViewModel, DataModelConstants.LIST_HAND);
    }

    @Override
    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a mBinding to the card layout
     */
    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        //create view model that takes binding, will handle onClick and such
        HandCardViewModel viewModel = new HandCardViewModel(binding);

        return new BaseRecViewHolder(binding, viewModel);
    }
}
