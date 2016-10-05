package com.example.srikar.magic.viewmodel.recyclerview;

import android.databinding.ViewDataBinding;
import android.widget.ImageView;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * View model for Battlefield list of your creatures or lands
 * Created by Srikar on 7/7/2016.
 */
public class BattlefieldListViewModel extends BaseCardListViewModel {
    @Inject
    protected Battlefield mBattlefield;

    public BattlefieldListViewModel(BoardBinding binding, int listName) {
        super(binding, listName);
        //injects singleton instance of Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set in binding, based on list name
        if (listName == DataModelConstants.LIST_CREATURES) {
            binding.get().setCreaturesModel(this);
        }
        else if (listName == DataModelConstants.LIST_LANDS) {
            binding.get().setLandsModel(this);
        }
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

    @Override
    public int getCardLayout() {
        return R.layout.permanent;
    }

    @Override
    public void onItemClick(int position) {
        mBattlefield.onViewPlayerPermanentClicked(mListName, position);
    }

    @Override
    public void onItemLoad(ViewDataBinding binding, int position) {
        //get the image view
        ImageView view = ((PermanentBinding)binding).cardImage;
        //get the Permanent
        Card perm = retrievePermanent(position);

        //if tapped, rotate 90 degrees
        //in future, will instead have a set of custom transformations that apply
        if (perm.isTapped()) {
            MagicLog.d(TAG, "onItemLoad: Drawing tapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(90f)
                    .into(view);
        }
        else {
            MagicLog.d(TAG, "onItemLoad: Drawing untapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .into(view);
        }

        //set power and toughness
        if (mListName == DataModelConstants.LIST_CREATURES) {
            ((PermanentBinding)binding).powerToughness.setText(getPTText(perm));
        }
    }

    /**
     * Using the listName list and the position in that list, get the Permanent Card that matches to this
     * position in the RecyclerView
     * @param position Position in list of Cards
     * @return Permanent
     */
    private Card retrievePermanent(int position) {
        return mBattlefield.getViewPlayerPermanent(mListName, position);
    }

    /**
     * Get text to display for creature power and toughness
     * @param perm Card that getting details from
     * @return Power/toughness string
     */
    public String getPTText(Card perm) {
        CreatureDetails details = perm.details;
        return details.power + "/" + details.toughness;
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
            mBinding.get().landsRecyclerview.setBackgroundResource(backgroundResource);
        }
        else if (mListName == DataModelConstants.LIST_CREATURES) {
            mBinding.get().creaturesRecyclerview.setBackgroundResource(backgroundResource);
        }
    }
}
