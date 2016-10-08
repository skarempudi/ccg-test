package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.view.BoardBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * View model for Battlefield list of opponent's creatures
 * Created by Srikar on 10/7/2016.
 */

public class OppCreaturesListViewModel extends BaseCardListViewModel {
    @Inject
    Battlefield mBattlefield;

    public OppCreaturesListViewModel(BoardBinding binding) {
        super(binding, DataModelConstants.LIST_OPP_CREATURES);
        //inject singleton instances
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
        //set in binding
        mBinding.get().setOppCreaturesModel(this);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getOtherViewPlayerCreaturesSize();
    }

    @Override
    public int getCardLayout() {
        return R.layout.permanent;
    }

    @Override
    public void onItemClick(ViewDataBinding binding, int position) {
        //no action for now
    }

    @Override
    public void onItemLoad(ViewDataBinding binding, int position) {
        PermanentBinding permBinding = (PermanentBinding)binding;

        //get the image view
        ImageView view = permBinding.cardImage;
        //get the creature
        Card perm = retrieveCreature(position);

        //compared to in MyBattlefieldListViewModel, card will be upside down, so rotated 180 degrees
        //if tapped, rotate 270 degrees
        if (perm.isTapped()) {
            MagicLog.d(TAG, "onItemLoad: Drawing tapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(270f)
                    .into(view);
        }
        else {
            MagicLog.d(TAG, "onItemLoad: Drawing untapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(180f)
                    .into(view);
        }

        //if declared attacking, make icon visible, or invisible if not
        if (perm.isDeclaredAttacking()) {
            permBinding.attackIcon.setVisibility(View.VISIBLE);
        }
        else {
            permBinding.attackIcon.setVisibility(View.INVISIBLE);
        }

        //set power and toughness
        permBinding.powerToughness.setText(getPTText(perm));
    }

    private Card retrieveCreature(int position) {
        return mBattlefield.getOtherViewPlayerCreature(position);
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

    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();
        mBinding.get().oppCreatures.setBackgroundResource(backgroundResource);
    }
}
