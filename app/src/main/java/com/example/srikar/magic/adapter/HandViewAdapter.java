package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.R;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.model.Hand;
import com.example.srikar.magic.viewmodel.CardViewModel;

import javax.inject.Inject;

/**
 * Adapter for the RecyclerView used to hold cards in mHand
 * Created by Srikar on 4/15/2016.
 */
public class HandViewAdapter extends RecyclerView.Adapter<HandViewAdapter.HandViewHolder> {
    private static final String TAG = "HandViewAdapter";
    @Inject
    public Hand mHand;
    final Context mContext;

    /**
     * Constructor
     * @param activityContext Used to inflate views
     */
    public HandViewAdapter(Context activityContext) {
        super();
        mContext = activityContext;
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    /**
     * Used to hold the layout data for each element of the list
     */
    public class HandViewHolder extends RecyclerView.ViewHolder {
        private CardBinding mBinding;

        public HandViewHolder(CardBinding binding) {
            super(binding.cardImage);
            mBinding = binding;
        }

        public CardBinding getBinding() {
            return mBinding;
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

        return new HandViewHolder(binding);
    }

    @Override
    /**
     * When list entry becomes visible on screen, set the view model and onClick
     * When click, just logs data now
     */
    public void onBindViewHolder(HandViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        //get the mBinding from the holder
        CardBinding cardBinding = holder.getBinding();
        //get the card that corresponds to this position
        Card card = mHand.getCard(position);

        //set card model and what happens when click view
        cardBinding.setCardViewModel(new CardViewModel(
                mContext,
                card,
                v -> card.onClick()
            )
        );
    }

    @Override
    /**
     * Number of elements in list equal to number of cards in hand
     */
    public int getItemCount() {
        return mHand.getHandSize();
    }
}
