package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;
import android.widget.ImageView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.CardBinding;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.view.BoardBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by Srikar on 11/1/2016.
 */

public class LandListViewModel extends BaseCardListViewModel {
    @Inject
    Battlefield mBattlefield;

    public LandListViewModel(BoardBinding binding) {
        super(binding, DataModelConstants.LIST_LANDS);

        //injects singleton instance
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mBinding.get().setLandsModel(this);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerLandsSize();
    }

    @Override
    public int getCardLayout() {
        return R.layout.card;
    }

    @Override
    public void onItemClick(ViewDataBinding binding, int position) {
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
        mBinding.get().landsRecyclerview.setBackgroundResource(backgroundResource);
    }
}
