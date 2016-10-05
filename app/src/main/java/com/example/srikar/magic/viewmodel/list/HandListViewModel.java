package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;
import android.widget.ImageView;

import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Hand;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores Cards in hand
 * Created by Srikar on 6/29/2016.
 */
public class HandListViewModel extends BaseCardListViewModel {
    @Inject
    protected Hand mHand;

    public HandListViewModel(BoardBinding binding) {
        super(binding, DataModelConstants.LIST_HAND);
        //gets singleton Hand instance
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //sets self in binding
        binding.get().setHandModel(this);
    }

    /**
     * Get the number of Cards in Hand.
     * @return Number of Cards
     */
    @Override
    public int getItemCount() {
        return mHand.getHandSize();
    }

    @Override
    public int getCardLayout() {
        return R.layout.card;
    }

    @Override
    public void onItemClick(int position) {
        //do nothing for now
    }

    @Override
    public void onItemLoad(ViewDataBinding binding, int position) {
        ImageView view = ((CardBinding)binding).cardImage;
        Picasso.with(view.getContext())
                .load(R.drawable.ic_launcher)
                .into(view);
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.get().handRecyclerview.setBackgroundResource(backgroundResource);
    }
}
