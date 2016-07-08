package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.R;
import com.example.srikar.magic.viewmodel.CardViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * There is currently no complex hand interaction, so this is separated from the Battlefield-related code.
 * Adapter for the RecyclerView used to hold cards in mHand
 * Created by Srikar on 4/15/2016.
 */
public class HandRecViewAdapter extends RecyclerView.Adapter<HandRecViewAdapter.HandViewHolder> {
    private static final String TAG = "HandRecViewAdapter";
    private final Context mContext;
    //the RecyclerViewModel that created this
    protected final BaseRecyclerViewModel mRecyclerViewModel;

    /**
     * Constructor
     * @param activityContext Used to inflate views
     */
    public HandRecViewAdapter(Context activityContext, BaseRecyclerViewModel recyclerViewModel) {
        super();
        mContext = activityContext;
        mRecyclerViewModel = recyclerViewModel;
    }

    /**
     * Used to hold the layout data for each element of the list
     */
    public class HandViewHolder extends RecyclerView.ViewHolder {
        public CardBinding binding;
        public CardViewModel viewModel;

        public HandViewHolder(CardBinding binding, CardViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
        }
    }

    @Override
    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a mBinding to the card layout
     */
    public HandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.card,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        //create view model that takes binding, will handle onClick and such
        CardViewModel viewModel = new CardViewModel(binding);

        return new HandViewHolder(binding, viewModel);
    }

    @Override
    /**
     * When list entry becomes visible on screen, set the view model and onClick
     * When click, just logs data now
     */
    public void onBindViewHolder(HandViewHolder holder, int position) {
        //update position of the view model
        holder.viewModel.setListPosition(position);
        //load the image
        holder.viewModel.loadImage();
    }

    @Override
    /**
     * Number of elements in list equal to number of cards in hand
     */
    public int getItemCount() {
        return mRecyclerViewModel.getItemCount();
    }
}
